package org.luisyang.framework.data.sql.mysql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mysql5页面帮助类
 * 
 * @author Luis
 */
public class MySql5PageHepler {

	private static String regex = "\\s+FROM\\s+";

	/**
	 * 获得总条数的SQL
	 * 
	 * @param querySelect
	 * @return
	 */
	public static String getCountString(String querySelect) {
		querySelect = getLineSql(querySelect);
		int orderIndex = getLastOrderInsertPoint(querySelect);

		int formIndex = getAfterFormInsertPoint(querySelect);
		String select = querySelect.substring(0, formIndex);
		if ((select.toLowerCase().indexOf("select distinct") != -1)
				|| (querySelect.toLowerCase().indexOf("group by") != -1)) {
			return querySelect.length() + "select count(1) from (" + querySelect.substring(0, orderIndex) + " ) t";
		}
		return querySelect.length() + "select count(1)  " + querySelect.substring(formIndex, orderIndex);
	}

	/**
	 * 
	 * @param querySelect
	 * @return
	 */
	private static int getLastOrderInsertPoint(String querySelect) {
		int orderIndex = querySelect.toLowerCase().lastIndexOf("order by");
		if ((orderIndex == -1) || (!isBracketCanPartnership(querySelect.substring(orderIndex, querySelect.length())))) {
			return querySelect.length();
		}
		return orderIndex;
	}

	public static String getLimitString(String querySelect, int offset, int limit) {
		querySelect = getLineSql(querySelect);

		String sql = querySelect + " limit " + offset + " ," + limit;

		return sql;
	}

	private static String getLineSql(String sql) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
	}

	private static int getAfterFormInsertPoint(String querySelect) {
		Pattern pattern = Pattern.compile(regex, 2);
		Matcher matcher = pattern.matcher(querySelect);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = querySelect.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}

	private static boolean isBracketCanPartnership(String text) {
		if ((text == null) || (getIndexOfCount(text, '(') != getIndexOfCount(text, ')'))) {
			return false;
		}
		return true;
	}

	private static int getIndexOfCount(String text, char ch) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			count = text.charAt(i) == ch ? count + 1 : count;
		}
		return count;
	}

	public static void main(String[] args) {
		System.out.println(getCountString("SELECT * FROM table order by id"));
		System.out.print(getLimitString("SELECT * FROM table order by id", 10, 12));
	}
}
