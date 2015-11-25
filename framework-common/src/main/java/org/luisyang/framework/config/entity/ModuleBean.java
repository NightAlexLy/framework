package org.luisyang.framework.config.entity;

import java.io.Serializable;
/**
 * 基本模型接口
 * @author luisYang
 */
public abstract interface ModuleBean extends Serializable {

	/**
	 * 获得ID
	 * @return
	 */
	public abstract String getId();
	  
	/**
	 * 获得Name
	 * @return
	 */
	public abstract String getName();
	  
	/**
	 * 获得描述
	 * @return
	 */
	public abstract String getDescription();
	  
	//public abstract RightBean[] getRightBeans();
	
}
