package org.speedyfs.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * <p>
 * Custom Multipart resolver to support PUT Method
 * 
 * @author rahul.sharma3
 *
 */
public class LocalCommonMultipartResolver extends CommonsMultipartResolver {
	private static final String POST_METHOD = "POST";
	private static final String PUT_METHOD = "PUT";

	@Override
	public boolean isMultipart(HttpServletRequest request) {
		boolean isMultipartRequest = false;
		if (request != null) {
			if (POST_METHOD.equalsIgnoreCase(request.getMethod()) || PUT_METHOD.equalsIgnoreCase(request.getMethod())) {
				isMultipartRequest = FileUploadBase.isMultipartContent(new ServletRequestContext(request));
			}
		}
		return isMultipartRequest;
	}
}
