package org.speedyfs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Subselect;

/**
 * This Entity is used to fetch records related to configuration. Two portlets
 * "PhotoConfiguration Portlet” and First-->"LDAP Configuration" portlet are
 * created in liferay for this.
 * 
 * @author gauri.shukla
 *
 */
@Entity
@Table(name = "photoapplication_photoappconfiguration")
@Subselect("select * from photoapplication_photoappconfiguration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "speedyfs")
public class PhotoConfiguration {
	
	@Id
	String configName;
	@Column
	String value1;
	@Column
	String value2;
	@Column
	String value3;
	@Column
	String value4;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}



}
