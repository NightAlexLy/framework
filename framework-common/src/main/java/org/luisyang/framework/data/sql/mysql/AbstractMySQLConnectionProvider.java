package org.luisyang.framework.data.sql.mysql;

import org.luisyang.framework.data.sql.SQLConnectionProvider;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;

/**
 * 抽象Mysql数据源供应商
 * 
 * @author LuisYang
 */
public abstract class AbstractMySQLConnectionProvider extends SQLConnectionProvider {
	
	/**
	 * 初始化
	 * @param parameterProvider
	 * @param logger
	 */
	public AbstractMySQLConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
	}

	@Override
	public String allMatch(String value) {
		StringBuilder builder = new StringBuilder(value.length() + 2);
		builder.append('%').append(value).append('%');
		return builder.toString();
	}

	@Override
	public String prefixMatch(String value) {
		return '%' + value;
	}

	@Override
	public String subfixMatch(String value) {
		return value + '%';
	}
}
