package org.luisyang.framework.data.redis;

import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;

/**
 * Redis数据源供应商
 * 
 * @author LuisYang
 */
public abstract class RedisConnectionProvider extends DataConnectionProvider {
	
	/**
	 * 初始化
	 * @param parameterProvider
	 * @param logger
	 */
	public RedisConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
	}

	/**
	 * 获得数据连接
	 */
	public abstract RedisConnection getConnection();

	/**
	 * 设置服务地址
	 * @param paramString
	 */
	public abstract void setServerAddress(String address);

	/**
	 * 设置服务地址
	 * @param address
	 * @param port
	 */
	public abstract void setServerAddress(String address, int port);

	/**
	 * 唯一标识类
	 */
	public final Class<? extends DataConnectionProvider> getIdentifiedType() {
		return RedisConnectionProvider.class;
	}
}