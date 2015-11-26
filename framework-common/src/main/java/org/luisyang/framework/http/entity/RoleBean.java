package org.luisyang.framework.http.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 角色模型
 * 
 * @author LuisYang
 */
public abstract interface RoleBean extends Serializable {
	
	public abstract int getRoleId();

	public abstract String getName();

	public abstract String getDescription();

	public abstract Timestamp getCreateTime();

	public abstract int getCreaterId();

	public abstract String getStatus();
}
