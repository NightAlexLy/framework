package org.luisyang.util.parser;

import org.luisyang.util.StringHelper;

/**
 * 高精度解析
 */
public class DoubleParser {
	/**
	 * 解析字符串
	 * @param value 内容
	 * @return
	 */
	public static double parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return 0.0D;
		}
		try {
			return Double.parseDouble(StringHelper.trim(value).replaceAll(",", ""));
		} catch (NumberFormatException e) {
		}
		return 0.0D;
	}

	/**
	 * 解析数组
	 * @param values
	 * @return
	 */
	public static double[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		double[] ds = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			if (!StringHelper.isEmpty(values[i])) {
				try {
					ds[i] = Double.parseDouble(StringHelper.trim(values[i]).replaceAll(",", ""));
				} catch (NumberFormatException e) {
					ds[i] = 0.0D;
				}
			}
		}
		return ds;
	}

	/**
	 * 解析对象
	 * @param value
	 * @return
	 */
	public Double parseObject(String value) {
		return Double.valueOf(parse(value));
	}

	/**
	 * 解析对象数组
	 * @param values
	 * @return
	 */
	public static Double[] parseObjectArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Double[] ds = new Double[values.length];
		for (int i = 0; i < values.length; i++) {
			if (!StringHelper.isEmpty(values[i])) {
				try {
					ds[i] = Double.valueOf(values[i].replaceAll(",", ""));
				} catch (NumberFormatException e) {
					ds[i] = Double.valueOf(0.0D);
				}
			}
		}
		return ds;
	}
}
