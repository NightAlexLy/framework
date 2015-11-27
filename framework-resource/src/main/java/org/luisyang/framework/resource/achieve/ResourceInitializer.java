package org.luisyang.framework.resource.achieve;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.servlet.http.HttpServlet;

import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.config.VariableTypeAnnotation;
import org.luisyang.framework.config.entity.ModuleBean;
import org.luisyang.framework.config.entity.VariableBean;
import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.http.entity.RightBean;
import org.luisyang.framework.http.servlet.Controller;
import org.luisyang.framework.http.servlet.Rewriter;
import org.luisyang.framework.http.servlet.annotation.Module;
import org.luisyang.framework.http.servlet.annotation.Right;
import org.luisyang.framework.http.session.SessionManager;
import org.luisyang.framework.resource.AchieveVersion;
import org.luisyang.framework.resource.InitParameterProvider;
import org.luisyang.framework.resource.NamedResource;
import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceAnnotation;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRegister;
import org.luisyang.framework.resource.ResourceRetention;
import org.luisyang.framework.resource.cycle.Shutdown;
import org.luisyang.framework.resource.cycle.Startup;
import org.luisyang.framework.service.AbstractService;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceProvider;
import org.luisyang.util.StringHelper;

@HandlesTypes({ ServiceFactory.class, Right.class, Module.class, SystemDefine.class, DataConnectionProvider.class,
		ConfigureProvider.class, SessionManager.class, ServiceProvider.class, Resource.class, NamedResource.class,
		Startup.class, Shutdown.class, HttpServlet.class, AbstractService.class, VariableTypeAnnotation.class })
public class ResourceInitializer extends ResourceRegister implements ServletContainerInitializer {
	protected final org.apache.log4j.Logger p2pLogger;

	public ResourceInitializer() {
		this.p2pLogger = org.apache.log4j.Logger.getLogger(getClass());
	}

	protected static boolean isDenied(ResourceRetention retention, ResourceRetention[] acceptRetentions) {
		boolean denied = true;
		for (ResourceRetention acceptRetention : acceptRetentions) {
			if (retention == acceptRetention) {
				denied = false;
				break;
			}
		}
		return denied;
	}

	protected static boolean isDenied(Class<?> clazz, ResourceRetention retention) {
		ResourceAnnotation annotation = (ResourceAnnotation) clazz.getAnnotation(ResourceAnnotation.class);
		if (annotation == null) {
			return false;
		}
		return isDenied(retention, annotation.value());
	}

	protected static int getVersion(Class<?> clazz) {
		AchieveVersion annotation = (AchieveVersion) clazz.getAnnotation(AchieveVersion.class);

		return annotation == null ? 0 : annotation.version();
	}

	public ResourceProvider initialize(Set<Class<?>> classes, String... arguments) {
		return initialize(classes, System.getProperty("user.dir"), new org.luisyang.framework.log.Logger() {
			public void log(String message) {
				ResourceInitializer.this.p2pLogger.info(message);
			}

			public void log(Throwable throwable) {
				ResourceInitializer.this.p2pLogger.error("ResourceInitializer.initialize", throwable);
			}

			public void error(String message, Throwable throwable) {
				ResourceInitializer.this.p2pLogger.error(message, throwable);
			}

			public void error(Throwable throwable) {
				ResourceInitializer.this.p2pLogger.error("ResourceInitializer.initialize", throwable);
			}

			public void info(String message) {
				ResourceInitializer.this.p2pLogger.info(message);
			}
		}, null, new ArgumentInitParameterProvider(arguments));
	}

	public ResourceProvider initialize(Set<Class<?>> classes, org.luisyang.framework.log.Logger logger,
			String... arguments) {
		return initialize(classes, System.getProperty("user.dir"), logger, null,
				new ArgumentInitParameterProvider(arguments));
	}

