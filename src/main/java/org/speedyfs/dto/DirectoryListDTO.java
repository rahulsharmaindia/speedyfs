package org.speedyfs.dto;

import java.util.List;

import org.speedyfs.model.Album;

public class DirectoryListDTO {
	private List<Album> dirNames;
	private String serverName;

	public List<Album> getDirNames() {
		return dirNames;
	}

	public void setDirNames(List<Album> dirNames) {
		this.dirNames = dirNames;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
