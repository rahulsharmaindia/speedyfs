package org.speedyfs.service;

import org.speedyfs.model.FileData;

public interface ResourcePathService {
	/**
	 * <p>
	 * To get the root path from context
	 * 
	 * @return rootPath
	 */
	String getRootPath();

	/**
	 * <p>
	 * To get the absolute path to album
	 * 
	 * @return rootPath
	 */
	String getAlbumPath(String albumName);

	/**
	 * <p>
	 * To get the absolute path to file.
	 * 
	 * @return file path.
	 */

	String getFilePath(FileData fileData);

}
