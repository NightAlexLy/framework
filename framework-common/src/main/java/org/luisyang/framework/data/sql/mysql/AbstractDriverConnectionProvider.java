package org.luisyang.framework.data.sql.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.luisyang.framework.data.sql.SQLConnection;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;

/**
 * 抽象数据源驱动供应商
 * 
 * @author LuisYang
 */
public abstract class AbstractDriverConnectionProvider extends AbstractMySQLConnectionProvider {
	
	/**
	 * 驱动类名
	 */
	protected String driver;
	/**
	 * 地址
	 */
	protected String url;
	/**
	 * 属性
	 */
	protected Properties properties;

	/**
	 * 初始化
	 * @param parameterProvider
	 * @param logger
	 */
	public AbstractDriverConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
		this.properties = new Properties();
	}

	/**
	 * 获得链接
	 */
	public SQLConnection getConnection() throws SQLException {
		try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		return new ConnectionWrapper(this.properties == null ? DriverManager.getConnection(this.url)
				: DriverManager.getConnection(this.url, this.properties));
	}

	/**
	 * 关闭
	 */
	public void close() {
	}
}
