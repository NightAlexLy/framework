package org.luisyang.framework.service.query;

/**
 * 空分页返回
 * 
 *  @author LuisYang
 */
public class EmptyPageResult<T> implements PagingResult<T> {
	public int getCurrentPage() {
		return 1;
	}

	public int getSize() {
		return 10;
	}

	public int getItemCount() {
		return 0;
	}

	public int getPageCount() {
		return 1;
	}

	public T[] getItems() {
		return null;
	}
}
