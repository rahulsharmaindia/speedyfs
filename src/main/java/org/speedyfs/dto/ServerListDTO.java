package org.speedyfs.dto;

import java.util.List;

import org.speedyfs.model.OtherServer;

public class ServerListDTO {
	private List<OtherServer> servers;
	private OtherServer webServer;

	public List<OtherServer> getServers() {
		return servers;
	}

	public void setServers(List<OtherServer> servers) {
		this.servers = servers;
	}
	
	public OtherServer getWebServer() {
		return webServer;
	}

	public void setWebServer(OtherServer webServer) {
		this.webServer = webServer;
	}
}
