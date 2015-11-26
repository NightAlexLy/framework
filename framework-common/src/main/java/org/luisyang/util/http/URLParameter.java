package org.luisyang.util.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.luisyang.util.StringHelper;

/**
 * URL地址参数
 */
public class URLParameter {
	
	protected final String prefix;
	protected final boolean parameters;
	protected final StringBuilder builder = new StringBuilder();
	protected final int initialLenght;
	protected final String[] keys;
	protected final boolean multiSelect;
	private String charset = "UTF-8";
	private HttpServletRequest request;
	private Map<String, Set<String>> map = new HashMap();

	public URLParameter(HttpServletRequest request) {
		this(request, null, false, new String[0]);
	}

	public URLParameter(HttpServletRequest request, String prefix) {
		this(request, prefix, false, new String[0]);
	}

	public URLParameter(HttpServletRequest request, String prefix, boolean multiSelect, String... keys) {
		this.request = request;

		this.prefix = (StringHelper.isEmpty(prefix) ? null : prefix);
		this.parameters = (this.prefix != null);
		if (this.prefix != null) {
			this.builder.append(prefix);
		}
		this.request = request;
		this.initialLenght = this.builder.length();
		this.keys = keys;
		this.multiSelect = multiSelect;
		clear();
	}

	public void setCharset(String charset) {
		if (!StringHelper.isEmpty(charset)) {
			this.charset = charset;
		}
	}

	public void set(String key, String value) {
		if ((StringHelper.isEmpty(key)) || (StringHelper.isEmpty(value))) {
			return;
		}
		Set<String> list = (Set) this.map.get(key);
		if (list == null) {
			list = new LinkedHashSet(1);
			this.map.put(key, list);
		}
		if (this.multiSelect) {
			list.add(value);
		} else {
			list.clear();
			list.add(value);
		}
	}

	public boolean contains(String key) {
		Set<String> set = (Set) this.map.get(key);
		return (set != null) && (set.size() > 0);
	}

	public boolean contains(String key, String value) {
		Set<String> set = (Set) this.map.get(key);
		return (set != null) && (set.contains(value));
	}

	public String[] remove(String key) {
		Set<String> list = (Set) this.map.get(key);
		String[] values = null;
		if ((list != null) && (list.size() > 0)) {
			values = (String[]) list.toArray(new String[list.size()]);
			list.clear();
		}
		return values;
	}

	public void remove(String key, String value) {
		Set<String> list = (Set) this.map.get(key);
		if (list != null) {
			list.remove(value);
		}
	}

	public void clear() {
		this.map.clear();
		if ((this.keys != null) && (this.keys.length > 0) && (this.request != null)) {
			if (this.multiSelect) {
				for (String key : this.keys) {
					String[] values = this.request.getParameterValues(key);
					if ((values != null) && (values.length != 0)) {
						Set<String> valueList = (Set) this.map.get(key);
						if (valueList == null) {
							valueList = new LinkedHashSet(values.length);
							this.map.put(key, valueList);
						} else {
							valueList.clear();
						}
						for (String value : values) {
							if (!StringHelper.isEmpty(value)) {
								valueList.add(value);
							}
						}
					}
				}
			} else {
				for (String key : this.keys) {
					String value = this.request.getParameter(key);
					if (!StringHelper.isEmpty(value)) {
						Set<String> valueList = (Set) this.map.get(key);
						if (valueList == null) {
							valueList = new LinkedHashSet(1);
							this.map.put(key, valueList);
						} else {
							valueList.clear();
						}
						valueList.add(value);
					}
				}
			}
		}
	}

	public String getQueryString() {
		if (this.map.size() <= 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		boolean hasParameters;
		String key;
		try {
			hasParameters = false;
			for (Map.Entry<String, Set<String>> entry : this.map.entrySet()) {
				Set<String> values = (Set) entry.getValue();
				if ((values != null) && (values.size() != 0)) {
					key = (String) entry.getKey();
					for (String value : values) {
						if (hasParameters) {
							builder.append('&');
						} else {
							builder.append('?');
							hasParameters = true;
						}
						builder.append(key).append('=').append(URLEncoder.encode(value, this.charset));
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	public String toString() {
		this.builder.setLength(this.initialLenght);
		String key;
		try {
			boolean hasParameters = this.parameters;
			if (this.map.size() > 0) {
				for (Map.Entry<String, Set<String>> entry : this.map.entrySet()) {
					Set<String> values = (Set) entry.getValue();
					if ((values != null) && (values.size() != 0)) {
						key = (String) entry.getKey();
						for (String value : values) {
							if (hasParameters) {
								this.builder.append('&');
							} else {
								this.builder.append('?');
								hasParameters = true;
							}
							this.builder.append(key).append('=').append(URLEncoder.encode(value, this.charset));
						}
					}
				}
			}
			clear();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this.builder.toString();
	}
}