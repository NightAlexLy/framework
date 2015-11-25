package org.luisyang.framework.resource;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.config.entity.ModuleBean;
import org.luisyang.framework.config.entity.VariableType;
import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.log.Logger;

public abstract interface ResourceProvider extends Logger, InitParameterProvider {
	
	
	public abstract <C extends DataConnectionProvider> C getDataConnectionProvider(Class<C> paramClass,
			String paramString) throws ResourceNotFoundException;

	public abstract <R extends Resource> R getResource(Class<R> paramClass) throws ResourceNotFoundException;

	public abstract <R extends NamedResource> R getNamedResource(Class<R> paramClass, String paramString)
			throws ResourceNotFoundException;

	public abstract void close();

	public abstract ModuleBean[] getModuleBeans();

	public abstract ModuleBean getModuleBean(String paramString);

	//public abstract RightBean getRightBean(String paramString);

	public abstract String getHome();

	//public abstract ResourceRetention getResourceRetention();

	public abstract String getContextPath();

	public abstract String getCharset();

	public abstract SystemDefine getSystemDefine();

	public abstract VariableType[] getVariableTypes();
}
