package org.speedyfs.dto;

import java.util.List;

import org.speedyfs.model.FileData;

public class ImageListDTO {
	private List<FileData> images;
	private String path;
	private String lastModified;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<FileData> getImageFile() {
		return images;
	}

	public void setImageFile(List<FileData> images) {
		this.images = images;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
}
