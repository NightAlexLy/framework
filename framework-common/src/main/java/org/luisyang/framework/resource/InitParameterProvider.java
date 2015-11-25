package org.luisyang.framework.resource;

import java.util.Enumeration;

/**
 * 初始化参数供应商
 * 
 * @author LuisYang
 */
public abstract interface InitParameterProvider {
	
	/**
	 * 根据名称获得值
	 * @param name
	 * @return
	 */
	public abstract String getInitParameter(String name);

	/**
	 * 获得初始化参数枚举集合
	 * @return 参数枚举
	 */
	public abstract Enumeration<String> getInitParameterNames();
}
