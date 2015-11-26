package org.luisyang.framework.data.sql.mysql;

import java.sql.SQLException;
import java.util.Properties;

import org.luisyang.framework.data.sql.SQLConnection;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;
import org.luisyang.util.StringHelper;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 
 * 连接池供应商
 * 
 * @author LuisYang
 */
public abstract class AbstractPooledConnectionProvider extends AbstractMySQLConnectionProvider {
	private DruidDataSource dataSource;

	/**
	 * 初始化
	 * @param parameterProvider
	 * @param logger
	 */
	public AbstractPooledConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
		try {
			this.dataSource = ((DruidDataSource) DruidDataSourceFactory
					.createDataSource(getProperties(parameterProvider)));
		} catch (Exception e) {
			logger.error("AbstractPooledConnectionProvider.dataSource init", e);
		}
	}

	/**
	 * 获得配置属性
	 * @param parameterProvider
	 * @return
	 */
	private Properties getProperties(InitParameterProvider parameterProvider) {
		Properties properties = new Properties();
		String value = parameterProvider.getInitParameter("name");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("name", value);
		}
		value = parameterProvider.getInitParameter("url");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("url", value);
		}
		value = parameterProvider.getInitParameter("username");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("username", value);
		}
		value = parameterProvider.getInitParameter("password");
		if (!StringHelper.isEmpty(value)) {
			try {
				properties.setProperty("password", StringHelper.decode(value));
			} catch (Throwable e) {
				this.logger.error("AbstractPooledConnectionProvider.setPassword", e);
			}
		}
		value = parameterProvider.getInitParameter("driverClassName");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("driverClassName", value);
		}
		value = parameterProvider.getInitParameter("initialSize");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("initialSize", value);
		}
		value = parameterProvider.getInitParameter("maxActive");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("maxActive", value);
		}
		value = parameterProvider.getInitParameter("minIdle");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("minIdle", value);
		}
		value = parameterProvider.getInitParameter("maxWait");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("maxWait", value);
		}
		value = parameterProvider.getInitParameter("poolPreparedStatements");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("poolPreparedStatements", value);
		}
		value = parameterProvider.getInitParameter("maxOpenPreparedStatements");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("maxOpenPreparedStatements", value);
		}
		value = parameterProvider.getInitParameter("validationQuery");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("validationQuery", value);
		}
		value = parameterProvider.getInitParameter("testOnBorrow");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("testOnBorrow", value);
		}
		value = parameterProvider.getInitParameter("testOnReturn");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("testOnReturn", value);
		}
		value = parameterProvider.getInitParameter("testWhileIdle");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("testWhileIdle", value);
		}
		value = parameterProvider.getInitParameter("timeBetweenEvictionRunsMillis");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("timeBetweenEvictionRunsMillis", value);
		}
		value = parameterProvider.getInitParameter("numTestsPerEvictionRun");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("numTestsPerEvictionRun", value);
		}
		value = parameterProvider.getInitParameter("minEvictableIdleTimeMillis");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("minEvictableIdleTimeMillis", value);
		}
		value = parameterProvider.getInitParameter("connectionInitSqls");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("connectionInitSqls", value);
		}
		value = parameterProvider.getInitParameter("exceptionSorter");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("exceptionSorter", value);
		}
		value = parameterProvider.getInitParameter("filters");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("filters", value);
		}
		value = parameterProvider.getInitParameter("proxyFilters");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("proxyFilters", value);
		}
		value = parameterProvider.getInitParameter("useUnfairLock");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("useUnfairLock", value);
		}
		value = parameterProvider.getInitParameter("removeAbandoned");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("removeAbandoned", value);
		}
		value = parameterProvider.getInitParameter("removeAbandonedTimeout");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("removeAbandonedTimeout", value);
		}
		value = parameterProvider.getInitParameter("logAbandoned");
		if (!StringHelper.isEmpty(value)) {
			properties.setProperty("logAbandoned", value);
		}
		return properties;
	}

	/**
	 * 获得链接
	 */
	public SQLConnection getConnection() throws SQLException {
		return new DruidConnectionWrapper(this.dataSource.getConnection());
	}

	public void close() {
		this.dataSource.close();
	}
}
