package org.luisyang.framework.service.query;

/**
 * 分页模型
 *
 * @author LuisYang
 */
public abstract interface Paging {
	
	/**
	 * 当前页
	 * @return
	 */
	public abstract int getCurrentPage();

	/**
	 * 条数
	 * @return
	 */
	public abstract int getSize();
}
