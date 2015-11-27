package org.luisyang.framework.config.entity;

import java.io.Serializable;

import org.luisyang.framework.http.entity.RightBean;
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
	  
	/**
	 * 获得模块权限
	 * @return
	 */
	public abstract RightBean[] getRightBeans();
	
}
