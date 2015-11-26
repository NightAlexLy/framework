package org.luisyang.framework.http.service;

import java.sql.Date;

import org.luisyang.framework.service.Service;

/**
 * 用户统计管理
 * 
 * @author Luisyang
 */
public abstract interface UserStatisticsManage extends Service {
	
	/**
	 * 查看指定日期登录次数
	 * @param date
	 * @return
	 * @throws Throwable
	 */
	public abstract int getLoginCount(Date date) throws Throwable;

	/**
	 * 查看在线人数
	 * @return
	 * @throws Throwable
	 */
	public abstract int getOnlineCount() throws Throwable;

	/**
	 * 查看在线人的地址
	 * @param date 日期
	 * @return
	 * @throws Throwable
	 */
	public abstract int[] getOnlineHistory(Date date) throws Throwable;

	/**
	 * 获得指定空间登录人数
	 * @param date 日期
	 * @param schemaName 空间名
	 * @return
	 * @throws Throwable
	 */
	public abstract int getLoginCount(Date date, String schemaName) throws Throwable;

	/**
	 * 查看在线人数
	 * @param schemaName 空间名
	 * @return
	 * @throws Throwable
	 */
	public abstract int getOnlineCount(String schemaName) throws Throwable;

	/**
	 * 获得指定空间在线人数
	 * @param date 日期
	 * @param schemaName 空间
	 * @return
	 * @throws Throwable
	 */
	public abstract int[] getOnlineHistory(Date date, String schemaName) throws Throwable;
}
