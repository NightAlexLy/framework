package org.luisyang.framework.service.query;

/**
 * 分页结果模型
 * 
 */
public abstract interface PagingResult<T> extends Paging {
	
	/**
	 * 当前元素条数
	 * @return
	 */
	public abstract int getItemCount();

	/**
	 * 分页条数
	 * @return
	 */
	public abstract int getPageCount();

	/**
	 * 元素集合
	 * @return
	 */
	public abstract T[] getItems();
}
