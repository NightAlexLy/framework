package org.luisyang.framework.data.mongo;

import org.luisyang.framework.data.DataConnectionProvider;
import org.luisyang.framework.log.Logger;
import org.luisyang.framework.resource.InitParameterProvider;

import com.mongodb.ServerAddress;

/**
 * MongoDb数据源供应商
 * 
 * @author LuisYang
 */
public abstract class MongoConnectionProvider extends DataConnectionProvider {
	
	/**
	 * 初始化
	 * @param parameterProvider
	 * @param logger
	 */
	public MongoConnectionProvider(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
	}

	/**
	 * 获得链接
	 */
	public abstract MongoConnection getConnection();

	/**
	 * 设置服务地址
	 * @param paramVarArgs
	 */
	public abstract void setServerAddresses(ServerAddress... paramVarArgs);

	/**
	 * 唯一标识类
	 */
	public final Class<? extends DataConnectionProvider> getIdentifiedType() {
		return MongoConnectionProvider.class;
	}
}
