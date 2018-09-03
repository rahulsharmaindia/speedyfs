package org.speedyfs.service;

import java.io.File;

import javax.servlet.ServletContext;

import org.speedyfs.model.FileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourcePathServiceImpl implements ResourcePathService {
	@Autowired
	ServletContext servletContext;
	
	@Override
	public String getRootPath() {
		return (String) servletContext.getAttribute("rootPath");
	}

	@Override
	public String getAlbumPath(String albumName) {
		File file = new File(getRootPath() + File.separator + albumName);
		return file.getAbsolutePath();
	}
	
	@Override
	public String getFilePath(FileData fileData) {
		File file = new File(getAlbumPath(fileData.getAlbumName()) + File.separator + fileData.getName());
		return file.getAbsolutePath();
	}
}
