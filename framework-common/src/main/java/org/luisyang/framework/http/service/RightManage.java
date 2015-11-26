package org.luisyang.framework.http.service;

import org.luisyang.framework.http.entity.RightBean;
import org.luisyang.framework.service.Service;

/**
 * 权限管理
 * 
 * @author LuisYang
 */
public abstract interface RightManage extends Service {

	/**
	 * 匹配权限
	 * @param userId  用户ID
	 * @param rightId　权限ID
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean hasRight(int userId, String rightId) throws Throwable;

	/**
	 * 设置用户权限
	 * @param userId  用户ID 
	 * @param rightIds 权限ID
	 * @throws Throwable
	 */
	public abstract void setUserRights(int userId, String... rightIds) throws Throwable;

	/**
	 * 设置角色权限
	 * @param roleId 角色ID
	 * @param rightIds 权限IDs
	 * @throws Throwable
	 */
	public abstract void setRoleRights(int roleId, String... rightIds) throws Throwable;

	/**
	 * 获得用户权限
	 * @param userId 用户ID
	 * @return
	 * @throws Throwable
	 */
	public abstract RightBean[] getUserRights(int userId) throws Throwable;

	/**
	 * 获得角色权限
	 * @param roleId 角色ID
	 * @return
	 * @throws Throwable
	 */
	public abstract RightBean[] getRoleRights(int roleId) throws Throwable;
}
