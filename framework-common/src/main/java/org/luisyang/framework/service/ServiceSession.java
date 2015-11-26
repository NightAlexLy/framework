package org.luisyang.framework.service;

import java.sql.Connection;

import org.luisyang.framework.http.servlet.Controller;
import org.luisyang.framework.http.session.Session;
import org.luisyang.framework.resource.Prompter;
import org.luisyang.framework.resource.ResourceInvalidatedException;
import org.luisyang.framework.resource.ResourceNotFoundException;
import org.luisyang.framework.service.exception.ServiceNotFoundException;

/**
 * 服务会话
 * 
 * @author LuisYang
 */
public abstract interface ServiceSession extends AutoCloseable, Prompter {
	
	/**
	 * 获得会话
	 * @return
	 */
	public abstract Session getSession();

	/**
	 * 获得服务
	 * @param serviceInterface  服务接口类
	 * @return
	 * @throws ServiceNotFoundException  服务不存在
	 * @throws ResourceInvalidatedException 资源已失效
	 */
	public abstract <S extends Service> S getService(Class<S> serviceInterface)
			throws ServiceNotFoundException, ResourceInvalidatedException;

	/**
	 * 开启事务
	 * @param paramConnection
	 * @throws Throwable
	 */
	public abstract void openTransactions(Connection connection) throws Throwable;

	/**
	 * 事务级别
	 * @param level  级别
	 * @throws Throwable
	 */
	public abstract void openTransactions(int level) throws Throwable;

	/**
	 * 提交事务
	 * @param connection
	 * @throws Throwable
	 */
	public abstract void commit(Connection connection) throws Throwable;

	/**
	 * 回滚事务
	 * @param connection
	 * @throws Throwable
	 */
	public abstract void rollback(Connection connection) throws Throwable;

	/**
	 * 事务是否无效
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isTransactions() throws Throwable;

	/**
	 * 关闭
	 */
	public abstract void close();

	/**
	 * 获得控制器
	 * @return
	 * @throws ResourceNotFoundException 资源找不到
	 */
	public abstract Controller getController() throws ResourceNotFoundException;

}
