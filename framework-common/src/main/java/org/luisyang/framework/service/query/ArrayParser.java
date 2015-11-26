package org.luisyang.framework.service.query;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数组解析
 * 
 * @author LuisYang
 */
public abstract interface ArrayParser<T> {
	/**
	 * 解析
	 * 
	 * @param set
	 * @return
	 * @throws SQLException
	 */
	public abstract T[] parse(ResultSet set) throws SQLException;
}
