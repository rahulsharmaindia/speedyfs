package org.speedyfs.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "file_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "speedyfs")
public class FileData implements Serializable {
	private static final long serialVersionUID = 5020367798695319492L;
	@Id
	@GeneratedValue
	@Column(name = "file_id")
	private Long file_id;
	@Transient
	private String file_path;	
	@Column
	private String name;
	@Lob
	@Column
	private String comments;
	@Column
	private Long size;
	@Column
	private String albumName;
	@Column
	private Integer media_type;
	@Transient
	private Integer content_flags;	
	@UpdateTimestamp
	@Column
	private Timestamp lastModified;
	@Column(name = "creation_date")
	private String creation_date;
	@Column(name = "created_by")
	private String createdBy;

	public Long getFile_id() {
		return file_id;
	}

	public void setFile_id(Long file_id) {
		this.file_id = file_id;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public Integer getMedia_type() {
		return media_type;
	}

	public void setMedia_type(Integer media_type) {
		if (media_type != null)
			this.media_type = media_type;
		else
			this.media_type = getContent_flags();
		if (getContent_flags() == null) {
			setContent_flags(this.media_type);
		}
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getContent_flags() {
		return content_flags;
	}

	public void setContent_flags(Integer content_flags) {
		if (content_flags != null)
			this.content_flags = content_flags;
		else
			this.content_flags = getMedia_type();
		if(getMedia_type() == null) {
			setMedia_type(this.content_flags);
		}
	}

}
