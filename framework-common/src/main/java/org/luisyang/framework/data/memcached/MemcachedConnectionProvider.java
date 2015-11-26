package org.luisyang.framework.data.memcached;

import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;

public abstract class MemcachedConnectionProvider extends DataConnectionProvider {
	
	/**
	 * 初始化
	 * @param parameterProvider
	 * @param logger
	 */
	public MemcachedConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
	}

	/**
	 * 获取链接
	 */
	public abstract MemcachedConnection getConnection();

	/**
	 * 设置服务器地址
	 * @param paramString
	 */
	public abstract void setServerAddresses(String address);

	/**
	 * 唯一标识类
	 */
	public final Class<? extends DataConnectionProvider> getIdentifiedType() {
		return MemcachedConnectionProvider.class;
	}
}