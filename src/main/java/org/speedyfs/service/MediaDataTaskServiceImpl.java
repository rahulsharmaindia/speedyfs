package org.speedyfs.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.speedyfs.dao.TaskDao;
import org.speedyfs.model.FileData;

@Service
public class MediaDataTaskServiceImpl implements MediaDataTaskService {
	@Autowired
	TaskDao taskDao;
	@Autowired
	ResourcePathService resourcePathService;
	private Log log = LogFactory.getLog(this.getClass());
	@Override
	public void updateNullCreationDateData() {
		List<FileData> imageList = taskDao.getNullCreationDateData();
		correctCreationDate(imageList);
		taskDao.updateData(imageList);
	}

	/**
	 * Method check and update file creation date.
	 * 
	 * @param imageList
	 */
	private void correctCreationDate(List<FileData> imageList) {
		for (FileData data : imageList) {
				checkAndUpdateExifData(data);

		}
	}

	/**
	 * Method updates file Creation date from EXIF information.
	 * 
	 * @param data
	 */
	private void checkAndUpdateExifData(FileData data) {
		log.info("Updating the Image Creation date from EXIF information");
		Path file;
		try {
			file = Paths.get(resourcePathService.getFilePath(data));
		BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
		data.setCreation_date(String.valueOf(attr.creationTime().toMillis()));
		} catch (InvalidPathException | IOException e) {
			log.error("Error while correcting the creation_date", e);
		}
	}

}
