package org.luisyang.framework.http.service;

import java.sql.Timestamp;

import org.luisyang.framework.http.entity.RoleBean;
import org.luisyang.framework.service.Service;
import org.luisyang.framework.service.query.Paging;
import org.luisyang.framework.service.query.PagingResult;

/**
 * 角色管理
 * 
 * @author LuisYang
 */
public abstract interface RoleManage extends Service {
	
	/**
	 * 添加角色
	 * @param name
	 * @param description
	 * @return
	 * @throws Throwable
	 */
	public abstract int add(String name, String description) throws Throwable;

	/**
	 * 删除角色
	 * @param roleId
	 * @throws Throwable
	 */
	public abstract void delete(int roleId) throws Throwable;

	/**
	 * 激活
	 * @param roleId
	 * @throws Throwable
	 */
	public abstract void active(int roleId) throws Throwable;

	/**
	 * 停用角色
	 * @param roleId
	 * @throws Throwable
	 */
	public abstract void inActive(int roleId) throws Throwable;

	/**
	 * 获得用户角色
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public abstract RoleBean[] getRoles(int userId) throws Throwable;

	/**
	 * 设置用户角色
	 * @param userId
	 * @param roleId
	 * @throws Throwable
	 */
	public abstract void setRoles(int userId, int... roleId) throws Throwable;

	/**
	 * 获得指定角色
	 * @param roleId
	 * @return
	 * @throws Throwable
	 */
	public abstract RoleBean getRole(int roleId) throws Throwable;

	/**
	 * 分页查询角色
	 * @param query
	 * @param paramPaging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<RoleBean> search(RoleQuery query, Paging paramPaging) throws Throwable;

	/**
	 * 角色查询模型
	 * 
	 * @author LuisYang
	 */
	public static abstract interface RoleQuery {
		
		public abstract int getRoleId();

		public abstract String getName();

		public abstract String getDescription();

		public abstract int getCreaterId();

		public abstract Timestamp getCreateTimeStart();

		public abstract Timestamp getCreateTimeEnd();

		public abstract String getStatus();
	}
}
