package org.luisyang.util.parser;

import org.luisyang.util.StringHelper;

/**
 * 布尔类型解析
 * 
 * @author LuisYang
 */
public class BooleanParser {
	
	/**
	 * <b>解析字符串</b>
	 * <ul>
	 * 	<li>Y --> true</li>
	 *  <li>1 --> true</li>
	 *  <li>true --> true</li>
	 *  <li>yes --> true</li>
	 *  <li>null ---> false</li>
	 *  <li>"" ---> false</li>
	 * </ul>
	 * @param value
	 * @return
	 */
	public static boolean parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return false;
		}
		value = value.trim();
		if (("Y".equalsIgnoreCase(value)) || ("1".equalsIgnoreCase(value)) || ("true".equalsIgnoreCase(value))
				|| ("yes".equalsIgnoreCase(value))) {
			return true;
		}
		return false;
	}

	/**
	 * 解析数组
	 * @param values
	 * @return
	 */
	public static boolean[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		boolean[] b = new boolean[values.length];
		for (int i = 0; i < values.length; i++) {
			b[i] = parse(values[i]);
		}
		return b;
	}

	/**
	 * 解析对象
	 * @param value
	 * @return
	 */
	public static Boolean parseObject(String value) {
		if (StringHelper.isEmpty(value)) {
			return Boolean.valueOf(false);
		}
		return Boolean.valueOf(parse(value));
	}

	/**
	 * 解析对象数组
	 * @param values
	 * @return
	 */
	public static Boolean[] parseObjectArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Boolean[] b = new Boolean[values.length];
		for (int i = 0; i < values.length; i++) {
			b[i] = Boolean.valueOf(parse(values[i]));
		}
		return b;
	}
}