package org.speedyfs.dao;

import java.util.List;

import org.speedyfs.model.Device;

public interface DeviceDAO extends GenericDao<Device, String> {
	void saveDevice(Device vo);

	@SuppressWarnings("rawtypes")
	List fetchDevice(String remoteAddr);

	void updateDevice(Device vo);
}
