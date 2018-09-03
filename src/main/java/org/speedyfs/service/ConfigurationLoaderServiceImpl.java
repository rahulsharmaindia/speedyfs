package org.speedyfs.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import org.speedyfs.dao.ConfigurationLoaderDAO;

@Service
public class ConfigurationLoaderServiceImpl implements ConfigurationLoaderService {

	@Autowired
	ConfigurationLoaderDAO dao;

	@Override
	public Map<String, String> loadConfiguration() throws DataRetrievalFailureException {

		return dao.loadConfiguration();
	}
}
