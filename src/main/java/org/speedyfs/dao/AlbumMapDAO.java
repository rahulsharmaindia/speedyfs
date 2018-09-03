package org.speedyfs.dao;

import java.util.List;

import org.speedyfs.model.Album;
import org.speedyfs.model.AlbumMap;

public interface AlbumMapDAO extends GenericDao<AlbumMap, Integer> {

	/**
	 * Method returns list of albums associated with specific user.
	 * 
	 * @param user
	 * @return
	 */
	List<Album> getUserSharedAlbums(String user);
	
	/**
	 * Method deletes album shared.
	 * 
	 * @param albumId
	 * @return
	 */
	boolean deleteSharedAlbum(int albumId);
}
