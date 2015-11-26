package org.luisyang.util.parser;

import java.lang.reflect.Array;

import org.luisyang.util.StringHelper;

/**
 * 枚举解析
 */
public class EnumParser {
	/**
	 * 解析
	 * 
	 * @param enumClass
	 * @param value
	 * @return
	 */
	public static <E extends Enum<E>> E parse(Class<E> enumClass, String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return Enum.valueOf(enumClass, value);
		} catch (IllegalArgumentException localIllegalArgumentException) {
		}
		return null;
	}

	/**
	 * 解析数组
	 * 
	 * @param enumClass
	 * @param values
	 * @return
	 */
	public static <E extends Enum<E>> E[] parseArray(Class<E> enumClass, String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Enum<E>[] es = (Enum[]) Array.newInstance(enumClass, values.length);
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				try {
					es[i] = Enum.valueOf(enumClass, values[i]);
				} catch (IllegalArgumentException e) {
					es[i] = null;
				}
			}
		}
		return (E[]) es;
	}
}
