package org.luisyang.util.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.luisyang.util.StringHelper;

/**
 * 精准时间解析
 */
public class DateTimeParser {
	protected static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 解析时间
	 * @param value
	 * @return
	 */
	public static Date parse(String value) {
		return parse(value, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 解析时间
	 * @param value 内容
	 * @param pattern 格式
	 * @return
	 */
	public static Date parse(String value, String pattern) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		try {
			return fmt.parse(value);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 解析数组
	 * @param values
	 * @return
	 */
	public static Date[] parseArray(String[] values) {
		return parseArray(values, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 解析数组
	 * @param values 内容
	 * @param pattern 格式
	 * @return
	 */
	public static Date[] parseArray(String[] values, String pattern) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		Date[] dates = new Date[values.length];
		for (int i = 0; i < values.length; i++) {
			if (!StringHelper.isEmpty(values[i])) {
				try {
					dates[i] = fmt.parse(values[i]);
				} catch (ParseException e) {
				}
			}
		}
		return dates;
	}

	/**
	 * 格式化时间 
	 * @param date  时间
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化时间
	 * @param date 时间
	 * @param pattern 格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.format(date);
		} catch (IllegalArgumentException e) {
		}
		return "";
	}
}