	private ResourceProvider initialize(Set<Class<?>> classes, String home, org.luisyang.framework.log.Logger logger,
			String contextURI, InitParameterProvider initParameterProvider) {
		if (classes == null) {
			this.p2pLogger.error("没有任何可用的资源存在.");
			return null;
		}
		Set<Class<? extends SystemDefine>> systemDefines = new LinkedHashSet(1);

		Map<String, VariableTypeAnnotation> variableTypeAnnotations = new LinkedHashMap(1);

		Map<String, List<Class<? extends Enum<?>>>> variables = new LinkedHashMap(1);

		Set<Class<? extends ConfigureProvider>> configureProviders = new LinkedHashSet(1);

		Set<Class<? extends DataConnectionProvider>> connectionProviders = new LinkedHashSet(1);

		Set<Class<? extends SessionManager>> sessionManagers = new LinkedHashSet(1);

		Set<Class<? extends ServiceProvider>> serviceProviders = new LinkedHashSet(1);

		Set<Class<? extends Resource>> otherResources = new LinkedHashSet(1);
		Set<Class<? extends ServiceFactory<?>>> serviceFactories = new HashSet();
		Set<Class<? extends AbstractService>> serviceImplements = new HashSet();
		Set<Class<? extends Startup>> startups = new LinkedHashSet();
		final Set<Class<? extends Shutdown>> shutdowns = new LinkedHashSet();
		for (Class<?> clazz : classes) {
			int modifier = clazz.getModifiers();
			VariableTypeAnnotation variableTypeAnnotation;
			if ((variableTypeAnnotation = (VariableTypeAnnotation) clazz
					.getAnnotation(VariableTypeAnnotation.class)) != null) {
				variableTypeAnnotations.put(variableTypeAnnotation.id(), variableTypeAnnotation);
				if ((VariableBean.class.isAssignableFrom(clazz)) && (clazz.isEnum())) {
					List<Class<? extends Enum<?>>> list = (List) variables.get(variableTypeAnnotation.id());
					if (list == null) {
						list = new ArrayList();
						variables.put(variableTypeAnnotation.id(), list);
					}
					list.add((Class<? extends Enum<?>>) clazz);
				}
			}
			if ((!Modifier.isAbstract(modifier)) && (!Modifier.isInterface(modifier))) {
				if (Startup.class.isAssignableFrom(clazz)) {
					startups.add((Class<? extends Startup>) clazz);
				}
				if (Shutdown.class.isAssignableFrom(clazz)) {
					shutdowns.add((Class<? extends Shutdown>) clazz);
				}
				if (ServiceFactory.class.isAssignableFrom(clazz)) {
					serviceFactories.add((Class<? extends ServiceFactory<?>>) clazz);
				} else if (AbstractService.class.isAssignableFrom(clazz)) {
					serviceImplements.add((Class<? extends AbstractService>) clazz);
				}
				if (SystemDefine.class.isAssignableFrom(clazz)) {
					systemDefines.add((Class<? extends SystemDefine>) clazz);
				} else if (ConfigureProvider.class.isAssignableFrom(clazz)) {
					configureProviders.add((Class<? extends ConfigureProvider>) clazz);
				} else if (DataConnectionProvider.class.isAssignableFrom(clazz)) {
					connectionProviders.add((Class<? extends DataConnectionProvider>) clazz);
				} else if (SessionManager.class.isAssignableFrom(clazz)) {
					sessionManagers.add((Class<? extends SessionManager>) clazz);
				} else if (ServiceProvider.class.isAssignableFrom(clazz)) {
					serviceProviders.add((Class<? extends ServiceProvider>) clazz);
				} else if (Resource.class.isAssignableFrom(clazz)) {
					otherResources.add((Class<? extends Resource>) clazz);
				}
			}
		}
		SystemDefine systemDefine = null;
		try {
			if (systemDefines.size() == 0) {
				systemDefine = null;
			} else {
				Iterator i$ = systemDefines.iterator();
				if (i$.hasNext()) {
					Class<? extends SystemDefine> s = (Class) i$.next();
					systemDefine = (SystemDefine) s.newInstance();
				}
			}
		} catch (Throwable t) {
			this.p2pLogger.error("ResourceInitializer.initialize", t);
		}
		if (systemDefine == null) {
			this.p2pLogger.error("找不到系统配置信息.");
			return null;
		}
		MappedResourceProvider resourceProvider = new MappedResourceProvider(home, logger, contextURI,
				initParameterProvider, systemDefine) {
			public synchronized void close() {
				if ((shutdowns != null) && (shutdowns.size() > 0)) {
					for (Class<? extends Shutdown> shutdown : shutdowns) {
						if ((shutdown != null) && (!shutdown.isInterface())
								&& (!Modifier.isAbstract(shutdown.getModifiers()))) {
							try {
								((Shutdown) shutdown.newInstance()).onShutdown(this);
							} catch (Throwable throwable) {
								ResourceInitializer.this.p2pLogger.error("ResourceInitializer.initialize", throwable);
							}
						}
					}
				}
				super.close();
			}

			public void error(String message, Throwable throwable) {
				ResourceInitializer.this.p2pLogger.error(message, throwable);
			}

			public void error(Throwable throwable) {
				ResourceInitializer.this.p2pLogger.error("ResourceInitializer.initialize", throwable);
			}

			public void info(String message) {
				ResourceInitializer.this.p2pLogger.info(message);
			}
		};
		resourceProvider.setVariableTypes(variableTypeAnnotations, variables);

		loadRights(classes, resourceProvider);
		ResourceRetention retention = resourceProvider.getResourceRetention();
		if (retention == null) {
			retention = ResourceRetention.DEVELOMENT;
		}
		loadCongifureProvider(configureProviders, resourceProvider, retention);

		loadDataConnectionProviders(connectionProviders, resourceProvider, retention);

		loadSessionManager(sessionManagers, resourceProvider, retention);

		loadServiceProvider(serviceProviders, resourceProvider, retention, serviceFactories, serviceImplements);

		loadOtherResources(otherResources, resourceProvider, retention);

		doStartup(resourceProvider, startups);
		return resourceProvider;
	}

