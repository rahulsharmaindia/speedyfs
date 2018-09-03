package org.speedyfs.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.speedyfs.dao.AlbumDAO;
import org.speedyfs.dao.AlbumMapDAO;
import org.speedyfs.dao.MediaDAO;
import org.speedyfs.dto.AlbumDTO;
import org.speedyfs.dto.DirectoryListDTO;
import org.speedyfs.dto.ImageDTO;
import org.speedyfs.dto.ImageListDTO;
import org.speedyfs.exception.AlbumException;
import org.speedyfs.model.Album;
import org.speedyfs.model.FileData;
import org.speedyfs.utils.CommonUtility;
import org.speedyfs.utils.ThumbnailUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class AlbumServiceImpl implements AlbumService {
	@Autowired
	AlbumDAO albumDAO;
	@Autowired
	MediaDAO mediaDAO;
	@Autowired
	AlbumMapDAO albumMapDAO;
	private Log log = LogFactory.getLog(this.getClass());

	@Override
	public Album saveAlbum(AlbumDTO albumDto, String user, String albumPath) {
		if (StringUtils.isBlank(albumDto.getDirectoryName()) || StringUtils.isBlank(user)) {
			throw new AlbumException("Album Name or user is blank.");
		}
		log.debug("Creating new album " + albumDto.getDirectoryName() + " on path " + albumPath);
		File albumDir = new File(albumPath);
		Album album = new Album();
		album.setAlbumName(albumDto.getDirectoryName());
		album.setAlbumPath(albumDir.getAbsolutePath());
		album.setAccess(albumDto.getAccess());
		album.setCreatedBy(user);		
		if (!albumDir.exists() && albumDir.mkdirs()) {
			log.debug("Created new album " + albumDto.getDirectoryName() + " on path " + albumPath + " and now creating in database" + albumDir.exists());
			albumDAO.add(album);			
			new ThumbnailUtility().createThumbDirectories(albumPath);
			return album;
		} else {
			throw new AlbumException("Album Name: " + albumDto.getDirectoryName() + " is already present on server.");
		}
	}

	@Override
	public AlbumDTO deleteAlbum(Album album, String dir) {
		String albumName = album.getAlbumName();
		List<Long> photoIdList = mediaDAO.getPhotoIdList(albumName);
		boolean flag = mediaDAO.deleteAllImagesOfIds(photoIdList);
		log.debug("STATUS of images deletion from album:" + flag);
		AlbumDTO albumDTO = new AlbumDTO();
		albumDTO.setAccess(album.getAccess());
		albumDTO.setDirectoryId(album.getAlbumId());
		albumDTO.setDirectoryName(album.getAlbumName());
		albumDAO.remove(album);		
		deleteDirectory(new File(dir));		
		return albumDTO;
	}

	@Override
	public void modifyAlbumAccess(Album album) {
		albumDAO.update(album);		
	}	
	
	@Override
	public DirectoryListDTO getUserAlbumList(String serverName, String user) {		
		DirectoryListDTO dto = new DirectoryListDTO();
		List<Album> albumList = getUpdatedAlbumList(getUserAllAlbums(user));		
		dto.setDirNames(albumList);
		dto.setServerName(serverName);
		return dto;
	}
	
	/**
	 * Method returns list of albums 
	 * @param albumList
	 * @return
	 */
	private List<Album> getUpdatedAlbumList(List<Album> albumList) {
		List<Album> list = new ArrayList<Album>();
		for (Album album : albumList) {
			album.setAlbumName(new CommonUtility().getVirtualAlbumName(album.getAlbumName()));
			list.add(album);
		}
		return list;
	}
	
	@Deprecated
	@Override
	public List<Album> getUserAlbumList(String user) {
		List<Album> albums = albumDAO.list();
		List<Album> userAlbums = new ArrayList<Album>();
		for (Album album : albums) {
			/**
			 * Below semi-colon concatenated as required by native app.
			 */
			String shareWith = album.getSharedWith();
			if (StringUtils.isNotBlank(shareWith)) {
				shareWith = shareWith + ";";
			}
			if (album.getCreatedBy() != null && album.getCreatedBy().equalsIgnoreCase(user)) {
				album.setSharedWith(shareWith);
				userAlbums.add(album);
			} else if (album.getAccess() == 1) {
				album.setSharedWith(shareWith);
				userAlbums.add(album);
			} else if (album.getAccess() == 2) {
				String usersString = album.getSharedWith();
				if(StringUtils.isNotBlank(usersString)) {
					List<String> usersList = Arrays.asList(usersString.split("\\s*;\\s*"));
					for (String userId : usersList) {
						if (user.equalsIgnoreCase(userId)) {
							album.setSharedWith(shareWith);
							userAlbums.add(album);
							break;
						}
					}
				}
				
			}
		}

		return userAlbums;
	}
	
	@Override
	public List<Album> getUserAllAlbums(String user) {
		List<Album> albumList = albumDAO.getAlbumsOfUser(user);
		List<Album> sharedAlbumList = albumMapDAO.getUserSharedAlbums(user);
		albumList.addAll(sharedAlbumList);
		return albumList;
	}
	
	@Override 
	public boolean deleteSharedAlbumsEntries(int albumId) {
		return albumMapDAO.deleteSharedAlbum(albumId);
	}
	
	@Override
	public ImageListDTO getAlbumFiles(String albumName, String albumPath) {
		ImageListDTO imageListDTO = new ImageListDTO();
		List<FileData> imageDataList = albumDAO.getAlbumFiles(albumName);
		ImageDTO dto = null;
		List<ImageDTO> imageDTOs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		if (!CollectionUtils.isEmpty(imageDataList)) {
			imageDTOs = new ArrayList<ImageDTO>();
			File album = new File(albumPath);			
			imageListDTO.setImageFile(imageDataList);
			imageListDTO.setLastModified(Long.toString(album.lastModified()));
			imageListDTO.setPath(albumName);
		}
		return imageListDTO;
	}

	@Override
	public Album getAlbum(Integer albumId) {
		return albumDAO.find(albumId);
	}

	@Override
	public Album getAlbum(String albumName) {
		Album album = albumDAO.getAlbum(albumName);
		if(null != album) {
			album.setAlbumName(new CommonUtility().getVirtualAlbumName(album.getAlbumName()));	
		}		
		return album;
	}
	/**
	 * Used to delete the directory and all files inside it.
	 * 
	 * @param album
	 */
	public void deleteDirectory(File album) throws SecurityException {
		File[] files = album.listFiles();
		if (files != null && files.length > 0) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteDirectory(f);
				} else {
					f.delete();
				}
			}
		}
		album.delete();
	}
	
	@Override
	public Album getDefaultAlbum(AlbumDTO albumDto, String user, String albumPath) {
		File albumDir = new File(albumPath);
		Album album = new Album();
		album.setAlbumName(albumDto.getDirectoryName());
		album.setAlbumPath(albumDir.getAbsolutePath());
		album.setAccess(albumDto.getAccess());
		album.setCreatedBy(user);
		if (!albumDir.exists() && albumDir.mkdirs()) {
			log.debug("Created new album " + albumDto.getDirectoryName() + " on path " + albumPath
					+ " and now creating in database" + albumDir.exists());
			albumDAO.add(album);
			new ThumbnailUtility().createThumbDirectories(albumPath);			
		}
		album.setAlbumName(new CommonUtility().getVirtualAlbumName(album.getAlbumName()));
		return album;
	}
}
