package org.luisyang.framework.data.sql;

import java.sql.SQLException;

import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;
import org.luisyang.util.StringHelper;

/**
 * SQL数据源供应商
 * 
 * @author LuisYang
 */
public abstract class SQLConnectionProvider extends DataConnectionProvider {
	
	/**
	 * 初始化
	 * @param parameterProvider
	 * @param logger
	 */
	public SQLConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
	}

	/**
	 * 获得数据源
	 */
	public abstract SQLConnection getConnection() throws SQLException;

	/**
	 * 获得链接
	 */
	public SQLConnection getConnection(String schema) throws SQLException {
		SQLConnection connection = getConnection();
		if (!StringHelper.isEmpty(schema)) {
			connection.setSchema(schema);
		}
		return connection;
	}

	/**
	 * 唯一标识符
	 */
	public final Class<? extends DataConnectionProvider> getIdentifiedType() {
		return SQLConnectionProvider.class;
	}

	/**
	 * 前缀匹配
	 * @param paramString
	 * @return
	 */
	public abstract String prefixMatch(String value);

	/**
	 * 后缀匹配
	 * @param subfix
	 * @return
	 */
	public abstract String subfixMatch(String value);

	/**
	 * 全局匹配
	 * @param all
	 * @return
	 */
	public abstract String allMatch(String value);
}