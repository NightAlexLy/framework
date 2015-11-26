package org.luisyang.framework.data.memcached;

import java.io.IOException;

import org.luisyang.framework.data.DataConnection;

import net.rubyeye.xmemcached.MemcachedClient;

/**
 * Memcached数据连接
 * 
 */
public abstract interface MemcachedConnection extends DataConnection {
	/**
	 * 获得链接
	 * @return
	 * @throws IOException
	 */
	public abstract MemcachedClient getConnection() throws IOException;
}