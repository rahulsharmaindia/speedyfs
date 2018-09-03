package org.speedyfs.dao;

import java.util.Map;

public interface ConfigurationLoaderDAO extends GenericDao<String, String> {
	Map<String, String> loadConfiguration();
}
