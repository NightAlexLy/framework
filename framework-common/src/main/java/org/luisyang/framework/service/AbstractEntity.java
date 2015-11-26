package org.luisyang.framework.service;

import org.luisyang.framework.http.upload.PartFile;
import org.luisyang.framework.http.upload.UploadFile;
import org.luisyang.util.StringHelper;
import org.luisyang.util.parser.BigDecimalParser;
import org.luisyang.util.parser.BooleanParser;
import org.luisyang.util.parser.ByteParser;
import org.luisyang.util.parser.DateParser;
import org.luisyang.util.parser.DoubleParser;
import org.luisyang.util.parser.EnumParser;
import org.luisyang.util.parser.FloatParser;
import org.luisyang.util.parser.IntegerParser;
import org.luisyang.util.parser.LongParser;
import org.luisyang.util.parser.ShortParser;
import org.luisyang.util.parser.TimeParser;
import org.luisyang.util.parser.TimestampParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Map<Class<?>, FieldParser> PARSERS = new HashMap();

	private static final void register(FieldParser setter, Class<?>... types) {
		for (Class<?> type : types) {
			PARSERS.put(type, setter);
		}
	}

	static {
		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				try {
					Part part = request.getPart(name);
					if ((part != null) && (part.getSize() > 0L)) {
						return part;
					}
				} catch (IOException | ServletException e) {
				}
				return null;
			}
		}, new Class[] { Part.class });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				try {
					Part part = request.getPart(name);
					if ((part != null) && (part.getSize() > 0L)) {
						return new PartFile(part);
					}
				} catch (IOException | ServletException e) {
				}
				return null;
			}
		}, new Class[] { UploadFile.class, PartFile.class });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				if (StringHelper.isEmpty(value)) {
					return null;
				}
				return value.trim();
			}
		}, new Class[] { String.class });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return Integer.valueOf(IntegerParser.parse(value));
			}
		}, new Class[] { Integer.class, Integer.TYPE });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return Long.valueOf(LongParser.parse(value));
			}
		}, new Class[] { Long.class, Long.TYPE });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return Short.valueOf(ShortParser.parse(value));
			}
		}, new Class[] { Short.class, Short.TYPE });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return Byte.valueOf(ByteParser.parse(value));
			}
		}, new Class[] { Byte.class, Byte.TYPE });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return Double.valueOf(DoubleParser.parse(value));
			}
		}, new Class[] { Double.class, Double.TYPE });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return Boolean.valueOf(BooleanParser.parse(value));
			}
		}, new Class[] { Boolean.class, Boolean.TYPE });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return Float.valueOf(FloatParser.parse(value));
			}
		}, new Class[] { Float.class, Float.TYPE });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return TimeParser.parse(value);
			}
		}, new Class[] { Time.class });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return TimestampParser.parse(value);
			}
		}, new Class[] { Timestamp.class });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return DateParser.parse(value);
			}
		}, new Class[] { Date.class });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				if (StringHelper.isEmpty(value)) {
					return null;
				}
				try {
					return new URL(value.trim());
				} catch (MalformedURLException e) {
				}
				return null;
			}
		}, new Class[] { URL.class });

		register(new FieldParser() {
			public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
				String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
				if (StringHelper.isEmpty(name)) {
					name = fieldName;
				}
				String value = request.getParameter(name);
				return BigDecimalParser.parse(value);
			}
		}, new Class[] { BigDecimal.class });
	}

	private static final FieldParser INPUTSTREAM_PARSER = new FieldParser() {
		public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
			String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
			if (StringHelper.isEmpty(name)) {
				name = fieldName;
			}
			try {
				Part part = request.getPart(name);
				if ((part != null) && (part.getSize() > 0L)) {
					return part.getInputStream();
				}
			} catch (IOException | ServletException e) {
			}
			return null;
		}
	};
	private static final FieldParser PART_ARRAY_PARSER = new FieldParser() {
		public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
			String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
			if (StringHelper.isEmpty(name)) {
				name = fieldName;
			}
			try {
				Collection<Part> parts = request.getParts();
				if ((parts != null) && (parts.size() > 0)) {
					ArrayList<Part> list = null;
					for (Part part : parts) {
						if ((part != null) && (part.getSize() > 0L)) {
							if (name.equals(part.getName())) {
								if (list == null) {
									list = new ArrayList();
								}
								list.add(part);
							}
						}
					}
					if (list != null) {
						return list.toArray(new Part[list.size()]);
					}
				}
			} catch (IOException | ServletException e) {
			}
			return null;
		}
	};
	private static final FieldParser UPLOADFILE_ARRAY_PARSER = new FieldParser() {
		public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
			String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
			if (StringHelper.isEmpty(name)) {
				name = fieldName;
			}
			try {
				Collection<Part> parts = request.getParts();
				if ((parts != null) && (parts.size() > 0)) {
					ArrayList<UploadFile> list = null;
					for (Part part : parts) {
						if ((part != null) && (part.getSize() > 0L)) {
							if (name.equals(part.getName())) {
								if (list == null) {
									list = new ArrayList();
								}
								list.add(new PartFile(part));
							}
						}
					}
					if (list != null) {
						return list.toArray(new Part[list.size()]);
					}
				}
			} catch (IOException | ServletException e) {
			}
			return null;
		}
	};
	private static final FieldParser STRING_ARRAY_PARSER = new FieldParser() {
		public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
			String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
			if (StringHelper.isEmpty(name)) {
				name = fieldName;
			}
			String[] values = request.getParameterValues(name);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					values[i] = StringHelper.trim(values[i]);
				}
			}
			return values;
		}
	};
	private static final FieldParser INTEGER_ARRAY_PARSER = new FieldParser() {
		public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
			String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
			if (StringHelper.isEmpty(name)) {
				name = fieldName;
			}
			return IntegerParser.parseArray(request.getParameterValues(name));
		}
	};
	private static final FieldParser LONG_ARRAY_PARSER = new FieldParser() {
		public Object parse(HttpServletRequest request, Map<String, String> fieldMap, String fieldName) {
			String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
			if (StringHelper.isEmpty(name)) {
				name = fieldName;
			}
			return LongParser.parseArray(request.getParameterValues(name));
		}
	};

	private static final FieldParser getFieldParser(Class<?> type) {
		FieldParser parser = (FieldParser) PARSERS.get(type);
		if (parser == null) {
			if (InputStream.class.isAssignableFrom(type)) {
				parser = INPUTSTREAM_PARSER;
			} else if (type.isArray()) {
				if (Part.class.isAssignableFrom(type)) {
					parser = PART_ARRAY_PARSER;
				} else if (UploadFile.class.isAssignableFrom(type)) {
					parser = UPLOADFILE_ARRAY_PARSER;
				} else if (String.class.isAssignableFrom(type)) {
					parser = STRING_ARRAY_PARSER;
				} else if ((Integer.TYPE == type) || (Integer.class.isAssignableFrom(type))) {
					parser = INTEGER_ARRAY_PARSER;
				} else if ((Long.TYPE == type) || (Long.class.isAssignableFrom(type))) {
					parser = LONG_ARRAY_PARSER;
				}
			}
		}
		return parser;
	}

	public void parse(HttpServletRequest request) {
		parse(request, null);
	}

	@SuppressWarnings("unchecked")
	public void parse(HttpServletRequest request, Map<String, String> fieldMap) {
		if (request == null) {
			return;
		}
		Field[] fields = getClass().getDeclaredFields();
		if ((fields == null) || (fields.length == 0)) {
			return;
		}
		for (Field field : fields) {
			String fieldName = field.getName();
			Object fieldValue = null;
			if (!Modifier.isFinal(field.getModifiers())) {
				Class<?> type = field.getType();
				if (Enum.class.isAssignableFrom(type)) {
					String name = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
					if (StringHelper.isEmpty(name)) {
						name = fieldName;
					}
					String value = request.getParameter(name);
					if (StringHelper.isEmpty(value)) {
						fieldValue = null;
					} else {
						try {
							//http://stackoverflow.com/questions/17051495/how-to-convert-string-to-enum-value-when-enum-type-reference-is-a-class
							fieldValue = Enum.valueOf((Class<Enum>)type, value);
						} catch (Throwable e) {
						}
					}
				} else if ((type.isArray()) && (AbstractEntity.class.isAssignableFrom(type.getComponentType()))) {
					Enumeration<String> parameterNames = request.getParameterNames();

					fieldName = fieldMap == null ? fieldName : (String) fieldMap.get(fieldName);
					if (StringHelper.isEmpty(fieldName)) {
						fieldName = field.getName();
					}
					StringBuilder regex = new StringBuilder();
					regex.append("^").append(fieldName).append("\\[").append("\\d").append("\\]").append("\\.[^\\.]+$");

					Pattern pattern = Pattern.compile(regex.toString());
					TreeSet<String> prefixs = new TreeSet();
					Map<String, Map<String, String>> fieldMaps = new HashMap();
					while (parameterNames.hasMoreElements()) {
						String parameterName = (String) parameterNames.nextElement();
						Matcher matcher = pattern.matcher(parameterName);
						if (matcher.find()) {
							int index = parameterName.lastIndexOf('.');
							String prefix = parameterName.substring(0, index);
							prefixs.add(parameterName.substring(0, index));
							try {
								String subFieldName = parameterName.substring(index + 1);

								type.getComponentType().getDeclaredField(subFieldName);

								Map<String, String> subFieldMap = (Map) fieldMaps.get(prefix);
								if (subFieldMap == null) {
									subFieldMap = new HashMap();
									fieldMaps.put(prefix, subFieldMap);
								}
								subFieldMap.put(subFieldName, parameterName);
							} catch (NoSuchFieldException | SecurityException e) {
							}
						}
					}
					if (prefixs.size() > 0) {
						ArrayList<AbstractEntity> values = null;
						for (String prefix : prefixs) {
							try {
								AbstractEntity entity = (AbstractEntity) type.getComponentType().newInstance();

								entity.parse(request, (Map) fieldMaps.get(prefix));
								if (values == null) {
									values = new ArrayList();
								}
								values.add(entity);
							} catch (InstantiationException | IllegalAccessException e) {
							}
						}
						int size = values == null ? 0 : values.size();
						if (size > 0) {
							fieldValue = Array.newInstance(type.getComponentType(), values.size());
							for (int i = 0; i < size; i++) {
								Array.set(fieldValue, i, values.get(i));
							}
						}
					}
				} else {
					FieldParser fieldParser = getFieldParser(type);
					if (fieldParser == null) {
						continue;
					}
					fieldValue = fieldParser.parse(request, fieldMap, field.getName());
				}
				if (fieldValue != null) {
					try {
						field.setAccessible(true);
						field.set(this, fieldValue);
					} catch (IllegalArgumentException | IllegalAccessException e) {
					}
				}
			}
		}
	}

	private static abstract interface FieldParser {
		public abstract Object parse(HttpServletRequest paramHttpServletRequest, Map<String, String> paramMap,
				String paramString);
	}
}