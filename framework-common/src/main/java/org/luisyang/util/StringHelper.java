package org.luisyang.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.luisyang.framework.config.Envionment;
import org.luisyang.util.filter.AbstractFilter;

public class StringHelper {
	private static final char[] HEXES = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	private static final Pattern REPLACE_VARIABLE_PATTERN = Pattern.compile("\\$\\{\\s*(\\w|\\.|-|_|\\$)+\\s*\\}", 2);
	public static final String EMPTY_STRING = "";
	private static final byte[] ROW_BYTES = "80e36e39f34e678c".getBytes();

	/**
	 * 清空空格
	 * @param value 需要被清的字符串
	 * @return
	 */
	public static String trim(String value) {
		return value == null ? null : value.trim();
	}

	/**
	 * <b>判断是否为空</b>
	 * <ul>
	 * 	<li>StringHelp.isEmty(null) return true;</li>
	 *  <li>StringHelp.isEmty("") return true;</li>
	 * </ul>
	 * @param value  被判断的字符串
	 * @return true表示为空，  false不为空
	 */
	public static boolean isEmpty(String value) {
		int length;
		if ((value == null) || ((length = value.length()) == 0)) {
			return true;
		}
		for (int index = 0; index < length; index++) {
			char ch = value.charAt(index);
			if ((ch != ' ') && (ch != ' ') && (!Character.isISOControl(ch))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 过滤
	 * @param writer
	 * @param str
	 * @throws IOException
	 */
	public static void filter(AbstractFilter writer, String str) throws IOException {
		if (isEmpty(str)) {
			return;
		}
		writer.append(str);
	}

	/**
	 * 过滤HTML
	 * @param writer
	 * @param str
	 * @throws IOException
	 */
	public static void filterHTML(Appendable writer, String str) throws IOException {
		if (isEmpty(str)) {
			return;
		}
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (!Character.isISOControl(ch)) {
				switch (ch) {
				case '"':
				case '&':
				case '\'':
				case '<':
				case '>':
					writer.append("&#");
					writer.append(Integer.toString(ch));
					writer.append(';');
					break;
				default:
					writer.append(ch);
				}
			}
		}
	}

	/**
	 * 过滤饮用
	 * @param writer
	 * @param str
	 * @throws IOException
	 */
	public static void filterQuoter(Appendable writer, String str) throws IOException {
		if (isEmpty(str)) {
			return;
		}
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch == '"') {
				writer.append('\\');
			}
			writer.append(ch);
		}
	}

