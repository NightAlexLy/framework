package org.luisyang.framework.resource;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.config.entity.ModuleBean;
import org.luisyang.framework.config.entity.VariableType;
import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.http.entity.RightBean;
import org.luisyang.framework.log.Logger;

/**
 * 资源供应商
 *
 * @author LuisYang
 */
public abstract interface ResourceProvider extends Logger, InitParameterProvider {
	
	/**
	 * 获得指定资源供应商
	 * @param providerType  资源类型
	 * @param name  资源名
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public abstract <C extends DataConnectionProvider> C getDataConnectionProvider(Class<C> providerType,
			String name) throws ResourceNotFoundException;

	/**
	 * 获得资源
	 * @param resourceType 资源类型 
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public abstract <R extends Resource> R getResource(Class<R> resourceType) throws ResourceNotFoundException;

	/**
	 * 获得指定名称的资源
	 * @param resourceType 资源类型
	 * @param name 名称
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public abstract <R extends NamedResource> R getNamedResource(Class<R> resourceType, String name)
			throws ResourceNotFoundException;

	/**
	 * 关闭
	 */
	public abstract void close();

	/**
	 * 获得所有基本模型
	 * @return
	 */
	public abstract ModuleBean[] getModuleBeans();

	/**
	 * 通过模型ID获得指定模型
	 * @param moduleId
	 * @return
	 */
	public abstract ModuleBean getModuleBean(String moduleId);

	
	public abstract RightBean getRightBean(String moduleId);

	/**
	 * 资源目录 <br/>
	 * 	<p>user.dir</p>
	 * @return
	 */
	public abstract String getHome();

	/**
	 * 获得资源保留级别
	 * @return
	 */
	public abstract ResourceRetention getResourceRetention();

	/**
	 * 上下文地址
	 * @return
	 */
	public abstract String getContextPath();

	/**
	 * 字符编码UTF-8 
	 * @return
	 */
	public abstract String getCharset();

	/**
	 * 获得系统定义
	 * @return
	 */
	public abstract SystemDefine getSystemDefine();

	/**
	 * 获得所有变量类型
	 * @return
	 */
	public abstract VariableType[] getVariableTypes();
}
