package org.speedyfs.service;

import java.util.List;

import org.speedyfs.model.Device;

public interface DeviceService {

	void saveDevice(Device vo);

	void updateDevice(Device vo);

	List fetchDevice(String remoteAddr);
}
