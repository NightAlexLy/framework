package org.luisyang.util.parser;

import org.luisyang.util.StringHelper;

/**
 * 低精度解析 
 */
public class FloatParser {
	
	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static float parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return 0.0F;
		}
		try {
			return Float.parseFloat(StringHelper.trim(value).replaceAll(",", ""));
		} catch (NumberFormatException e) {
		}
		return 0.0F;
	}

	/**
	 * 解析字符串数组
	 * @param values
	 * @return
	 */
	public static float[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		float[] fs = new float[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				try {
					fs[i] = Float.parseFloat(StringHelper.trim(values[i]).replaceAll(",", ""));
				} catch (NumberFormatException e) {
					fs[i] = 0.0F;
				}
			}
		}
		return fs;
	}

	/**
	 * 解析对象
	 * @param value
	 * @return
	 */
	public static Float parseObject(String value) {
		return Float.valueOf(parse(value));
	}

	/**
	 * 解析对象数组
	 * @param values
	 * @return
	 */
	public static Float[] parseObjectArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Float[] fs = new Float[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				try {
					fs[i] = Float.valueOf(StringHelper.trim(values[i]).replaceAll(",", ""));
				} catch (NumberFormatException e) {
					fs[i] = Float.valueOf(0.0F);
				}
			}
		}
		return fs;
	}
}
