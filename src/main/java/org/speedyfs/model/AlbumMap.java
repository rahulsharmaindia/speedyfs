package org.speedyfs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "share_album")

public class AlbumMap implements Serializable {
	private static final long serialVersionUID = 5071259104002964521L;
	@EmbeddedId
	private AlbumMapId albumMapId;

	@Column(name = "album_name")
	private String albumName;

	public AlbumMapId getAlbumMapId() {
		return albumMapId;
	}

	public void setAlbumMapId(AlbumMapId albumMapId) {
		this.albumMapId = albumMapId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

}
