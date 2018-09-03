package org.speedyfs.controller;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.speedyfs.utils.CommonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Authentication Interceptor responsible for authenticating user and password
 * from LDAP server. And log the status.
 * 
 * <p>
 * Logs the request params and header information befor processing the request.
 * <p>
 * Logs the response status.
 * 
 * @author rahul.sharma3
 *
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	ServletContext servletContext;
	@Value("${ldap.manager.dn}")
	private String dn;
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * <p>
	 * Interceptor method Called by Spring Framework before processing the
	 * request as configured in xml
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		logRequest(req);
		boolean flag = false;
		String userName = req.getHeader("user");
		String password = req.getHeader("pass");
		String isUnencrypted = req.getHeader("unencrypted");

		if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)
				&& validate(userName, password, isUnencrypted)) {
			res.setStatus(HttpStatus.OK.value());
			flag = true;
			log.debug("LDAP validation successful for User " + userName + " and password "
					+ password.replaceAll("[^&]*", "***"));
		} else {
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			log.error("Invalid LDAP credentials " + userName + " password " + password.replaceAll("[^&]*", "***"));
		}
		return flag;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		logResponse(response);
	}

	/**
	 * Validates the User with the LDAP Server.
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean validate(String userName, String password, String isUnencrypted) {
		log.info("LDAP validation started for user " + userName + " and pass " + password.replaceAll("[^&]*", "***"));
		if (StringUtils.isEmpty(isUnencrypted)) {
			password = new CommonUtility().decrypt(password);
		}		
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.PROVIDER_URL, servletContext.getAttribute("url"));// "ldap://localhost:10389"
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.SECURITY_PRINCIPAL, "cn=" + userName + "," + dn);
		properties.put(Context.SECURITY_CREDENTIALS, password);
		DirContext context = null;
		try {
			context = new InitialDirContext(properties);
			context.close();
		} catch (NamingException | RuntimeException e) {
			log.error("Problem connecting LDAP server with properties" + properties.toString(), e);
		}
		if (context == null)
			return false;
		else
			return true;
	}

	private void logRequest(HttpServletRequest req) {
		log.debug("-------------------Request Start--------------------");
		log.debug("REQUEST HEADER = USER: " + req.getHeader("user") + " & PASS: " + req.getHeader("pass").replaceAll("[^&]*", "***"));
		log.debug("REQUEST ENDPOINT : " + req.getRequestURI());
		log.debug("-------------------Request End----------------------");
	}

	private void logResponse(HttpServletResponse res) {
		log.debug("-------------------Response Start--------------------");
		log.debug("RESPONSE STATUS : " + res.getStatus());
		log.debug("-------------------Response End----------------------");
	}
}
