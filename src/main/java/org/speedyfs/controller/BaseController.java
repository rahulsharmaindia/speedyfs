package org.speedyfs.controller;

import java.io.File;

import javax.servlet.ServletContext;

import org.speedyfs.model.FileData;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * Base controller of all controllers in the application
 * @author rahul.sharma3
 *
 */
public class BaseController {
	@Autowired
	ServletContext servletContext;

	/**
	 * <p>
	 * To get the root path from context
	 * @return rootPath
	 */
	protected String getRootPath() {
		return (String) servletContext.getAttribute("rootPath");
	}

	/**
	 * <p>
	 * To get the absolute path to album
	 * @return rootPath
	 */
	protected String getAlbumPath(String albumName) {
		File file = new File(getRootPath() + File.separator + albumName);
		return file.getAbsolutePath();
	}
	
	/**
	 * <p>
	 * To get the absolute path to file.
	 * 
	 * @return file path.
	 */
	protected String getFilePath(FileData fileData) {
		File file = new File(getAlbumPath(fileData.getAlbumName()) + File.separator + fileData.getName());
		return file.getAbsolutePath();
	}
}
