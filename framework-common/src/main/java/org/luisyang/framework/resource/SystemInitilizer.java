package org.luisyang.framework.resource;

import java.sql.Connection;

/**
 * 系统初始化
 * 
 * @author LuisYang
 */
public abstract interface SystemInitilizer {
	/**
	 * 初始化
	 * @param paramConnection
	 * @throws Throwable
	 */
	public abstract void initialize(Connection paramConnection) throws Throwable;
}
