package org.speedyfs.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.speedyfs.service.ConfigurationLoaderService;

/**
 * <p>
 * Context Listener class executed during the application context initialization.
 * @author rahul.sharma3
 *
 */
@Component
public class ContextListener {

	/**
	 * HttpServletContext object of application
	 */
	@Autowired
	ServletContext servletContext;
	/**
	 * <p>
	 * {@link ConfigurationLoaderService} reference to save data into database
	 */
	@Autowired
	ConfigurationLoaderService service;

	@Value("${root_path}")
	private String root_path;
	@Value("${ldap_url}")
	private String ldap_url;
	@Value("${ldap_username}")
	private String userName;
	@Value("${ldap_password}")
	private String password;
	@Value("${server_name}")
	private String server_name;

	/**
	 * Handles the context create and refresh events
	 * @param event
	 *            ContextRefreshedEvent
	 */
	@EventListener
	public void handleContextRefreshEvent(ContextRefreshedEvent event) {
		Map<String, String> configs = null;
		try {
			configs = service.loadConfiguration();
			setConfiguration(configs);
		} catch (Exception e) {
			System.out.println("Unable to load configuration data from database to context" + e.getMessage());
			System.out.println("loading configuration data from property file");
			configs = new HashMap<>();
			configs.put("root_path", root_path);
			configs.put("server_name", server_name);
			configs.put("url", ldap_url);
			configs.put("username", userName);
			configs.put("password", password);
			setConfiguration(configs);
		}
	}

	private void setConfiguration(Map<String, String> configs) {
		servletContext.setAttribute("rootPath", configs.get("root_path"));
		servletContext.setAttribute("serverName", configs.get("server_name"));
		servletContext.setAttribute("url", configs.get("url"));
		servletContext.setAttribute("username", configs.get("username"));
		servletContext.setAttribute("password", configs.get("password"));
	}

}
