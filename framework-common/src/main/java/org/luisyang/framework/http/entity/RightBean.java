package org.luisyang.framework.http.entity;

import java.io.Serializable;

/**
 * 用户权限模型
 * @author LuisYang
 */
public abstract interface RightBean extends Serializable {
	
	/**
	 * 
	 * @return
	 */
	public abstract String getId();

	/**
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 
	 * @return
	 */
	public abstract String getDescription();

	/**
	 * 
	 * @return
	 */
	public abstract String getModuleId();

	/**
	 * 
	 * @return
	 */
	public abstract boolean isDeprecated();

	/**
	 * 
	 * @return
	 */
	public abstract boolean isMenu();
}
