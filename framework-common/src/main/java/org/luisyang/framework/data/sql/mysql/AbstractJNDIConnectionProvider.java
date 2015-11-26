package org.luisyang.framework.data.sql.mysql;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.luisyang.framework.data.sql.SQLConnection;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;

/**
 * 抽象数据源连接
 * 
 * @author LuisYang
 */
public abstract class AbstractJNDIConnectionProvider extends AbstractMySQLConnectionProvider {
	protected abstract String getResource();

	/**
	 * 初始化
	 * @param parameterProvider
	 * @param logger
	 */
	public AbstractJNDIConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
	}

	/**
	 * 获得链接
	 */
	public SQLConnection getConnection() throws SQLException {
		try {
			Context context = (Context) new InitialContext().lookup("java:/comp/env");

			String resource = getResource();
			Object object = context.lookup(resource);
			if ((object == null) || (!(object instanceof DataSource))) {
				throw new NamingException(resource);
			}
			return new ConnectionWrapper(((DataSource) object).getConnection());
		} catch (Throwable e) {
			throw new SQLException(e);
		}
	}

	/**
	 * 关闭
	 */
	public void close() {
	}
}
