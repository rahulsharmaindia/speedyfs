package org.speedyfs.service;

import java.util.Map;

import org.hibernate.HibernateException;

import org.speedyfs.dto.ThumbBatchReqDTO;
import org.speedyfs.enums.Thumbnail;
import org.speedyfs.model.FileData;
import org.speedyfs.model.ThumbFile;

public interface MediaService {
	/**
	 * Service method to save media file details. 
	 * @param vo
	 */
	void saveImageData(FileData vo);

	/**
	 * Service Method to update media file details.
	 * @param obj
	 */
	void updateImageData(FileData obj);

	/**
	 * Service Method to fetch all the media files data.
	 * 
	 * @param imageId
	 * @return
	 */
	FileData loadImageData(Long imageId);

	/**
	 * Service method deletes image record against imageId from table file_data.
	 *  
	 * @param imageId
	 * @return
	 */
	boolean deleteImageData(Long imageId);

	/**
	 * Service method returns list of thumbnails from thumb_file
	 * 
	 * @param obj
	 * @param thumbnail
	 * @return
	 */
	Map<Long, byte[]> loadImages(ThumbBatchReqDTO obj, Thumbnail thumbnail);

	/**
	 * Service method to delete media file if the user is owner of album in which file exists.	 * 
	 * 
	 * @param imageId
	 * @param user
	 * @return
	 */
	boolean deleteImage(Long imageId, String user);

	/**
	 * Service method to save thumbnails data.
	 * 
	 * @param imageFile
	 * @throws HibernateException
	 */
	void saveThumbnails(ThumbFile imageFile) throws HibernateException;

	/**
	 * Service method to fetch thumbnail of requested size.
	 * 
	 * @param fileId
	 * @param size
	 * @return
	 */
	byte[] loadThumbnail(Long fileId, Thumbnail size);

	/**
	 * Service method to get content informations.
	 * 
	 * @param string
	 * @return
	 */
	FileData getContentComments(Long fileId);
}
