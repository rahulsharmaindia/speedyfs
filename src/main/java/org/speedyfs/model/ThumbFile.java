package org.speedyfs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "thumb_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "speedyfs")
public class ThumbFile {
	@Id
	public Long file_id;
	@Lob
	@Column
	public byte[] small;
	@Lob
	@Column
	public byte[] medium;
	@Lob
	@Column
	public byte[] large;

	public Long getFile_id() {
		return file_id;
	}

	public void setFile_id(Long file_id) {
		this.file_id = file_id;
	}

	public byte[] getSmall() {
		return small;
	}

	public void setSmall(byte[] small) {
		this.small = small;
	}

	public byte[] getMedium() {
		return medium;
	}

	public void setMedium(byte[] medium) {
		this.medium = medium;
	}

	public byte[] getLarge() {
		return large;
	}

	public void setLarge(byte[] large) {
		this.large = large;
	}

}
