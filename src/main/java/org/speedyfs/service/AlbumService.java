package org.speedyfs.service;

import java.util.List;

import org.speedyfs.dto.AlbumDTO;
import org.speedyfs.dto.DirectoryListDTO;
import org.speedyfs.dto.ImageListDTO;
import org.speedyfs.model.Album;

public interface AlbumService {
	/**
	 * Service Method which created album and saves its entry to album_data table.
	 * 
	 * @param albumName
	 * @param user
	 * @param access
	 * @param path
	 * @return
	 */
	Album saveAlbum(AlbumDTO albumDto, String user, String path);

	/**
	 * Service Method to delete album.
	 * 
	 * @param album
	 * @param string
	 * @return
	 */
	AlbumDTO deleteAlbum(Album album, String string);

	/**
	 * Service Method to modify access level-Private-0, Public-1 or Shared-2.
	 * 
	 * @param album
	 */
	void modifyAlbumAccess(Album album);

	/**
	 * Service Method to give album {@link Album} against albumId.
	 * 
	 * @param albumId
	 * @return
	 */
	Album getAlbum(Integer albumId);
	
	/**
	 * Service Method to give album {@link Album} against albumName.
	 * 
	 * @param albumName
	 * @return
	 */
	Album getAlbum(String albumName);

	/**
	 * Service Method gives list of albums in {@link DirectoryListDTO} for specific
	 * user.
	 * 
	 * @param serverName
	 * @param user
	 * @return
	 */
	DirectoryListDTO getUserAlbumList(String serverName, String user);

	/**
	 * Service Method gives list of album.
	 * 
	 * @param user
	 * @return
	 */
	List<Album> getUserAlbumList(String user);

	/**
	 * Service Method returns list of media files.
	 * 
	 * @param albumName
	 * @param albumPath
	 * @return
	 */
	ImageListDTO getAlbumFiles(String albumName, String albumPath);

	/**
	 * Service Method returns list of albums-private, public and shared of selected
	 * user.
	 * 
	 * @param user
	 * @return
	 */
	List<Album> getUserAllAlbums(String user);

	/**
	 * Method deletes all entries of shared album if user changes access level of
	 * the album to private.
	 * 
	 * @param user
	 * @return
	 */
	boolean deleteSharedAlbumsEntries(int albumId);
	
	/**
	 * Method fetch Album details if exists else create default album.
	 * 
	 * @param albumDto
	 * @param user
	 * @param albumPath
	 * @return
	 */
	public Album getDefaultAlbum(AlbumDTO albumDto, String user, String albumPath);

}
