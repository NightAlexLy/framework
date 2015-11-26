package org.luisyang.framework.resource;

import javax.servlet.ServletContext;

/**
 * 资源注册器
 * 
 * @author LuisYang
 */
public abstract class ResourceRegister {
	
	private static final String RESOURCE_PROVIDER_KEY = "LUISYANG_RESOURCE_PROVIDER";

	/**
	 * 初始化
	 * @param servletContext
	 * @param provider
	 */
	protected static final void initialize(ServletContext servletContext, ResourceProvider provider) {
		if ((servletContext == null) || (provider == null)) {
			return;
		}
		Object object = servletContext.getAttribute("LUISYANG_RESOURCE_PROVIDER");
		if (object == null) {
			servletContext.setAttribute("LUISYANG_RESOURCE_PROVIDER", provider);
		} else if (provider != object) {
			if ((object instanceof ResourceProvider)) {
				try {
					((ResourceProvider) object).close();
				} catch (Exception e) {
					provider.log(e);
				}
			} else {
				servletContext.setAttribute("LUISYANG_RESOURCE_PROVIDER", provider);
			}
		}
	}

	/**
	 * 获取数据供应商
	 * @param servletContext
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public static final ResourceProvider getResourceProvider(ServletContext servletContext)
			throws ResourceNotFoundException {
		if (servletContext == null) {
			throw new ResourceNotFoundException();
		}
		Object object = servletContext.getAttribute("LUISYANG_RESOURCE_PROVIDER");
		if (object == null) {
			throw new ResourceNotFoundException();
		}
		if ((object instanceof ResourceProvider)) {
			return (ResourceProvider) object;
		}
		throw new ResourceNotFoundException();
	}
}
