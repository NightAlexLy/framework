package org.luisyang.framework.resource.achieve;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.luisyang.framework.resource.InitParameterProvider;

public class ServletInitParameterProvider implements InitParameterProvider {
	protected final ServletContext servletContext;

	public ServletInitParameterProvider(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getInitParameter(String name) {
		return this.servletContext.getInitParameter(name);
	}

	public Enumeration<String> getInitParameterNames() {
		return this.servletContext.getInitParameterNames();
	}
}