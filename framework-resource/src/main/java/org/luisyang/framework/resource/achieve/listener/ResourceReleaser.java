package org.luisyang.framework.resource.achieve.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRegister;

@WebListener
public class ResourceReleaser extends ResourceRegister implements ServletContextListener {
	public void contextDestroyed(ServletContextEvent event) {
		ResourceProvider resourceProvider = getResourceProvider(event.getServletContext());
		if (resourceProvider != null) {
			resourceProvider.close();
		}
	}

	public void contextInitialized(ServletContextEvent event) {
	}
}