	/**
	 * 过滤相同的引用
	 * @param writer
	 * @param str
	 * @throws IOException
	 */
	public static void filterSingleQuoter(Appendable writer, String str) throws IOException {
		if (isEmpty(str)) {
			return;
		}
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch == '\'') {
				writer.append('\\');
			}
			writer.append(ch);
		}
	}

	/**
	 * 签名
	 * @param content 内容
	 * @return
	 * @throws Throwable
	 */
	public static String digest(String content) throws Throwable {
		if (isEmpty(content)) {
			return content;
		}
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		byte[] ciphertext = digest.digest(content.getBytes());
		char[] chars = new char[ciphertext.length + ciphertext.length];
		int i = 0;
		for (byte element : ciphertext) {
			chars[(i++)] = HEXES[(element & 0xF)];
			chars[(i++)] = HEXES[(element >> 4 & 0xF)];
		}
		return new String(chars);
	}

	/**
	 * 加密
	 * @param content 内容
	 * @return
	 * @throws Throwable
	 */
	public static String encode(String content) throws Throwable {
		if (isEmpty(content)) {
			return content;
		}
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec keySpec = new SecretKeySpec(ROW_BYTES, "AES");
		cipher.init(1, keySpec);
		byte[] ciphertext = cipher.doFinal(content.getBytes());
		return Base64.encodeBase64String(ciphertext);
	}

	/**
	 * 解密
	 * @param content 内容
	 * @return
	 * @throws Throwable
	 */
	public static String decode(String content) throws Throwable {
		if (isEmpty(content)) {
			return content;
		}
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec keySpec = new SecretKeySpec(ROW_BYTES, "AES");
		cipher.init(2, keySpec);
		byte[] ciphertext = cipher.doFinal(Base64.decodeBase64(content));
		return new String(ciphertext);
	}

	/**
	 * 格式化
	 * @param pattern 正则
	 * @param envionment 环境
	 * @return
	 */
	public static String format(String pattern, Envionment envionment) {
		if ((envionment == null) || (isEmpty(pattern))) {
			return pattern;
		}
		StringBuilder builder = new StringBuilder();
		try {
			format(builder, pattern, envionment);
		} catch (IOException e) {
		}
		return builder.toString();
	}

	/**
	 * 格式化
	 * @param out
	 * @param pattern
	 * @param envionment
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void format(Appendable out, String pattern, Envionment envionment) throws IOException {
		if ((out == null) || (envionment == null) || (isEmpty(pattern))) {
			return;
		}
		Set<String> loadedKeys = new HashSet();
		format(out, pattern, envionment, loadedKeys);
	}

	/**
	 * 格式化
	 * @param out
	 * @param pattern
	 * @param envionment
	 * @param loadedKeys
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final void format(Appendable out, String pattern, Envionment envionment, Set<String> loadedKeys)
			throws IOException {
		Matcher matcher = REPLACE_VARIABLE_PATTERN.matcher(pattern);
		int startIndex = 0;
		int endIndex = 0;
		while (matcher.find()) {
			endIndex = matcher.start();
			if (endIndex != startIndex) {
				out.append(pattern, startIndex, endIndex);
			}
			String key = matcher.group();
			key = key.substring(2, key.length() - 1).trim();
			if (!loadedKeys.contains(key)) {
				String value = envionment.get(key);
				if (value != null) {
					Set<String> set = new HashSet(loadedKeys);
					set.add(key);
					format(out, value, envionment, set);
				}
			}
			startIndex = matcher.end();
		}
		endIndex = pattern.length();
		if (startIndex < endIndex) {
			out.append(pattern, startIndex, endIndex);
		}
	}

	/**
	 * 截断指定字符串
	 * @param string 内容
	 * @param maxLength 长度
	 * @return
	 */
	public static String truncation(String string, int maxLength) {
		if (isEmpty(string)) {
			return "";
		}
		try {
			StringBuilder out = new StringBuilder();
			truncation(out, string, maxLength, null);
			return out.toString();
		} catch (IOException e) {
		}
		return "";
	}

	/**
	 * 截断指定字符串
	 * @param string 内容
	 * @param maxLength 长度
	 * @param replace 替换字符串
	 * @return
	 */
	public static String truncation(String string, int maxLength, String replace) {
		if (isEmpty(string)) {
			return "";
		}
		try {
			StringBuilder out = new StringBuilder();
			truncation(out, string, maxLength, replace);
			return out.toString();
		} catch (IOException e) {
		}
		return "";
	}

	/**
	 * 截断指定字符串
	 * @param out 输出
	 * @param string 内容
	 * @param maxLength 长度
	 * @throws IOException
	 */
	public static void truncation(Appendable out, String string, int maxLength) throws IOException {
		truncation(out, string, maxLength, null);
	}

	/**
	 * 截断指定字符串
	 * @param out 输出
	 * @param string 内容
	 * @param maxLength 长度
	 * @param replace 替换字符串  如果为空  则替换为...
	 * @throws IOException
	 */
	public static void truncation(Appendable out, String string, int maxLength, String replace) throws IOException {
		if ((isEmpty(string)) || (maxLength <= 0)) {
			return;
		}
		if (isEmpty(replace)) {
			replace = "...";
		}
		int index = 0;
		int end = Math.min(string.length(), maxLength);
		for (; index < end; index++) {
			out.append(string.charAt(index));
		}
		if (string.length() > maxLength) {
			out.append(replace);
		}
	}
}
