package org.luisyang.util;

/**
 * 对象帮助类
 */
public class ObjectHelper {
	
	public static <T> T convert(Object object, Class<T> type) {
		if ((object == null) || (type == null)) {
			return null;
		}
		if (type.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return null;
	}

	public static <T> T[] convertArray(Object object, Class<T> type) {
		if ((object == null) || (type == null)) {
			return null;
		}
		Class<?> objectClass = object.getClass();
		if ((objectClass.isArray()) && (type == objectClass.getComponentType())) {
			return (T[]) object;
		}
		return null;
	}

	public static int[] convertIntArray(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> objectClass = object.getClass();
		if ((objectClass.isArray()) && (Integer.TYPE == objectClass.getComponentType())) {
			return (int[]) object;
		}
		return null;
	}

	public static float[] convertFloatArray(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> objectClass = object.getClass();
		if ((objectClass.isArray()) && (Float.TYPE == objectClass.getComponentType())) {
			return (float[]) object;
		}
		return null;
	}

	public static double[] convertDoubleArray(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> objectClass = object.getClass();
		if ((objectClass.isArray()) && (Double.TYPE == objectClass.getComponentType())) {
			return (double[]) object;
		}
		return null;
	}

	public static short[] convertShortArray(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> objectClass = object.getClass();
		if ((objectClass.isArray()) && (Short.TYPE == objectClass.getComponentType())) {
			return (short[]) object;
		}
		return null;
	}

	public static long[] convertLongArray(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> objectClass = object.getClass();
		if ((objectClass.isArray()) && (Long.TYPE == objectClass.getComponentType())) {
			return (long[]) object;
		}
		return null;
	}

	public static boolean[] convertBooleanArray(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> objectClass = object.getClass();
		if ((objectClass.isArray()) && (Boolean.TYPE == objectClass.getComponentType())) {
			return (boolean[]) object;
		}
		return null;
	}

	public static byte[] convertByteArray(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> objectClass = object.getClass();
		if ((objectClass.isArray()) && (Byte.TYPE == objectClass.getComponentType())) {
			return (byte[]) object;
		}
		return null;
	}
}
