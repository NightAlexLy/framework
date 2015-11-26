package org.luisyang.framework.service.query;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 元素解析
 */
public abstract interface ItemParser<T> {
	
	/**
	 * 解析
	 * @param set
	 * @return
	 * @throws SQLException
	 */
	public abstract T parse(ResultSet set) throws SQLException;
}
