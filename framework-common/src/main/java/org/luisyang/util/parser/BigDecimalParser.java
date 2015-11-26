package org.luisyang.util.parser;

import java.math.BigDecimal;

import org.luisyang.util.StringHelper;

/**
 * 高精度数值解析
 * 
 * @author LuisYang
 */
public class BigDecimalParser {
	
	/**
	 * 解析
	 * @param value
	 * @return
	 */
	public static BigDecimal parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return BigDecimal.valueOf(0L);
		}
		try {
			return new BigDecimal(StringHelper.trim(value).replaceAll(",", ""));
		} catch (NumberFormatException e) {
		}
		return BigDecimal.valueOf(0L);
	}

	/**
	 * 解析数组
	 * @param values
	 * @return
	 */
	public static BigDecimal[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		BigDecimal[] ds = new BigDecimal[values.length];
		for (int i = 0; i < values.length; i++) {
			if (StringHelper.isEmpty(values[i])) {
				ds[i] = BigDecimal.valueOf(0L);
			} else {
				try {
					ds[i] = new BigDecimal(StringHelper.trim(values[i]).replaceAll(",", ""));
				} catch (NumberFormatException e) {
					ds[i] = BigDecimal.valueOf(0L);
				}
			}
		}
		return ds;
	}
}
