package org.luisyang.util.parser;

import org.luisyang.util.StringHelper;

/**
 * 整型解析
 */
public class LongParser {
	
	/**
	 * 解析对象
	 * @param value
	 * @return
	 */
	public static long parse(Object value) {
		return value == null ? 0L : parse(value.toString());
	}

	/**
	 * 解析对象
	 * @param value
	 * @return
	 */
	public static Long parseObject(Object value) {
		return Long.valueOf(value == null ? Long.valueOf(0L).longValue() : parse(value.toString()));
	}

	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static long parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return 0L;
		}
		try {
			return Long.parseLong(StringHelper.trim(value).replaceAll(",", ""));
		} catch (NumberFormatException e) {
		}
		return 0L;
	}

	/**
	 * 解析数组
	 * @param values
	 * @return
	 */
	public static long[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		long[] fs = new long[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				fs[i] = Long.parseLong(StringHelper.trim(values[i]).replaceAll(",", ""));
			} catch (NumberFormatException e) {
				fs[i] = 0L;
			}
		}
		return fs;
	}

	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static Long parseObject(String value) {
		return Long.valueOf(parse(value));
	}

	/**
	 * 解析字符串数组
	 * @param values
	 * @return
	 */
	public static Long[] parseObjectArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Long[] fs = new Long[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				fs[i] = Long.valueOf(StringHelper.trim(values[i]).replaceAll(",", ""));
			} catch (NumberFormatException e) {
				fs[i] = Long.valueOf(0L);
			}
		}
		return fs;
	}
}
