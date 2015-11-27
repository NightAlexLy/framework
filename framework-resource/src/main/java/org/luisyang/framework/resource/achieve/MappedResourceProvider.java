package org.luisyang.framework.resource.achieve;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.config.VariableTypeAnnotation;
import org.luisyang.framework.config.entity.ModuleBean;
import org.luisyang.framework.config.entity.VariableBean;
import org.luisyang.framework.config.entity.VariableType;
import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.http.entity.RightBean;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.AchieveVersion;
import org.luisyang.framework.resource.InitParameterProvider;
import org.luisyang.framework.resource.NamedResource;
import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRetention;
import org.luisyang.util.StringHelper;

abstract class MappedResourceProvider implements ResourceProvider {
	protected Map<Class<? extends Resource>, Resource> resources;
	protected Map<Class<? extends Resource>, Map<String, Resource>> namedResources;
	protected Map<Class<? extends DataConnectionProvider>, Map<String, DataConnectionProvider>> connectionProviders;
	private Map<String, ModuleBean> modules = new LinkedHashMap();
	private Map<String, RightBean> rights = new HashMap();
	private VariableType[] variableTypes = null;
	protected final String home;
	protected final Logger logger;
	protected final InitParameterProvider initParameterProvider;
	protected final ResourceRetention resourceRetention;
	protected final String contextPath;
	protected final String charset;
	protected final SystemDefine systemDefine;

	public MappedResourceProvider(String home, Logger logger, String contextPath,
			InitParameterProvider initParameterProvider, SystemDefine systemDefine) {
		this.home = home;
		this.logger = logger;
		this.systemDefine = systemDefine;
		this.resources = new HashMap();
		this.namedResources = new HashMap();
		this.connectionProviders = new HashMap();
		if ((StringHelper.isEmpty(contextPath)) || ("/".equals(contextPath))) {
			this.contextPath = "";
		} else {
			this.contextPath = contextPath;
		}
		this.initParameterProvider = initParameterProvider;
		if (initParameterProvider == null) {
			this.resourceRetention = ResourceRetention.DEVELOMENT;
			this.charset = "UTF-8";
			return;
		}
		String parameter = initParameterProvider.getInitParameter("retention");
		if (StringHelper.isEmpty(parameter)) {
			this.resourceRetention = ResourceRetention.DEVELOMENT;
		} else {
			ResourceRetention retention = ResourceRetention.valueOf(parameter);
			if (retention == null) {
				if ("prd".equalsIgnoreCase(parameter)) {
					this.resourceRetention = ResourceRetention.PRODUCTION;
				} else if ("test".equalsIgnoreCase(parameter)) {
					this.resourceRetention = ResourceRetention.PRE_PRODUCTION;
				} else {
					this.resourceRetention = ResourceRetention.DEVELOMENT;
				}
			} else {
				this.resourceRetention = retention;
			}
		}
		parameter = initParameterProvider.getInitParameter("charset");
		if (StringHelper.isEmpty(parameter)) {
			this.charset = "UTF-8";
		} else {
			try {
				Charset.forName(parameter);
			} catch (Throwable t) {
				parameter = "UTF-8";
			}
			this.charset = parameter;
		}
	}

	public synchronized void close() {
		for (Resource resource : this.resources.values()) {
			try {
				resource.close();
			} catch (Exception e) {
				log(e);
			}
		}
		this.resources.clear();
		for (Map<String, Resource> instances : this.namedResources.values()) {
			for (Resource resource : instances.values()) {
				try {
					resource.close();
				} catch (Exception e) {
					log(e);
				}
			}
		}
		this.namedResources.clear();
		for (Map<String, DataConnectionProvider> connectionProviderMap : this.connectionProviders.values()) {
			for (DataConnectionProvider connectionProvider : connectionProviderMap.values()) {
				try {
					connectionProvider.close();
				} catch (Exception e) {
					log(e);
				}
			}
		}
	}

	public <R extends Resource> R getResource(Class<R> resourceType) {
		R resource = (R) this.resources.get(resourceType);
		if (resource == null) {
			throw new ResourceNotFoundException(resourceType.getCanonicalName());
		}
		return resource;
	}

	public <R extends NamedResource> R getNamedResource(Class<R> resourceType, String name)
			throws ResourceNotFoundException {
		Map<String, Resource> instances = (Map) this.namedResources.get(resourceType);
		if (instances == null) {
			throw new ResourceNotFoundException();
		}
		R resource = (R) instances.get(name);
		if (resource == null) {
			throw new ResourceNotFoundException();
		}
		return resource;
	}

	protected static int getVersion(Class<?> clazz) {
		AchieveVersion annotation = (AchieveVersion) clazz.getAnnotation(AchieveVersion.class);

		return annotation == null ? 0 : annotation.version();
	}

