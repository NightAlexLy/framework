package org.luisyang.framework.service.achieve;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.luisyang.framework.config.SystemDefine;
import org.luisyang.framework.config.entity.ModuleBean;
import org.luisyang.framework.config.entity.VariableType;
import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.data.sql.SQLConnection;
import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.http.entity.RightBean;
import org.luisyang.framework.http.servlet.Controller;
import org.luisyang.framework.http.session.Session;
import org.luisyang.framework.resource.AchieveVersion;
import org.luisyang.framework.resource.NamedResource;
import org.luisyang.framework.resource.PromptLevel;
import org.luisyang.framework.resource.Prompter;
import org.luisyang.framework.resource.Resource;
import org.luisyang.framework.resource.ResourceAnnotation;
import org.luisyang.framework.resource.ResourceInvalidatedException;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRetention;
import org.luisyang.framework.service.AbstractService;
import org.luisyang.framework.service.Service;
import org.luisyang.framework.service.ServiceFactory;
import org.luisyang.framework.service.ServiceProvider;
import org.luisyang.framework.service.ServiceResource;
import org.luisyang.framework.service.ServiceSession;
import org.luisyang.framework.service.achieve.proxy.SQLConnectionProviderProxy;
import org.luisyang.framework.service.achieve.proxy.SQLConnectionProxy;
import org.luisyang.framework.service.exception.ServiceNotFoundException;
import org.luisyang.util.StringHelper;

