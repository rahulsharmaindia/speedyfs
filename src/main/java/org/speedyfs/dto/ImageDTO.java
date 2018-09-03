package org.speedyfs.dto;

public class ImageDTO {
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Integer getMedia_type() {
		return media_type;
	}

	public void setMedia_type(Integer media_type) {
		this.media_type = media_type;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public Integer getContent_flags() {
		return content_flags;
	}

	public void setContent_flags(Integer content_flags) {
		this.content_flags = content_flags;
	}


	private String fileId;
	private String fileName;
	private String lastModified;
	private String creation_date;
	private Long size;
	private Integer media_type;
	private Integer content_flags;
}