	public synchronized <R extends Resource> void registerResource(R resource) {
		if (resource == null) {
			return;
		}
		Class<? extends Resource> type = resource.getIdentifiedType();
		if ((resource instanceof NamedResource)) {
			Map<String, Resource> instances = (Map) this.namedResources.get(type);
			if (instances == null) {
				instances = new HashMap();
				this.namedResources.put(type, instances);
			}
			NamedResource namedResource = (NamedResource) resource;
			Resource oldResource = (Resource) instances.get(namedResource.getName());
			if (oldResource == null) {
				instances.put(namedResource.getName(), namedResource);
			} else {
				int oldVersion = getVersion(oldResource.getClass());
				int newVersion = getVersion(resource.getClass());
				if (newVersion > oldVersion) {
					instances.put(namedResource.getName(), namedResource);
					if ((oldResource instanceof AutoCloseable)) {
						try {
							oldResource.close();
						} catch (Exception e) {
							log(e);
						}
					}
				} else if ((namedResource instanceof AutoCloseable)) {
					try {
						namedResource.close();
					} catch (Exception e) {
						log(e);
					}
				}
			}
		} else {
			Resource oldResource = (Resource) this.resources.get(type);
			if (oldResource == null) {
				this.resources.put(type, resource);
			} else {
				int oldVersion = getVersion(oldResource.getClass());
				int newVersion = getVersion(resource.getClass());
				if (newVersion > oldVersion) {
					this.resources.put(type, resource);
					if ((oldResource instanceof AutoCloseable)) {
						try {
							oldResource.close();
						} catch (Exception e) {
							log(e);
						}
					}
				} else if ((resource instanceof AutoCloseable)) {
					try {
						resource.close();
					} catch (Exception e) {
						log(e);
					}
				}
			}
		}
	}

	public void log(Throwable throwable) {
		this.logger.log(throwable);
	}

	public void log(String message) {
		this.logger.log(message);
	}

	public <C extends DataConnectionProvider> C getDataConnectionProvider(Class<C> providerType, String name)
			throws ResourceNotFoundException {
		Map<String, DataConnectionProvider> instances = (Map) this.connectionProviders.get(providerType);
		if (instances == null) {
			throw new ResourceNotFoundException();
		}
		C resource = (C) instances.get(name);
		if (resource == null) {
			throw new ResourceNotFoundException();
		}
		return resource;
	}

	public <C extends DataConnectionProvider> void registerConnectionProvider(C provider) {
		if (provider == null) {
			return;
		}
		Class<? extends DataConnectionProvider> type = provider.getIdentifiedType();

		Map<String, DataConnectionProvider> instances = (Map) this.connectionProviders.get(type);
		if (instances == null) {
			instances = new HashMap();
			this.connectionProviders.put(type, instances);
		}
		DataConnectionProvider oldResource = (DataConnectionProvider) instances.put(provider.getName(), provider);
		if ((oldResource != null) && (oldResource != provider)) {
			try {
				oldResource.close();
			} catch (Exception e) {
				log(e);
			}
		}
	}

	public ModuleBean[] getModuleBeans() {
		if (this.modules.size() == 0) {
			return new ModuleBean[0];
		}
		return (ModuleBean[]) this.modules.values().toArray(new ModuleBean[this.modules.size()]);
	}

	public ModuleBean getModuleBean(String moduleId) {
		return (ModuleBean) this.modules.get(moduleId);
	}

	public RightBean getRightBean(String rightId) {
		return (RightBean) this.rights.get(rightId);
	}

	public String getHome() {
		return this.home;
	}

	public String getInitParameter(String name) {
		return this.initParameterProvider == null ? null : this.initParameterProvider.getInitParameter(name);
	}

	public Enumeration<String> getInitParameterNames() {
		return this.initParameterProvider == null ? null : this.initParameterProvider.getInitParameterNames();
	}

	public ResourceRetention getResourceRetention() {
		return this.resourceRetention;
	}

	public String getContextPath() {
		return this.contextPath;
	}

	public String getCharset() {
		return this.charset;
	}

	public SystemDefine getSystemDefine() {
		return this.systemDefine;
	}

	void setModuleBeans(ModuleBean[] moduleBeans) {
		if (moduleBeans == null) {
			return;
		}
		for (ModuleBean moduleBean : moduleBeans) {
			this.modules.put(moduleBean.getId(), moduleBean);
			for (RightBean rightBean : moduleBean.getRightBeans()) {
				this.rights.put(rightBean.getId(), rightBean);
			}
		}
	}

	void setVariableTypes(Map<String, VariableTypeAnnotation> variableTypeAnnotations,
			final Map<String, List<Class<? extends Enum<?>>>> variables) {
		if ((variableTypeAnnotations == null) || (variableTypeAnnotations.size() == 0)) {
			return;
		}
		ArrayList<VariableType> values = new ArrayList();
		for (final VariableTypeAnnotation variableTypeAnnotation : variableTypeAnnotations.values()) {
			if (variableTypeAnnotation != null) {
				values.add(new VariableType() {
					VariableBean[] variableBeans = null;

					public String getName() {
						return variableTypeAnnotation.name();
					}

					public String getId() {
						return variableTypeAnnotation.id();
					}

					public VariableBean[] getVariableBeans() {
						return this.variableBeans == null ? null
								: (VariableBean[]) Arrays.copyOf(this.variableBeans, this.variableBeans.length);
					}
				});
			}
		}
		this.variableTypes = ((VariableType[]) values.toArray(new VariableType[values.size()]));
	}

	public VariableType[] getVariableTypes() {
		return this.variableTypes == null ? null
				: (VariableType[]) Arrays.copyOf(this.variableTypes, this.variableTypes.length);
	}
}