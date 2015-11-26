package org.luisyang.util.parser;

import org.luisyang.util.StringHelper;

/**
 * 整型解析 
 */
public class ShortParser {
	
	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static short parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return 0;
		}
		try {
			return Short.valueOf(StringHelper.trim(value).replaceAll(",", "")).shortValue();
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	/**
	 * 解析字符串数组
	 * @param values
	 * @return
	 */
	public static short[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		short[] fs = new short[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				fs[i] = Short.valueOf(StringHelper.trim(values[i]).replaceAll(",", "")).shortValue();
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
	public static Short parseObject(String value) {
		return Short.valueOf(parse(value));
	}

	/**
	 * 解析字符串数组
	 * @param values
	 * @return
	 */
	public static Short[] parseObjectArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Short[] fs = new Short[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				fs[i] = Short.valueOf(StringHelper.trim(values[i]).replaceAll(",", ""));
			} catch (NumberFormatException e) {
				fs[i] = Short.valueOf("0");
			}
		}
		return fs;
	}
}