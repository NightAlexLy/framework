package org.luisyang.framework.service;

/**
 * 服务工厂
 *
 * @author LuisYang
 */
public abstract interface ServiceFactory<S extends Service> {
	
	/**
	 * 获得服务资源实例
	 * @param resource
	 * @return
	 */
	public abstract S newInstance(ServiceResource resource);
}
