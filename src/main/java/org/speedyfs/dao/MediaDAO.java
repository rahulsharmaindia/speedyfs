package org.speedyfs.dao;

import java.util.List;

import org.speedyfs.dto.ThumbBatchReqDTO;
import org.speedyfs.enums.Thumbnail;
import org.speedyfs.model.FileData;
import org.speedyfs.model.ThumbFile;

public interface MediaDAO extends GenericDao<FileData, String> {
	
	/**
	 * Method to save media file details.
	 * 
	 * @param vo
	 *            {@link FileData}
	 */
	void saveImageData(FileData vo);

	/**
	 * Method to update media file details in the DB.
	 * 
	 * @param obj
	 *            {@link FileData}
	 */
	void updateImageData(FileData obj);

	/**
	 * Method to fetch all the media files details against list of file_ids
	 * 
	 * @param thumbBatchReqDTO
	 *            {@link ThumbBatchReqDTO}
	 * @return
	 */	
	List<FileData> loadImageDataList(ThumbBatchReqDTO thumbBatchReqDTO);

	/**
	 * Method returns record against imageId from file_data
	 * 
	 * @param imageId
	 * @return
	 */
	FileData loadImageData(Long imageId);

	/**
	 * Method deletes image record against imageId from table file_data
	 * 
	 * @param imageId
	 * @return
	 */
	boolean deleteImageData(Long imageId);

	/**
	 * Method returns list of thumbnails from thumb_file
	 * 
	 * @param obj
	 * @param thumb
	 * @return
	 */
	List<ThumbFile> loadImages(ThumbBatchReqDTO obj, Thumbnail thumb);

	/**
	 * Method returns list of thumbnail records from table thumb_file
	 * 
	 * @param photoId
	 * @return
	 */
	ThumbFile loadThumbnail(Long photoId);
	
	/**
	 *  Method to fetch list of images from album.
	 * @param albumName
	 * @return
	 */
	List<Long> getPhotoIdList(String albumName);
	
	/**
	 * Method deletes all {@link FileData} and {@link ThumbFile} entries
	 * whose file_id match in the list.
	 * 
	 * @param photoIds
	 * @return
	 */
	boolean deleteAllImagesOfIds(List<Long> photoIds);
}