	public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
		ResourceProvider resourceProvider = initialize(classes, servletContext.getRealPath("/"),
				new org.luisyang.framework.log.Logger() {
					public void log(String message) {
						ResourceInitializer.this.p2pLogger.debug(message);
					}

					public void log(Throwable throwable) {
						ResourceInitializer.this.p2pLogger.error("ResourceInitializer.onStartup", throwable);
					}

					public void error(String message, Throwable throwable) {
						ResourceInitializer.this.p2pLogger.error(message, throwable);
					}

					public void error(Throwable throwable) {
						ResourceInitializer.this.p2pLogger.error("ResourceInitializer.onStartup", throwable);
					}

					public void info(String message) {
						ResourceInitializer.this.p2pLogger.info(message);
					}
				}, servletContext.getContextPath(), new ServletInitParameterProvider(servletContext));
		if (resourceProvider == null) {
			return;
		}
		ResourceRegister.initialize(servletContext, resourceProvider);
		Rewriter rewriter = resourceProvider.getSystemDefine().getRewriter();
		Set<Class<? extends HttpServlet>> servlets = new HashSet();
		if (classes != null) {
			for (Class<?> clazz : classes) {
				if ((clazz != null) && (HttpServlet.class.isAssignableFrom(clazz)) && (!clazz.isInterface())
						&& (!Modifier.isAbstract(clazz.getModifiers()))) {
					Class<? extends HttpServlet> servlet = (Class<? extends HttpServlet>) clazz;
					if (rewriter.isAccepted(servlet)) {
						servlets.add(servlet);
					}
				}
			}
		}
		((Controller) resourceProvider.getResource(Controller.class)).initialize(servletContext, rewriter, servlets);
	}

	protected void loadRights(Set<Class<?>> classes, MappedResourceProvider resourceProvider) {
		Map<String, ModuleBeanImpl> modules = new TreeMap();
		ModuleBeanImpl nillModuleBean = new ModuleBeanImpl(null);
		Map<String, Module> moduleCache = new HashMap();
		for (Class<?> type : classes) {
			Right right = (Right) type.getAnnotation(Right.class);
			if (right != null) {
				Module module = null;

				String name = type.getPackage().getName();
				int lastIndex = name.length();
				while (lastIndex != -1) {
					module = (Module) moduleCache.get(name);
					if (module != null) {
						break;
					}
					Package pkg = Package.getPackage(name);
					if (pkg != null) {
						module = (Module) pkg.getAnnotation(Module.class);
						if (module != null) {
							moduleCache.put(name, module);
							break;
						}
					}
					lastIndex = name.lastIndexOf('.');
					if (lastIndex == -1) {
						break;
					}
					name = name.substring(0, lastIndex);
				}
				ModuleBeanImpl moduleBean;
				if ((module == null) || (StringHelper.isEmpty(module.id()))) {
					moduleBean = nillModuleBean;
				} else {
					moduleBean = (ModuleBeanImpl) modules.get(module.id());
					if (moduleBean == null) {
						moduleBean = new ModuleBeanImpl(module);
						modules.put(moduleBean.getId(), moduleBean);
					}
				}
				moduleBean.addRight(right, type.getAnnotation(Deprecated.class) != null);
			}
		}
		resourceProvider.setModuleBeans((ModuleBean[]) modules.values().toArray(new ModuleBean[modules.size()]));
	}

	protected ConfigureProvider loadCongifureProvider(Set<Class<? extends ConfigureProvider>> classes,
			MappedResourceProvider resourceProvider, ResourceRetention retention) {
		if ((classes == null) || (classes.size() == 0)) {
			return null;
		}
		Class<? extends ConfigureProvider> configureProviderClass = null;
		for (Class<? extends ConfigureProvider> clazz : classes) {
			if (!isDenied(clazz, retention)) {
				if (configureProviderClass == null) {
					configureProviderClass = clazz;
				} else if (getVersion(clazz) > getVersion(configureProviderClass)) {
					configureProviderClass = clazz;
				}
			}
		}
		if (configureProviderClass != null) {
			try {
				Constructor<? extends ConfigureProvider> constructor = configureProviderClass
						.getConstructor(new Class[] { ResourceProvider.class });

				ConfigureProvider resource = (ConfigureProvider) constructor
						.newInstance(new Object[] { resourceProvider });

				resourceProvider.registerResource(resource);
				return resource;
			} catch (Throwable e) {
				this.p2pLogger.error("ResourceInitializer.initialize", e);
			}
		}
		return null;
	}

	protected void loadDataConnectionProviders(Set<Class<? extends DataConnectionProvider>> classes,
			MappedResourceProvider resourceProvider, ResourceRetention retention) {
		if ((classes == null) || (classes.size() == 0)) {
			return;
		}
		for (Class<? extends DataConnectionProvider> clazz : classes) {
			if (!isDenied(clazz, retention)) {
				try {
					DataConnectionProvider provider = (DataConnectionProvider) clazz
							.getConstructor(
									new Class[] { InitParameterProvider.class, org.luisyang.framework.log.Logger.class })
							.newInstance(new Object[] { resourceProvider, resourceProvider });

					resourceProvider.registerConnectionProvider(provider);
				} catch (Throwable e) {
				}
			}
		}
	}

	protected void loadSessionManager(Set<Class<? extends SessionManager>> classes,
			MappedResourceProvider resourceProvider, ResourceRetention retention) {
		if ((classes == null) || (classes.size() == 0)) {
			return;
		}
		Class<? extends SessionManager> sessionManagerClass = null;
		for (Class<? extends SessionManager> clazz : classes) {
			if (!isDenied(clazz, retention)) {
				if (sessionManagerClass == null) {
					sessionManagerClass = clazz;
				} else if (getVersion(clazz) > getVersion(sessionManagerClass)) {
					sessionManagerClass = clazz;
				}
			}
		}
		if (sessionManagerClass != null) {
			try {
				Constructor<? extends SessionManager> constructor = sessionManagerClass
						.getConstructor(new Class[] { ResourceProvider.class });

				SessionManager resource = (SessionManager) constructor.newInstance(new Object[] { resourceProvider });

				resourceProvider.registerResource(resource);
			} catch (Throwable e) {
				resourceProvider.log(e);
			}
		}
	}

	protected void loadServiceProvider(Set<Class<? extends ServiceProvider>> classes,
			MappedResourceProvider resourceProvider, ResourceRetention retention,
			Set<Class<? extends ServiceFactory<?>>> serviceFactories,
			Set<Class<? extends AbstractService>> serviceImplements) {
		Class<? extends ServiceProvider> serviceProviderClass = null;
		for (Class<? extends ServiceProvider> clazz : classes) {
			if (!isDenied(clazz, retention)) {
				if (serviceProviderClass == null) {
					serviceProviderClass = clazz;
				} else if (getVersion(clazz) > getVersion(serviceProviderClass)) {
					serviceProviderClass = clazz;
				}
			}
		}
		ServiceProvider serviceProvider = null;
		if (serviceProviderClass != null) {
			try {
				Constructor<? extends ServiceProvider> constructor = serviceProviderClass
						.getConstructor(new Class[] { ResourceProvider.class });

				serviceProvider = (ServiceProvider) constructor.newInstance(new Object[] { resourceProvider });
				resourceProvider.registerResource(serviceProvider);
			} catch (Throwable e) {
				resourceProvider.log(e);
			}
		}
		if (serviceProvider != null) {
			if (serviceFactories != null) {
				serviceProvider.registerServiceFactories(serviceFactories);
			}
			if (serviceImplements != null) {
				serviceProvider.registerServiceImplements(serviceImplements);
			}
		}
	}

	protected void loadOtherResources(Set<Class<? extends Resource>> classes, MappedResourceProvider resourceProvider,
			ResourceRetention retention) {
		if ((classes == null) || (classes.size() == 0)) {
			return;
		}
		for (Class<? extends Resource> clazz : classes) {
			if (!isDenied(clazz, retention)) {
				try {
					Constructor<? extends Resource> constructor = clazz
							.getConstructor(new Class[] { ResourceProvider.class });

					Resource resource = (Resource) constructor.newInstance(new Object[] { resourceProvider });

					resourceProvider.registerResource(resource);
				} catch (Throwable e) {
					resourceProvider.log(e);
				}
			}
		}
	}

	protected void doStartup(MappedResourceProvider resourceProvider, Set<Class<? extends Startup>> startups) {
		if ((startups == null) || (startups.size() == 0)) {
			return;
		}
		for (Class<? extends Startup> startup : startups) {
			if ((!startup.isInterface()) && (!Modifier.isAbstract(startup.getModifiers()))) {
				try {
					((Startup) startup.newInstance()).onStartup(resourceProvider);
				} catch (InstantiationException | IllegalAccessException throwable) {
					resourceProvider.log(throwable);
				}
			}
		}
	}

	private class ModuleBeanImpl implements ModuleBean {
		private static final long serialVersionUID = 1L;
		final Map<String, RightBeanImpl> rights = new LinkedHashMap();
		protected Module module;

		public ModuleBeanImpl(Module module) {
			this.module = module;
		}

		private void addRight(Right right, boolean deprecated) {
			RightBeanImpl rightBean = (RightBeanImpl) this.rights.get(right.id());
			if (rightBean != null) {
				return;
			}
			this.rights.put(right.id(), new RightBeanImpl(right, deprecated));
		}

		public String getId() {
			return this.module == null ? "" : this.module.id();
		}

		public String getName() {
			return this.module == null ? "" : this.module.name();
		}

		public String getDescription() {
			return this.module == null ? "" : this.module.description();
		}

		public RightBean[] getRightBeans() {
			return (RightBean[]) this.rights.values().toArray(new RightBean[this.rights.size()]);
		}

		private class RightBeanImpl implements RightBean {
			private static final long serialVersionUID = 1L;
			boolean deprecated;
			Right right;

			public RightBeanImpl(Right right, boolean deprecated) {
				this.right = right;
				this.deprecated = deprecated;
			}

			public String getId() {
				return this.right.id();
			}

			public String getName() {
				return this.right.name();
			}

			public String getDescription() {
				return this.right.description();
			}

			public String getModuleId() {
				return ResourceInitializer.ModuleBeanImpl.this.getId();
			}

			public boolean isDeprecated() {
				return this.deprecated;
			}

			public boolean isMenu() {
				return this.right.isMenu();
			}
		}
	}
}