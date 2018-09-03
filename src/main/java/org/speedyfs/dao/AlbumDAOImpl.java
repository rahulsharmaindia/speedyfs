package org.speedyfs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import org.speedyfs.model.Album;
import org.speedyfs.model.FileData;
import org.speedyfs.transformer.AliasToBeanNestedResultTransformer;

@Repository
public class AlbumDAOImpl extends HibernateDao<Album, Integer> implements AlbumDAO {
	@SuppressWarnings("unchecked")
	@Override
	public List<FileData> getAlbumFiles(String albumName) {
		List<FileData> fileList = null;
		ProjectionList projections = Projections.projectionList().add(Projections.property("file_id").as("file_id"))
				.add(Projections.property("name").as("name")).add(Projections.property("size").as("size"))
				/*.add(Projections.property("file_path").as("file_path"))*/
				.add(Projections.property("lastModified").as("lastModified"))
				.add(Projections.property("media_type").as("media_type"))
				.add(Projections.property("creation_date").as("creation_date"));

		Criteria criteria = currentSession().createCriteria(FileData.class).add(Restrictions.eq("albumName", albumName))
				.setProjection(projections)
				.setResultTransformer(new AliasToBeanNestedResultTransformer(FileData.class));
		criteria.setCacheable(true);
		fileList = criteria.list();
		return fileList;
	}

	@Override
	public List<Album> getAlbumsOfUser(String user) {
		Criteria crit = currentSession().createCriteria(Album.class);
		Criterion cr1 = Restrictions.eq("createdBy", user);
		Criterion cr2 = Restrictions.eq("access", 1);
		crit.add(Restrictions.or(cr1, cr2));
		@SuppressWarnings("unchecked")
		List<Album> list = (List<Album>) crit.list();
		return list;
	}

	@Override
	public Album getAlbum(String albumName) {
		Criteria crit = currentSession().createCriteria(Album.class).add(Restrictions.eq("albumName", albumName));
		Album album = (Album) crit.uniqueResult();
		return album;
	}
}
