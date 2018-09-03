package org.speedyfs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import org.speedyfs.constants.AppConstants;
import org.speedyfs.model.PhotoConfiguration;

@Repository
public class ConfigurationLoaderDAOImpl extends HibernateDao<String, String> implements ConfigurationLoaderDAO {

	@Override
	public Map<String, String> loadConfiguration() {
		Map<String, String> map = new HashMap<>();
		String hql;
		Query query;

		hql = "from PhotoConfiguration";
		query = currentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<PhotoConfiguration> list = query.list();
		if (list != null && !list.isEmpty()) {
			for (PhotoConfiguration photoConfig : list) {
				if (photoConfig != null && photoConfig.getConfigName().equals(AppConstants.PHOTO_CONFIGURATION)) {
					map.put("root_path", photoConfig.getValue1());
					map.put("server_name", photoConfig.getValue2());
				} else if (photoConfig != null && photoConfig.getConfigName().equals(AppConstants.LDAP_CONFIGURATION)) {
					map.put("url", photoConfig.getValue1());
					map.put("username", photoConfig.getValue2());
					map.put("password", photoConfig.getValue3());
				}
			}
		}else{
			throw new DataRetrievalFailureException("Configuration Data is missing in database.");
		}
		return map;

	}

}
