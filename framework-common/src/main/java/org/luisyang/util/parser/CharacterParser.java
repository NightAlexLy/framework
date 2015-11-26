package org.luisyang.util.parser;

import org.luisyang.util.StringHelper;

/**
 * 字符解析
 */
public class CharacterParser {
	
	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static char parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return '\000';
		}
		return value.charAt(0);
	}

	/**
	 * 解析字符串数组
	 * @param values
	 * @return
	 */
	public static char[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		char[] as = new char[values.length];
		for (int i = 0; i < values.length; i++) {
			if (!StringHelper.isEmpty(values[i])) {
				as[i] = values[i].charAt(0);
			}
		}
		return as;
	}

	/**
	 * 解析对象
	 * @param value
	 * @return
	 */
	public static Character parseObject(String value) {
		if (StringHelper.isEmpty(value)) {
			return Character.valueOf('\000');
		}
		return Character.valueOf(parse(value));
	}

	/**
	 * 解析对象数组
	 * @param values
	 * @return
	 */
	public static Character[] parseObjectArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Character[] as = new Character[values.length];
		for (int i = 0; i < values.length; i++) {
			as[i] = Character.valueOf(parse(values[i]));
		}
		return as;
	}
}
