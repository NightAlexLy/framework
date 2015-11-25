package org.luisyang.framework.data;

import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;

/**
 * 数据连接供应商
 * 
 * @author LuisYang
 */
public abstract class DataConnectionProvider implements AutoCloseable {

	/**
	 * 初始化参数供应商
	 */
	protected final InitParameterProvider parameterProvider;
	/**
	 * 日志模型
	 */
	protected final Logger logger;

	/**
	 * 初始化
	 * @param parameterProvider  初始化参数供应商
	 * @param logger 日志模型
	 */
	public DataConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		this.parameterProvider = parameterProvider;
		this.logger = logger;
	}

	/**
	 * 链接名
	 * @return
	 */
	public abstract String getName();

	/**
	 * 返回数据源类型唯一标示
	 * @return
	 */
	public abstract Class<? extends DataConnectionProvider> getIdentifiedType();

	/**
	 * 获得数据源
	 * @return
	 * @throws Throwable
	 */
	public abstract DataConnection getConnection() throws Throwable;

	/**
	 * 根据参数获得不同的数据源连接
	 * @param connName
	 * @return
	 * @throws Throwable
	 */
	public abstract DataConnection getConnection(String connName) throws Throwable;

	/**
	 * 关闭
	 */
	public abstract void close();
}
