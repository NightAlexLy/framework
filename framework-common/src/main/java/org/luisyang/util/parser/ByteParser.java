package org.luisyang.util.parser;

import org.luisyang.util.StringHelper;

/**
 * 字节解析
 * 
 * @author LuisYang
 */
public class ByteParser {
	
	/**
	 * 解析字符串
	 * @param value
	 * @return
	 */
	public static byte parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return 0;
		}
		byte b = 0;
		try {
			b = Byte.parseByte(value);
		} catch (NumberFormatException e) {
			return 0;
		}
		return b;
	}

	/**
	 * 解析字符串数组
	 * @param values
	 * @return
	 */
	public static byte[] parseArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		byte[] b = new byte[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				b[i] = Byte.parseByte(values[i]);
			} catch (NumberFormatException e) {
				b[i] = 0;
			}
		}
		return b;
	}

	/**
	 * 解析对象
	 * @param value
	 * @return
	 */
	public static Byte parseObject(String value) {
		if (StringHelper.isEmpty(value)) {
			return Byte.valueOf((byte) 0);
		}
		return Byte.valueOf(parse(value));
	}

	/**
	 * 解析对象数组
	 * @param values
	 * @return
	 */
	public static Byte[] parseObjectArray(String[] values) {
		if ((values == null) || (values.length == 0)) {
			return null;
		}
		Byte[] b = new Byte[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				b[i] = Byte.valueOf(values[i]);
			} catch (NumberFormatException e) {
				b[i] = Byte.valueOf("0");
			}
		}
		return b;
	}
}