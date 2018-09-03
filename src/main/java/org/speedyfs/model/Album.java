package org.speedyfs.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "album_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "speedyfs")
public class Album implements Serializable {
	private static final long serialVersionUID = 5071959104002964521L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "album_id")
	private Integer albumId;
	@Column(name = "album_name")
	private String albumName;
	@JsonIgnore
	@Column(name = "album_path")
	private String albumPath;
	@CreationTimestamp
	@Column(name = "creation_date")
	private Timestamp createdOn;
	@Column(name = "created_by")
	private String createdBy;
	@Transient
	private String sharedWith;
	@Column(name = "access_")
	private Integer access = 0;

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumPath() {
		return albumPath;
	}

	public void setAlbumPath(String albumPath) {
		this.albumPath = albumPath;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getAccess() {
		return access;
	}

	public void setAccess(Integer access) {
		this.access = access;
	}

	public String getSharedWith() {
		if (StringUtils.isBlank(sharedWith)) {
			sharedWith = "";
		}
		return sharedWith;
	}

	public void setSharedWith(String sharedWith) {
		if (StringUtils.isBlank(sharedWith)) {
			sharedWith = "";
		}
		this.sharedWith = sharedWith;
	}

}
