package org.luisyang.util.parser;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.luisyang.util.StringHelper;

/**
 * 时间解析
 */
public class TimeParser {
	protected static final String DEFAULT_PATTERN = "HH:mm:ss";

	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static Time parse(String value) {
		return parse(value, "HH:mm:ss");
	}

	/**
	 * 解析字符串
	 * @param value 内容
	 * @param pattern 格式
	 * @return
	 */
	public static Time parse(String value, String pattern) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "HH:mm:ss";
		}
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		try {
			return new Time(fmt.parse(value).getTime());
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 解析数组
	 * @param values 内容数组
	 * @return
	 */
	public static Time[] parseArray(String[] values) {
		return parseArray(values, "HH:mm:ss");
	}

	/**
	 * 解析数组
	 * @param values 内容
	 * @param pattern 格式
	 * @return
	 */
	public static Time[] parseArray(String[] values, String pattern) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "HH:mm:ss";
		}
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		Time[] dates = new Time[values.length];
		for (int i = 0; i < values.length; i++) {
			if (!StringHelper.isEmpty(values[i])) {
				try {
					dates[i] = new Time(fmt.parse(values[i]).getTime());
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
	public static String format(Date date) {
		return format(date, "HH:mm:ss");
	}

	/**
	 * 格式化
	 * @param date 时间
	 * @param pattern 格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (StringHelper.isEmpty(pattern)) {
			pattern = "HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.format(date);
		} catch (IllegalArgumentException e) {
		}
		return "";
	}
}