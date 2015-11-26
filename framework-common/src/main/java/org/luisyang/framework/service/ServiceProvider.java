package org.luisyang.framework.service;

import java.util.Set;

import org.luisyang.framework.http.session.Session;
import org.luisyang.framework.resource.Prompter;
import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.service.exception.ServiceNotFoundException;

/**
 * 服务供应商
 * 
 * @author LuisYang
 */
public abstract class ServiceProvider extends Resource implements AutoCloseable {
	
	/**
	 * 初始化
	 * @param resourceProvider
	 */
	public ServiceProvider(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	/**
	 * 创建服务会话
	 * @param session
	 * @return
	 */
	public abstract ServiceSession createServiceSession(Session session);

	/**
	 * 创建服务会话
	 * @param session
	 * @param prompter
	 * @return
	 */
	public abstract ServiceSession createServiceSession(Session session, Prompter prompter);

	/**
	 * 创建服务会话
	 * @return
	 */
	public abstract ServiceSession createServiceSession();

	/**
	 * 注册服务实现类
	 * @param set
	 */
	public abstract void registerServiceImplements(Set<Class<? extends AbstractService>> set);

	/**
	 * 注册服务工厂
	 * @param paramClass
	 */
	public abstract void registerServiceFactory(Class<? extends ServiceFactory<?>> paramClass);

	/**
	 * 注册服务工厂
	 * @param set
	 */
	public abstract void registerServiceFactories(Set<Class<? extends ServiceFactory<?>>> set);

	/**
	 * 获得服务
	 * @param paramClass
	 * @param resource
	 * @return
	 * @throws ServiceNotFoundException
	 */
	public abstract <S extends Service> S getService(Class<S> clazz, ServiceResource resource)
			throws ServiceNotFoundException;

	/**
	 * 唯一标识泪
	 */
	public final Class<? extends Resource> getIdentifiedType() {
		return ServiceProvider.class;
	}
}
