package org.speedyfs.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.speedyfs.model.Album;
import org.speedyfs.model.AlbumMap;
import org.springframework.stereotype.Repository;

@Repository
public class AlbumMapDAOImpl extends HibernateDao<AlbumMap, Integer> implements AlbumMapDAO {
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public boolean deleteSharedAlbum(int albumId) {
		try {
			log.info("Deleting all entries against album_id:" + albumId + " from table: SHARE_ALBUM");
			SQLQuery query = currentSession().createSQLQuery("DELETE FROM SHARE_ALBUM WHERE album_id=?");
			query.setInteger(0, albumId).executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Album> getUserSharedAlbums(String user) {
		log.info("Fetching all entries against user:" + user + " from table: SHARE_ALBUM");
		SQLQuery query = currentSession()
				.createSQLQuery("SELECT ALBUM_ID, ALBUM_NAME, SHARED_USER FROM SHARE_ALBUM WHERE SHARED_USER=?");
		List<Object[]> rows = query.setString(0, user).list();
		return convertAlbumList(rows, user);
	}

	/**
	 * Method converts list of albumMap to list of album.
	 *  
	 * @param list
	 * @return
	 */
	private List<Album> convertAlbumList(List<Object[]> list, String user) {
		List<Album> albumList = new ArrayList<Album>();
		for (Object[] item : list) {
			Album album = new Album();
			album.setAlbumId(Integer.valueOf(item[0].toString()));
			String albumName = item[1] != null ? item[1].toString() : "";
			album.setAlbumName(albumName);
			album.setAccess(2);
			album.setSharedWith(user);
			album.setCreatedBy("");
			album.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			albumList.add(album);
		}
		return albumList;
	}
}
