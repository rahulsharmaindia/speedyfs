package org.speedyfs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import org.speedyfs.model.Device;

@Repository
public class DeviceDAOImpl extends HibernateDao<Device, String> implements DeviceDAO {
	@Override
	public void saveDevice(Device vo) {
		saveOrUpdate(vo);
	}

	@Override
	public void updateDevice(Device vo) {
		saveOrUpdate(vo);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List fetchDevice(String remoteAddr) {
		Criteria criteria = currentSession().createCriteria(Device.class);
		criteria.add(Restrictions.eq("publicIP", remoteAddr));
		criteria.setCacheable(true);
		List devices = criteria.list();
		return devices;
	}
}
