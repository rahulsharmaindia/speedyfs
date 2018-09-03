package org.speedyfs.dto;

public class AlbumDTO {
	private String directoryName;
	private Integer directoryId;
	private Integer access = 0;
	public Integer getDirectoryId() {
		return directoryId;
	}
	public void setDirectoryId(Integer directoryId) {
		this.directoryId = directoryId;
	}	
	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	public Integer getAccess() {
		return access;
	}
	public void setAccess(Integer access) {
		this.access = access;
	}
}
