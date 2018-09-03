package org.speedyfs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.speedyfs.dto.ThumbBatchReqDTO;
import org.speedyfs.enums.Thumbnail;
import org.speedyfs.model.FileData;
import org.speedyfs.model.ThumbFile;
import org.springframework.stereotype.Repository;

@Repository
public class MediaDAOImpl extends HibernateDao<FileData, String> implements MediaDAO {
	@Override
	public void saveImageData(FileData vo) {
		add(vo);
	}

	@Override
	public void updateImageData(FileData obj) {
		Session session = null;
		session = currentSession();
		Criteria criteria = session.createCriteria(FileData.class);
		criteria.add(Restrictions.eq("file_id", obj.getFile_id()));
		criteria.setCacheable(true);
		FileData clientImageData = (FileData) criteria.uniqueResult();
		clientImageData.setComments(obj.getComments());
		session.flush();
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public List<FileData> loadImageDataList(ThumbBatchReqDTO thumbBatchReqDTO) {
		List<FileData> imageList = null;
		ProjectionList projections = Projections.projectionList().add(Projections.property("file_id").as("file_id")).add(Projections.property("name").as("name"))
				.add(Projections.property("size").as("size")).add(Projections.property("file_path").as("file_path")).add(Projections.property("lastModified").as("lastModified"));
		Criteria criteria = currentSession().createCriteria(FileData.class).add(Restrictions.in("file_id", thumbBatchReqDTO.getImageIds()))
				.setProjection(projections)
				.setResultTransformer(Transformers.aliasToBean(FileData.class));
		criteria.setCacheable(true);
		imageList = criteria.list();
		return imageList;
	}

	@Override
	public FileData loadImageData(Long imageId) {
		FileData image = null;
		Criteria criteria = currentSession().createCriteria(FileData.class).add(Restrictions.eq("file_id", imageId));
		criteria.setCacheable(true);
		image = (FileData) criteria.uniqueResult();
		return image;
	}

	@Override
	public boolean deleteImageData(Long imageId) {
		FileData image = null;
		Criteria criteria = currentSession().createCriteria(FileData.class).add(Restrictions.eq("file_id", imageId));
		image = (FileData) criteria.uniqueResult();
		currentSession().delete(image);
		/* delete corresponding  thumbnail */
		ThumbFile thumb = null;
		criteria = currentSession().createCriteria(ThumbFile.class).add(Restrictions.eq("file_id", imageId));
		thumb = (ThumbFile) criteria.uniqueResult();
		currentSession().delete(thumb);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ThumbFile> loadImages(ThumbBatchReqDTO obj, Thumbnail size) {
		List<ThumbFile> imageList = null;
		ProjectionList projections = Projections.projectionList();
		switch (size) {
		case HIGH:
			projections.add(Projections.property("large").as("large")).add(Projections.property("file_id").as("file_id"));
			break;
		case MEDIUM:
			projections.add(Projections.property("medium").as("medium")).add(Projections.property("file_id").as("file_id"));
			break;
		case LOW:
			projections.add(Projections.property("small").as("small")).add(Projections.property("file_id").as("file_id"));
			break;
		default:
			projections.add(Projections.property("small").as("small")).add(Projections.property("file_id").as("file_id"));
			break;
		}
		Criteria criteria = currentSession().createCriteria(ThumbFile.class).add(Restrictions.in("file_id", obj.getImageIds())).setProjection(projections)
				.setResultTransformer(Transformers.aliasToBean(ThumbFile.class));
		criteria.setCacheable(true);
		imageList = criteria.list();
		return imageList;
	}

	@Override
	public ThumbFile loadThumbnail(Long photoId) {
		ThumbFile imageList = null;
		@SuppressWarnings("unused")
		ProjectionList projections = Projections.projectionList();
		Criteria criteria = currentSession().createCriteria(ThumbFile.class).add(Restrictions.eq("file_id", photoId));
		criteria.setCacheable(true);
		imageList = (ThumbFile) criteria.uniqueResult();
		return imageList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getPhotoIdList(String albumName){
		List<Long> photoIdList = null;		
		Criteria criteria = currentSession().createCriteria(FileData.class).setProjection(Projections.property("file_id")).add(Restrictions.eq("albumName", albumName));				
		photoIdList = criteria.list();
		return photoIdList;
	}
	
	@Override
	public boolean deleteAllImagesOfIds(List<Long> fileIds) {
		boolean flag = false;
		
		try {			
			Session session  = currentSession();			
			SQLQuery fileQuery = session.createSQLQuery("delete from file_data where file_id in (:fileIds)");			
			fileQuery.setParameterList("fileIds", fileIds);			
			int rows = fileQuery.executeUpdate();
			System.out.println("rows deleted from FILE_DATA : "+rows);				
			SQLQuery thumbQuery = currentSession().createSQLQuery("delete from thumb_file where file_id in (:fileIds)");
			thumbQuery.setParameterList("fileIds", fileIds);
			rows = thumbQuery.executeUpdate();			
			System.out.println("rows deleted from THUMB_FILE : "+rows);
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}
