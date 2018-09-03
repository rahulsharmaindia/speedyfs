package org.speedyfs.dao;

import java.util.List;

import org.speedyfs.model.Album;
import org.speedyfs.model.FileData;

public interface AlbumDAO extends GenericDao<Album, Integer> {

	/**
	 * Method returns list of data from table file_data.
	 * 
	 * @param albumName
	 * @return
	 */
	List<FileData> getAlbumFiles(String albumName);
	
	/**
	 * Method returns list of albums against selected user.
	 * 
	 * @param user
	 * @return
	 */
	List<Album> getAlbumsOfUser(String user);

	/**
	 * 
	 * @param albumName
	 * @return
	 */
	Album getAlbum(String albumName);	
}
