package org.speedyfs.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.speedyfs.dao.DeviceDAO;
import org.speedyfs.model.Device;

@Service
public class DeviceServiceImpl implements DeviceService {
	@Autowired
	DeviceDAO deviceDAO;


	@Override
	public void saveDevice(Device vo) throws HibernateException {
		deviceDAO.saveDevice(vo);
	}

	@Override
	public void updateDevice(Device vo) throws HibernateException {
		deviceDAO.updateDevice(vo);
	}

	@Override
	public List fetchDevice(String remoteAddr) throws HibernateException {
		return deviceDAO.fetchDevice(remoteAddr);
	}
}
