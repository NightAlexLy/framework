package org.luisyang.framework.config.entity;

/**
 * 变量类型接口
 * 
 * @author LuisYang
 */
public abstract interface VariableType {

	/**
	 * 唯一ID
	 * @return
	 */
	public abstract String getId();

	/**
	 * 类型名称
	 * @return
	 */
	public abstract String getName();

	/**
	 * 获得所有变量模型数组
	 * @return
	 */
	public abstract VariableBean[] getVariableBeans();
}
