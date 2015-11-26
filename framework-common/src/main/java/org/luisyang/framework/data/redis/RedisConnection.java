package org.luisyang.framework.data.redis;

import org.luisyang.framework.data.DataConnection;

import redis.clients.jedis.Jedis;

/**
 * Redis数据连接
 * 
 * @author LuisYang
 */
public abstract interface RedisConnection extends DataConnection {
	
	/**
	 * 获得链接
	 * @return
	 */
	public abstract Jedis getJedis();

	/**
	 * 关闭
	 */
	public abstract void close();
}