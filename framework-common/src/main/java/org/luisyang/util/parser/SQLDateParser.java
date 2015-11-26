package org.luisyang.util.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.luisyang.util.StringHelper;

/**
 * SQL_Date解析 
 */
public class SQLDateParser {
	
	protected static final String DEFAULT_PATTERN = "yyyy-MM-dd";

	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static java.sql.Date parse(String value) {
		return parse(value, "yyyy-MM-dd");
	}

	/**
	 * 解析字符串
	 * @param value 内容
	 * @param pattern 格式
	 * @return
	 */
	public static java.sql.Date parse(String value, String pattern) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		try {
			return new java.sql.Date(fmt.parse(value).getTime());
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 解析字符串数组
	 * @param values 内容数组
	 * @return
	 */
	public static java.sql.Date[] parseArray(String[] values) {
		return parseArray(values, "yyyy-MM-dd");
	}

	/**
	 * 解析字符串数组
	 * @param values 内容数组
	 * @param pattern  格式
	 * @return
	 */
	public static java.sql.Date[] parseArray(String[] values, String pattern) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		java.sql.Date[] dates = new java.sql.Date[values.length];
		for (int i = 0; i < values.length; i++) {
			if (!StringHelper.isEmpty(values[i])) {
				try {
					dates[i] = new java.sql.Date(fmt.parse(values[i]).getTime());
				} catch (ParseException e) {
				}
			}
		}
		return dates;
	}

	/**
	 * 格式化
	 * @param date 时间
	 * @return
	 */
	public static String format(java.sql.Date date) {
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * 格式化
	 * @param date 时间
	 * @param pattern 格式
	 * @return
	 */
	public static String format(java.sql.Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.format(date);
		} catch (IllegalArgumentException e) {
		}
		return "";
	}
}
