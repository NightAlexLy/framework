package org.luisyang.framework.config;

/**
 * 环境接口
 * 
 * @author LuisYang
 */
public abstract interface Envionment {

	/**
	 * 获得环境信息
	 * @param key
	 * @return
	 */
	public abstract String get(String key);

	/**
	 * 设置环境信息
	 * @param key
	 * @param value
	 */
	public abstract void set(String key, String value);
}