package edu.buaa.rse.dotx.backend.util;

import javax.servlet.ServletContext;

public class ServletUtil {
	private static ServletContext servletContext;

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		ServletUtil.servletContext = servletContext;
	}
	
	public static String getConfigDir(){
		return getServletContext().getRealPath("/WEB-INF/config");
	}
}
