package org.speedyfs.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.speedyfs.dao.MediaDAO;
import org.speedyfs.dao.MediaThumbnailDAO;
import org.speedyfs.dto.ThumbBatchReqDTO;
import org.speedyfs.enums.Thumbnail;
import org.speedyfs.exception.MediaException;
import org.speedyfs.model.Album;
import org.speedyfs.model.FileData;
import org.speedyfs.model.ThumbFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class MediaServiceImpl implements MediaService {
	@Autowired
	MediaDAO mediaDAO;
	@Autowired
	MediaThumbnailDAO thumbDAO;
	@Autowired
	AlbumService albumService;
	@Autowired
	ResourcePathService resourcePathService;

	@Override
	public void saveImageData(FileData vo) throws HibernateException {
		mediaDAO.saveImageData(vo);
	}

	@Override
	public void saveThumbnails(ThumbFile imageFile) throws HibernateException {
		thumbDAO.add(imageFile);
	}

	@Override
	public void updateImageData(FileData obj) throws HibernateException {
		mediaDAO.updateImageData(obj);
	}	

	@Override
	public FileData loadImageData(Long imageId) {
		FileData imageData = mediaDAO.loadImageData(imageId);
		return imageData;
	}

	@Override
	public boolean deleteImageData(Long imageId) {
		return mediaDAO.deleteImageData(imageId);
	}

	@Override
	public Map<Long, byte[]> loadImages(ThumbBatchReqDTO obj, Thumbnail thumb) {
		List<ThumbFile> imageDataList = mediaDAO.loadImages(obj, thumb);
		Map<Long, byte[]> images = null;
		if (!CollectionUtils.isEmpty(imageDataList)) {
			images = new HashMap<>();
			switch (thumb) {
			case HIGH:
				for (ThumbFile imageFile : imageDataList) {
					images.put(imageFile.getFile_id(), imageFile.getLarge());
				}
				break;
			case MEDIUM:
				for (ThumbFile imageFile : imageDataList) {
					images.put(imageFile.getFile_id(), imageFile.getMedium());
				}
				break;
			case LOW:
				for (ThumbFile imageFile : imageDataList) {
					images.put(imageFile.getFile_id(), imageFile.getSmall());
				}
				break;
			default:
				break;
			}
		}
		return images;
	}

	@Override
	public boolean deleteImage(Long imageId, String user) {
		FileData imageData = loadImageData(imageId);
		List<Album> albumlist = albumService.getUserAlbumList(user);
		String albumNameOfImag = imageData.getAlbumName();
		boolean isAlbumExist = false;
		for (Album album : albumlist) {
			if (albumNameOfImag.equals(album.getAlbumName())) {
				isAlbumExist = true;
				break;
			}
		}

		if (isAlbumExist) {
			File image = new File(resourcePathService.getFilePath(imageData));
			boolean deleted = deleteImageData(imageId);
			if (deleted && image.exists() && image.delete()) {
				ThumbFile imageFile = new ThumbFile();
				imageFile.setFile_id(imageId);
				thumbDAO.remove(imageFile);
				return true;
			} else {
				return false;
			}
		} else {
			throw new MediaException("Delete failed! Logged-in user is not the owner of this album.");
		}

	}

	@Override
	public byte[] loadThumbnail(Long photoId, Thumbnail size) {
		ThumbFile imageFile = mediaDAO.loadThumbnail(photoId);
		byte[] thumbnail;
		switch (size) {
		case HIGH:
			thumbnail = imageFile.getLarge();
			break;
		case MEDIUM:
			thumbnail = imageFile.getMedium();
			break;
		default:
			thumbnail = imageFile.getSmall();
			break;
		}
		return thumbnail;
	}

	@Override
	public FileData getContentComments(Long imageId) {
		return mediaDAO.loadImageData(imageId);
	}
}
