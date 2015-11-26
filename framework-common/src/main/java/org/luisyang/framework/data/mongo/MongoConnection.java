package org.luisyang.framework.data.mongo;

import org.luisyang.framework.data.DataConnection;

import com.mongodb.DB;

/**
 * MongoDb数据源
 * 
 * @author LuisYang
 */
public abstract interface MongoConnection extends DataConnection {
	public abstract DB getDB(String paramString);
}