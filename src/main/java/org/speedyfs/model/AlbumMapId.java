package org.speedyfs.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AlbumMapId implements Serializable {
	private static final long serialVersionUID = 5071959104002462521L;
	@Column(name = "album_id")
	private Integer albumId;

	@Column(name = "shared_user")
	private String sharedUser;

	public AlbumMapId() {
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public String getSharedUser() {
		return sharedUser;
	}

	public void setSharedUser(String sharedUser) {
		this.sharedUser = sharedUser;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AlbumMapId))
			return false;
		AlbumMapId that = (AlbumMapId) o;
		return Objects.equals(getAlbumId(), that.getAlbumId()) && Objects.equals(getSharedUser(), that.getSharedUser());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAlbumId(), getSharedUser());
	}
}
