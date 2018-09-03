package org.speedyfs.dao;

import java.util.List;

import org.speedyfs.model.FileData;

public interface TaskDao extends GenericDao<FileData, String> {
	void checkAndUpdateCreationDate();

	List<FileData> getNullCreationDateData();

	void updateData(List<FileData> imageList);
}
