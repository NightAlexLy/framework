package org.luisyang.util.parser;

import java.math.BigInteger;

import org.luisyang.util.StringHelper;

/**
 * 高精度整型解析
 * 
 * @author LuisYang
 */
public class BigIntegerParser {

	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static BigInteger parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return BigInteger.valueOf(0L);
		}
		try {
			return new BigInteger(StringHelper.trim(value).replaceAll(",", ""));
		} catch (NumberFormatException e) {
		}
		return BigInteger.valueOf(0L);
	}

	/**
	 * 解析字符串数组
	 * @param values
	 * @return
	 */
	public static BigInteger[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		BigInteger[] ds = new BigInteger[values.length];
		for (int i = 0; i < values.length; i++) {
			if (StringHelper.isEmpty(values[i])) {
				ds[i] = BigInteger.valueOf(0L);
			} else {
				try {
					ds[i] = new BigInteger(StringHelper.trim(values[i]).replaceAll(",", ""));
				} catch (NumberFormatException e) {
					ds[i] = BigInteger.valueOf(0L);
				}
			}
		}
		return ds;
	}

}
