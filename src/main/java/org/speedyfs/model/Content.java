/**
 * 
 */
package org.speedyfs.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author himan
 *
 */
public class Content {

	public Content() {
	}

	public Content(Integer directoryId, MultipartFile inputFile, FileData clientImageData, String albumName, String albumPath) {
		this.directoryId = directoryId;
		this.inputFile = inputFile;
		this.clientImageData = clientImageData;
		this.albumName = albumName;
		this.albumPath = albumPath;
	}

	private Integer directoryId;
	private MultipartFile inputFile;
	private FileData clientImageData;
	private String albumName;
	private String albumPath;
	

	/**
	 * @return the directoryId
	 */
	public Integer getDirectoryId() {
		return directoryId;
	}

	/**
	 * @param directoryId
	 *            the directoryId to set
	 */
	public void setDirectoryId(Integer directoryId) {
		this.directoryId = directoryId;
	}

	/**
	 * @return the inputFile
	 */
	public MultipartFile getInputFile() {
		return inputFile;
	}

	/**
	 * @param inputFile
	 *            the inputFile to set
	 */
	public void setInputFile(MultipartFile inputFile) {
		this.inputFile = inputFile;
	}

	/**
	 * @return the clientImageData
	 */
	public FileData getClientImageData() {
		return clientImageData;
	}

	/**
	 * @param clientImageData
	 *            the clientImageData to set
	 */
	public void setClientImageData(FileData clientImageData) {
		this.clientImageData = clientImageData;
	}

	/**
	 * @return the albumName
	 */
	public String getAlbumName() {
		return albumName;
	}

	/**
	 * @param albumName the albumName to set
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * @return the albumPath
	 */
	public String getAlbumPath() {
		return albumPath;
	}

	/**
	 * @param albumPath the albumPath to set
	 */
	public void setAlbumPath(String albumPath) {
		this.albumPath = albumPath;
	}

}
