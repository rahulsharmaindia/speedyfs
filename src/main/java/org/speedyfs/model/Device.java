package org.speedyfs.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="devices")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "speedyfs")
public class Device implements Serializable {
	@Id
	@NotNull(message = "device.deviceID.NotNull")
	String deviceID;
	@Column
	String deviceName;
	@Column
	Date lastlogin;
	@Column
	String macAddress;
	@Column
	String publicIP;
	@Column
	String ethernetIP;
	@Column
	String wifiIP;
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Date getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getPublicIP() {
		return publicIP;
	}
	public void setPublicIP(String publicIP) {
		this.publicIP = publicIP;
	}
	public String getEthernetIP() {
		return ethernetIP;
	}
	public void setEthernetIP(String ethernetIP) {
		this.ethernetIP = ethernetIP;
	}
	public String getWifiIP() {
		return wifiIP;
	}
	public void setWifiIP(String wifiIP) {
		this.wifiIP = wifiIP;
	}
	
	
}
