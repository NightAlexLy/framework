package org.luisyang.util.parser;

import org.luisyang.util.StringHelper;

public class IntegerParser {
	/**
	 * 解析，Object转为Int
	 * @param value
	 * @return
	 */
	public static int parse(Object value) {
		return value == null ? 0 : parse(value.toString(), 0);
	}

	/**
	 * 解析对象
	 * @param value
	 * @return
	 */
	public static Integer parseObject(Object value) {
		return Integer.valueOf(value == null ? Integer.valueOf(0).intValue() : parse(value.toString(), 0));
	}

	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static int parse(String value) {
		return parse(value, 0);
	}

	/**
	 * 解析
	 * @param value 字符串
	 * @param defaultValue 默认值
	 * @return
	 */
	public static int parse(String value, int defaultValue) {
		if (StringHelper.isEmpty(value)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(StringHelper.trim(value).replaceAll(",", ""));
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	/**
	 * 解析数组
	 * @param values 数组
	 * @return
	 */
	public static int[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		int[] fs = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				fs[i] = Integer.parseInt(StringHelper.trim(values[i]).replaceAll(",", ""));
			} catch (NumberFormatException e) {
				fs[i] = 0;
			}
		}
		return fs;
	}

	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static Integer parseString(String value) {
		return Integer.valueOf(parse(value));
	}
	
	/**
	 * 解析字符串数组
	 * @param values 字符串数据
	 * @return
	 */
	public static Integer[] parseStringArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Integer[] fs = new Integer[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				fs[i] = Integer.valueOf(StringHelper.trim(values[i]).replaceAll(",", ""));
			} catch (NumberFormatException e) {
				fs[i] = Integer.valueOf(0);
			}
		}
		return fs;
	}
}