@ResourceAnnotation
public class SimpleServiceProvider extends ServiceProvider {
	protected final org.apache.log4j.Logger p2pLogger = org.apache.log4j.Logger.getLogger(getClass());
	protected static final Map<Class<?>, ParameterSetter> SETTERS = new HashMap();
	protected static final ParameterSetter INPUTSTREAM_SETTER = new ParameterSetter() {
		public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
			pstmt.setBlob(parameterIndex, (InputStream) parameter);
		}
	};
	protected static final ParameterSetter ENUM_SETTER = new ParameterSetter() {
		public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
			pstmt.setString(parameterIndex, ((Enum) parameter).name());
		}
	};
	protected static final ParameterSetter READER_SETTER = new ParameterSetter() {
		public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
			pstmt.setClob(parameterIndex, (Reader) parameter);
		}
	};
	protected static final ParameterSetter BYTES_SETTER = new ParameterSetter() {
		public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
			pstmt.setBytes(parameterIndex, (byte[]) parameter);
		}
	};

	protected static void register(ParameterSetter setter, Class<?>... types) {
		for (Class<?> type : types) {
			SETTERS.put(type, setter);
		}
	}

	static {
		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				if (StringHelper.isEmpty((String) parameter)) {
					pstmt.setNull(parameterIndex, 0);
				} else {
					pstmt.setString(parameterIndex, (String) parameter);
				}
			}
		}, new Class[] { String.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setInt(parameterIndex, ((Integer) parameter).intValue());
			}
		}, new Class[] { Integer.class, Integer.TYPE });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setLong(parameterIndex, ((Long) parameter).longValue());
			}
		}, new Class[] { Long.class, Long.TYPE });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setShort(parameterIndex, ((Short) parameter).shortValue());
			}
		}, new Class[] { Short.class, Short.TYPE });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setByte(parameterIndex, ((Byte) parameter).byteValue());
			}
		}, new Class[] { Byte.class, Byte.TYPE });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setDouble(parameterIndex, ((Double) parameter).doubleValue());
			}
		}, new Class[] { Double.class, Double.TYPE });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setBoolean(parameterIndex, ((Boolean) parameter).booleanValue());
			}
		}, new Class[] { Boolean.class, Boolean.TYPE });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setFloat(parameterIndex, ((Float) parameter).floatValue());
			}
		}, new Class[] { Float.class, Float.TYPE });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setTime(parameterIndex, (Time) parameter);
			}
		}, new Class[] { Time.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setTimestamp(parameterIndex, (Timestamp) parameter);
			}
		}, new Class[] { Timestamp.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setDate(parameterIndex, (Date) parameter);
			}
		}, new Class[] { Date.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setURL(parameterIndex, (URL) parameter);
			}
		}, new Class[] { URL.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setBigDecimal(parameterIndex, (BigDecimal) parameter);
			}
		}, new Class[] { BigDecimal.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setArray(parameterIndex, (Array) parameter);
			}
		}, new Class[] { Array.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setRowId(parameterIndex, (RowId) parameter);
			}
		}, new Class[] { RowId.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setBlob(parameterIndex, (Blob) parameter);
			}
		}, new Class[] { Blob.class });

		register(new ParameterSetter() {
			public void set(PreparedStatement pstmt, int parameterIndex, Object parameter) throws SQLException {
				pstmt.setClob(parameterIndex, (Clob) parameter);
			}
		}, new Class[] { Clob.class });
	}

	protected static ParameterSetter getSetter(Class<?> type) {
		ParameterSetter setter = (ParameterSetter) SETTERS.get(type);
		if (setter == null) {
			if (type.isArray()) {
				if ((type.getComponentType() == Byte.TYPE) || (type.getComponentType() == Byte.class)) {
					setter = BYTES_SETTER;
				} else {
					throw new RuntimeException("");
				}
			} else if (Reader.class.isAssignableFrom(type)) {
				setter = READER_SETTER;
			} else if (InputStream.class.isAssignableFrom(type)) {
				setter = INPUTSTREAM_SETTER;
			} else if (Enum.class.isAssignableFrom(type)) {
				setter = ENUM_SETTER;
			}
		}
		return setter;
	}

	protected final Map<Type, ServiceFactory<? extends Service>> serviceFactories = new HashMap();
	protected final Map<Class<?>, Class<? extends AbstractService>> serviceImplements = new HashMap();

	public SimpleServiceProvider(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	private static Type getParameterizedType(Class<?> interfaceClass, Class<?> clazz) {
		Type type = clazz.getGenericSuperclass();
		if ((type instanceof ParameterizedType)) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			if (interfaceClass.isAssignableFrom((Class) parameterizedType.getRawType())) {
				return parameterizedType.getActualTypeArguments()[0];
			}
		} else {
			Type[] types = clazz.getGenericInterfaces();
			if (types != null) {
				for (Type t : types) {
					if ((t instanceof ParameterizedType)) {
						ParameterizedType parameterizedType = (ParameterizedType) t;
						if (interfaceClass.isAssignableFrom((Class) parameterizedType.getRawType())) {
							return parameterizedType.getActualTypeArguments()[0];
						}
					}
				}
			}
		}
		return null;
	}

	public ServiceSession createServiceSession() {
		return new SimpleServiceSession();
	}

	public ServiceSession createServiceSession(Session session) {
		return new SimpleServiceSession(session);
	}

	public ServiceSession createServiceSession(Session session, Prompter prompter) {
		return new SimpleServiceSession(session, prompter);
	}

	public class SimpleServiceSession implements ServiceSession {
		private ServiceResource serviceResource = null;
		private Map<Class<? extends Service>, Service> services = null;
		private boolean transactions = false;
		private Map<String, SQLConnectionProxy> sqlConnections = null;
		private Map<String, SQLConnectionProvider> connectionProviders;
		private transient boolean invalidated = false;
		private int level = -1;
		private Session session;
		private Prompter prompter;

		public SimpleServiceSession() {
			this.prompter = null;
			this.session = null;
		}

		public SimpleServiceSession(Session session) {
			this.session = session;
		}

		public SimpleServiceSession(Session session, Prompter prompter) {
			this.session = session;
			this.prompter = prompter;
		}

		public synchronized void openTransactions(Connection connection) throws SQLException {
			if (connection.getAutoCommit()) {
				connection.setAutoCommit(false);
				this.transactions = false;
			}
		}

		public void openTransactions(int level) throws Throwable {
			if (this.invalidated) {
				throw new ResourceInvalidatedException();
			}
			if (this.transactions) {
				return;
			}
			try {
				if (this.sqlConnections != null) {
					for (SQLConnection connection : this.sqlConnections.values()) {
						connection.setTransactionIsolation(level);
						connection.setAutoCommit(false);
					}
				}
				this.transactions = true;
				this.level = level;
			} catch (Throwable t) {
				if (this.sqlConnections != null) {
					for (SQLConnection connection : this.sqlConnections.values()) {
						connection.setAutoCommit(true);
					}
				}
			}
		}

		public synchronized boolean isTransactions() throws SQLException {
			if (this.invalidated) {
				throw new ResourceInvalidatedException();
			}
			return this.transactions;
		}

		public synchronized void commit(Connection connection) throws SQLException {
			if (!connection.getAutoCommit()) {
				connection.commit();
				connection.setAutoCommit(true);
				this.transactions = true;
			}
		}

		public synchronized void rollback(Connection connection) throws SQLException {
			if (!connection.getAutoCommit()) {
				connection.rollback();
				connection.setAutoCommit(true);
				this.transactions = true;
			}
		}

		protected <C extends DataConnectionProvider> C getDataConnectionProvider(Class<C> providerType, String name)
				throws ResourceNotFoundException {
			if (SQLConnectionProvider.class.isAssignableFrom(providerType)) {
				if (this.connectionProviders == null) {
					this.connectionProviders = new HashMap();
				}
				SQLConnectionProvider provider = (SQLConnectionProvider) this.connectionProviders.get(name);
				if (provider == null) {
					provider = new SQLConnectionProviderProxy(SimpleServiceProvider.this.resourceProvider,
							SimpleServiceProvider.this.resourceProvider,
							(SQLConnectionProvider) SimpleServiceProvider.this.resourceProvider
									.getDataConnectionProvider(providerType, name)) {
						public SQLConnection getConnection(String schema) throws SQLException {
							String providerName = getName();
							SQLConnectionProxy connection;
							if ((SimpleServiceProvider.SimpleServiceSession.this.sqlConnections == null)
									|| ((connection = (SQLConnectionProxy) SimpleServiceProvider.SimpleServiceSession.this.sqlConnections
											.get(providerName)) == null)) {
								connection = new SQLConnectionProxy(this.connectionProvider.getConnection(schema));
								if (SimpleServiceProvider.SimpleServiceSession.this.sqlConnections == null) {
									SimpleServiceProvider.SimpleServiceSession.this.sqlConnections = new LinkedHashMap();
								}
								SimpleServiceProvider.SimpleServiceSession.this.sqlConnections.put(providerName,
										connection);
								if (SimpleServiceProvider.SimpleServiceSession.this.transactions) {
									if (SimpleServiceProvider.SimpleServiceSession.this.level != -1) {
										connection.setTransactionIsolation(
												SimpleServiceProvider.SimpleServiceSession.this.level);
									}
									connection.setAutoCommit(false);
								}
							} else {
								connection.setSchema(schema);
							}
							return connection;
						}

						public SQLConnection getConnection() throws SQLException {
							String providerName = getName();
							SQLConnectionProxy connection;
							if ((SimpleServiceProvider.SimpleServiceSession.this.sqlConnections == null)
									|| ((connection = (SQLConnectionProxy) SimpleServiceProvider.SimpleServiceSession.this.sqlConnections
											.get(providerName)) == null)) {
								connection = new SQLConnectionProxy(this.connectionProvider.getConnection());
								if (SimpleServiceProvider.SimpleServiceSession.this.sqlConnections == null) {
									SimpleServiceProvider.SimpleServiceSession.this.sqlConnections = new LinkedHashMap();
								}
								SimpleServiceProvider.SimpleServiceSession.this.sqlConnections.put(providerName,
										connection);
								if (SimpleServiceProvider.SimpleServiceSession.this.transactions) {
									if (SimpleServiceProvider.SimpleServiceSession.this.level != -1) {
										connection.setTransactionIsolation(
												SimpleServiceProvider.SimpleServiceSession.this.level);
									}
									connection.setAutoCommit(false);
								}
							}
							return connection;
						}
					};
					this.connectionProviders.put(name, provider);
				}
				return (C) provider;
			}
			return SimpleServiceProvider.this.resourceProvider.getDataConnectionProvider(providerType, name);
		}

		public synchronized void close() {
			if (this.invalidated) {
				return;
			}
			this.invalidated = true;
			if (this.sqlConnections != null) {
				Iterator i$ = this.sqlConnections.values().iterator();
				for (;;) {
					if (i$.hasNext()) {
						SQLConnectionProxy connection = (SQLConnectionProxy) i$.next();
						try {
							if (!connection.getAutoCommit()) {
								connection.rollback();
								connection.setAutoCommit(true);
							}
							try {
								connection._release();
							} catch (SQLException e) {
								SimpleServiceProvider.this.resourceProvider.log(e);
							}
						} catch (SQLException e) {
							SimpleServiceProvider.this.resourceProvider.log(e);
						} finally {
							try {
								connection._release();
							} catch (SQLException e) {
								SimpleServiceProvider.this.resourceProvider.log(e);
							}
						}
					}
				}
			}
			this.serviceResource = null;
			if (this.services != null) {
				this.services.clear();
			}
		}

		protected final synchronized ServiceResource getServiceResource() {
			if (this.serviceResource == null) {
				this.serviceResource = new ServiceResource() {
					public Session getSession() throws ResourceInvalidatedException {
						return SimpleServiceProvider.SimpleServiceSession.this.getSession();
					}

					public void log(Throwable throwable) {
						SimpleServiceProvider.this.resourceProvider.log(throwable);
					}

					public void log(String message) {
						SimpleServiceProvider.this.resourceProvider.log(message);
					}

					public <R extends Resource> R getResource(Class<R> resourceType) throws ResourceNotFoundException {
						if (ServiceProvider.class.isAssignableFrom(resourceType)) {
							throw new ResourceNotFoundException("禁止获取服务供应商.");
						}
						return SimpleServiceProvider.this.resourceProvider.getResource(resourceType);
					}

					public <R extends NamedResource> R getNamedResource(Class<R> resourceType, String name)
							throws ResourceNotFoundException {
						return SimpleServiceProvider.this.resourceProvider.getNamedResource(resourceType, name);
					}

					public <S extends Service> S getService(Class<S> serviceInterface)
							throws ServiceNotFoundException, ResourceInvalidatedException {
						throw new RuntimeException("禁止业务实现内部调用其他业务接口.");
					}

					public void openTransactions(Connection connection) throws Throwable {
						SimpleServiceProvider.SimpleServiceSession.this.openTransactions(connection);
					}

					public void commit(Connection connection) throws Throwable {
						SimpleServiceProvider.SimpleServiceSession.this.commit(connection);
					}

					public void rollback(Connection connection) throws Throwable {
						SimpleServiceProvider.SimpleServiceSession.this.rollback(connection);
					}

					public boolean isTransactions() throws Throwable {
						return SimpleServiceProvider.SimpleServiceSession.this.isTransactions();
					}

					public void close() {
					}

					public <C extends DataConnectionProvider> C getDataConnectionProvider(Class<C> providerType,
							String name) throws ResourceNotFoundException {
						return SimpleServiceProvider.SimpleServiceSession.this.getDataConnectionProvider(providerType,
								name);
					}

					public ModuleBean[] getModuleBeans() {
						return SimpleServiceProvider.this.resourceProvider.getModuleBeans();
					}

					public ModuleBean getModuleBean(String moduleId) {
						return SimpleServiceProvider.this.resourceProvider.getModuleBean(moduleId);
					}

					public RightBean getRightBean(String rightId) {
						return SimpleServiceProvider.this.resourceProvider.getRightBean(rightId);
					}

					public void openTransactions(int level) throws Throwable {
						SimpleServiceProvider.SimpleServiceSession.this.openTransactions(level);
					}

					public void setParameters(PreparedStatement pstmt, Collection<Object> parameters)
							throws SQLException {
						if ((parameters == null) || (parameters.size() == 0)) {
							return;
						}
						int parameterIndex = 1;
						for (Object parameter : parameters) {
							if (parameter == null) {
								pstmt.setNull(parameterIndex++, 0);
							} else {
								SimpleServiceProvider.getSetter(parameter.getClass()).set(pstmt, parameterIndex++,
										parameter);
							}
						}
					}

					public void setParameters(PreparedStatement pstmt, Object... parameters) throws SQLException {
						if ((parameters == null) || (parameters.length == 0)) {
							return;
						}
						int parameterIndex = 1;
						for (Object parameter : parameters) {
							if (parameter == null) {
								pstmt.setNull(parameterIndex++, 0);
							} else {
								SimpleServiceProvider.getSetter(parameter.getClass()).set(pstmt, parameterIndex++,
										parameter);
							}
						}
					}

					public void prompt(PromptLevel level, String message) {
						SimpleServiceProvider.SimpleServiceSession.this.prompt(level, message);
					}

					public void clearPrompts(PromptLevel level) {
						SimpleServiceProvider.SimpleServiceSession.this.clearPrompts(level);
					}

					public void clearAllPrompts() {
						SimpleServiceProvider.SimpleServiceSession.this.clearAllPrompts();
					}

					public String getHome() {
						return SimpleServiceProvider.this.resourceProvider.getHome();
					}

					public String getInitParameter(String name) {
						return SimpleServiceProvider.this.resourceProvider.getInitParameter(name);
					}

					public Enumeration<String> getInitParameterNames() {
						return SimpleServiceProvider.this.resourceProvider.getInitParameterNames();
					}

					public ResourceRetention getResourceRetention() {
						return SimpleServiceProvider.this.resourceProvider.getResourceRetention();
					}

					public String getContextPath() {
						return SimpleServiceProvider.this.resourceProvider.getContextPath();
					}

					public String getCharset() {
						return SimpleServiceProvider.this.resourceProvider.getCharset();
					}

					public SystemDefine getSystemDefine() {
						return SimpleServiceProvider.this.resourceProvider.getSystemDefine();
					}

					public Controller getController() throws ResourceNotFoundException {
						return SimpleServiceProvider.SimpleServiceSession.this.getController();
					}

					public VariableType[] getVariableTypes() {
						return SimpleServiceProvider.this.resourceProvider.getVariableTypes();
					}

					public void error(String message, Throwable throwable) {
						SimpleServiceProvider.this.p2pLogger.error(message, throwable);
					}

					public void error(Throwable throwable) {
						SimpleServiceProvider.this.p2pLogger.error(throwable);
					}

					public void info(String message) {
						SimpleServiceProvider.this.p2pLogger.info(message);
					}
				};
			}
			return this.serviceResource;
		}

		public synchronized <S extends Service> S getService(Class<S> serviceInterface)
				throws ServiceNotFoundException {
			if (this.invalidated) {
				throw new ResourceInvalidatedException();
			}
			if ((serviceInterface == null) || (!serviceInterface.isInterface())) {
				throw new ServiceNotFoundException();
			}
			S service;
			if (this.services == null) {
				service = SimpleServiceProvider.this.getService(serviceInterface, getServiceResource());

				this.services = new HashMap();
				this.services.put(serviceInterface, service);
			} else {
				service = (S) this.services.get(serviceInterface);
				if (service == null) {
					service = SimpleServiceProvider.this.getService(serviceInterface, getServiceResource());

					this.services.put(serviceInterface, service);
				}
			}
			return service;
		}

		public Session getSession() {
			return this.session;
		}

		public void prompt(PromptLevel level, String message) {
			if (this.prompter != null) {
				this.prompter.prompt(level, message);
			}
		}

		public void clearPrompts(PromptLevel level) {
			if (this.prompter != null) {
				this.prompter.clearPrompts(level);
			}
		}

		public void clearAllPrompts() {
			if (this.prompter != null) {
				this.prompter.clearAllPrompts();
			}
		}

		public Controller getController() throws ResourceNotFoundException {
			return (Controller) SimpleServiceProvider.this.resourceProvider.getResource(Controller.class);
		}
	}

	public void registerServiceFactories(Set<Class<? extends ServiceFactory<?>>> serviceFactoryClasses) {
		if (this.serviceFactories == null) {
			return;
		}
		for (Class<? extends ServiceFactory<?>> serviceFactoryClass : serviceFactoryClasses) {
			Type serviceInterface = getParameterizedType(ServiceFactory.class, serviceFactoryClass);
			if (serviceInterface != null) {
				try {
					this.serviceFactories.put(serviceInterface, serviceFactoryClass.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
				}
			}
		}
	}

	private int getAchieveVersion(Class<?> clazz) {
		AchieveVersion achieveVersion = (AchieveVersion) clazz.getAnnotation(AchieveVersion.class);

		return achieveVersion == null ? 0 : achieveVersion.version();
	}

	public void registerServiceImplements(Set<Class<? extends AbstractService>> serviceImplements) {
		if (serviceImplements == null) {
			return;
		}
		for (Class<? extends AbstractService> serviceImplement : serviceImplements) {
			int modifier = serviceImplement.getModifiers();
			if ((!Modifier.isAbstract(modifier)) && (!Modifier.isInterface(modifier))) {
				Class<?>[] serviceInterfaces = serviceImplement.getInterfaces();
				if (serviceInterfaces != null) {
					for (Class<?> serviceInterface : serviceInterfaces) {
						if (Service.class.isAssignableFrom(serviceInterface)) {
							Class<? extends AbstractService> achieve = (Class) this.serviceImplements
									.get(serviceInterface);
							if (achieve == null) {
								this.serviceImplements.put(serviceInterface, serviceImplement);
							} else if (getAchieveVersion(serviceInterface) > getAchieveVersion(achieve)) {
								this.serviceImplements.put(serviceInterface, serviceImplement);
							}
						}
					}
				}
			}
		}
	}

	public void registerServiceFactory(Class<? extends ServiceFactory<?>> serviceFactoryClass) {
		if (serviceFactoryClass == null) {
			return;
		}
		Type serviceInterface = getParameterizedType(ServiceFactory.class, serviceFactoryClass);
		if (serviceInterface != null) {
			try {
				this.serviceFactories.put(serviceInterface, serviceFactoryClass.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
			}
		}
	}

	public <S extends Service> S getService(Class<S> serviceInterface, ServiceResource serviceResource)
			throws ServiceNotFoundException {
		if (serviceInterface == null) {
			throw new ServiceNotFoundException();
		}
		ServiceFactory<S> factory = (ServiceFactory) this.serviceFactories.get(serviceInterface);
		if (factory != null) {
			return factory.newInstance(serviceResource);
		}
		Class<? extends AbstractService> serviceImplement = (Class) this.serviceImplements.get(serviceInterface);
		if (serviceImplement == null) {
			throw new ServiceNotFoundException(serviceInterface.getCanonicalName());
		}
		try {
			return (S) serviceImplement.getConstructor(new Class[] { ServiceResource.class })
					.newInstance(new Object[] { serviceResource });
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ServiceNotFoundException(serviceInterface.getCanonicalName(), e);
		}
	}

	public void close() {
	}

	public void initilize(Connection connection) {
	}

	protected static abstract interface ParameterSetter {
		public abstract void set(PreparedStatement paramPreparedStatement, int paramInt, Object paramObject)
				throws SQLException;
	}
}