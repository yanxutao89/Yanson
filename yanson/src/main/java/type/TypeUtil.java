package type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidJsonValueFormatException;

public class TypeUtil {

	private static final String[] SUPPORTED_DATA_TYPES = { "string", "number", "boolean", "null" };

	private TypeUtil() throws Exception {
		throw new Exception("The constructor can not be called outside");
	}

	public static String getKey(String jsonStr) {
		String json = jsonStr.trim();
		return json.substring(1, json.length() - 1);
	}

	public static Object getValue(String jsonStr) throws InvalidJsonValueFormatException {

		String json = jsonStr.trim();

		if (json.startsWith("\"") && json.endsWith("\"")) {
			return json;
		} else if ("null".equals(json)) {
			return null;
		} else if ("true".equals(json)) {
			return true;
		} else if ("false".equals(json)) {
			return false;
		} else if (isNumeric(jsonStr)) {
			return getNumber(jsonStr);
		} 

		throw new InvalidJsonValueFormatException(
			String.format("Invalid json data type, suppored types are %s, but found %s ",  Arrays.toString(SUPPORTED_DATA_TYPES), jsonStr));
	}

	public static List<String> formatKeyValues(String jsonStr) {

		List<String> list = new ArrayList<String>();
		String json = jsonStr.trim() + ',';
		int bracketCount = 0;
		
		StringBuilder sb = new StringBuilder();
		for (char c : json.toCharArray()) {
			if (c == '{') {
				bracketCount++;
			} else if (c == '}') {
				bracketCount--;
			}

			sb.append(c);
			if (bracketCount == 0 && c == ',') {
				list.add(sb.substring(0, sb.length() - 1));
				sb = new StringBuilder();
			}
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast(Object object, Class<T> clazz) throws Exception {

		if (object != null) {
			if (clazz == String.class) {
				return (T) castToString(object);
			}
			if (clazz == boolean.class || clazz == Boolean.class) {
				return (T) castToBoolean(object);
			}
			if (clazz == int.class || clazz == Integer.class) {
				return (T) castToInteger(object);
			}
			if (clazz == long.class || clazz == Long.class) {
				return (T) castToLong(object);
			}
			if (clazz == short.class || clazz == Short.class) {
				return (T) castToShort(object);
			}
			if (clazz == BigInteger.class) {
				return (T) castToBigInteger(object);
			}
			if (clazz == float.class || clazz == Float.class) {
				return (T) castToFloat(object);
			}
			if (clazz == double.class || clazz == Double.class) {
				return (T) castToDouble(object);
			}
			if (clazz == double.class || clazz == Double.class) {
				return (T) castToDouble(object);
			}
			if (clazz == BigDecimal.class) {
				return (T) castToBigDecimal(object);
			}
			if (clazz == byte.class || clazz == Byte.class) {
				return (T) castToByte(object);
			}
			if (clazz == char.class || clazz == Character.class) {
				return (T) castToCharacter(object);
			}
		}

		return null;
	}

	public static String castToString(Object object) {
		return (String) object;
	}

	public static Boolean castToBoolean(Object object) {
		return (Boolean) object;
	}

	public static Integer castToInteger(Object object) {
		Long l = (Long) object;
		return l.intValue();
	}

	public static Long castToLong(Object object) {
		return (Long) object;
	}

	public static Short castToShort(Object object) {
		Long l = (Long) object;
		return l.shortValue();
	}

	public static BigInteger castToBigInteger(Object object) {
		return new BigInteger(object.toString());
	}

	public static Float castToFloat(Object object) {
		Double d = (Double) object;
		return d.floatValue();
	}

	public static Double castToDouble(Object object) {
		return (Double) object;
	}

	public static BigDecimal castToBigDecimal(Object object) {
		return new BigDecimal(object.toString());
	}

	public static Byte castToByte(Object object) {
		Long l = (Long) object;
		return l.byteValue();
	}

	public static Character castToCharacter(Object object) {
		return (Character) object;
	}

	public static Date castToDate(Object object) {
		return (Date) object;
	}

	private static final Set<Class<?>> PRIMITIVE_TYPE_SET = new HashSet<Class<?>>();
	private static final Set<Class<?>> ELEMENT_TYPE_SET = new HashSet<Class<?>>();
	private static final Set<Class<?>> COLLECTION_TYPE_SET = new HashSet<Class<?>>();

	static {

		PRIMITIVE_TYPE_SET.add(byte.class);
		PRIMITIVE_TYPE_SET.add(char.class);
		PRIMITIVE_TYPE_SET.add(int.class);
		PRIMITIVE_TYPE_SET.add(short.class);
		PRIMITIVE_TYPE_SET.add(long.class);
		PRIMITIVE_TYPE_SET.add(float.class);
		PRIMITIVE_TYPE_SET.add(double.class);
		PRIMITIVE_TYPE_SET.add(boolean.class);

		ELEMENT_TYPE_SET.add(String.class);
		ELEMENT_TYPE_SET.add(Byte.class);
		ELEMENT_TYPE_SET.add(Short.class);
		ELEMENT_TYPE_SET.add(Character.class);
		ELEMENT_TYPE_SET.add(Integer.class);
		ELEMENT_TYPE_SET.add(Long.class);
		ELEMENT_TYPE_SET.add(Float.class);
		ELEMENT_TYPE_SET.add(Double.class);
		ELEMENT_TYPE_SET.add(Boolean.class);
		ELEMENT_TYPE_SET.add(Date.class);
		ELEMENT_TYPE_SET.add(Class.class);
		ELEMENT_TYPE_SET.add(BigInteger.class);
		ELEMENT_TYPE_SET.add(BigDecimal.class);

		COLLECTION_TYPE_SET.add(Object[].class);
		COLLECTION_TYPE_SET.add(byte[].class);
		COLLECTION_TYPE_SET.add(char[].class);
		COLLECTION_TYPE_SET.add(int[].class);
		COLLECTION_TYPE_SET.add(short[].class);
		COLLECTION_TYPE_SET.add(long[].class);
		COLLECTION_TYPE_SET.add(float[].class);
		COLLECTION_TYPE_SET.add(double[].class);
		COLLECTION_TYPE_SET.add(boolean[].class);
		COLLECTION_TYPE_SET.add(Map.class);
		COLLECTION_TYPE_SET.add(Collection.class);
	}

	public static boolean isPrimitiveType(Class<?> clazz) {
		return PRIMITIVE_TYPE_SET.contains(clazz);
	}

	public static boolean isElementType(Class<?> clazz) {
		return ELEMENT_TYPE_SET.contains(clazz);
	}

	public static boolean isCollectionType(Class<?> clazz) {
		return COLLECTION_TYPE_SET.contains(clazz);
	}
	
	/**
	 * integer (-MAX, MAX)
	 */
	public final static String REGEX_LONG = "^[-\\+]?\\d+$"; //$NON-NLS-1$
	/**
	 * integer [1, MAX)
	 */
	public final static String REGEX_POSITIVE_LONG = "^\\+?[1-9]\\d*$"; //$NON-NLS-1$
	/**
	 * integer (-MAX, -1]
	 */
	public final static String REGEX_NEGATIVE_LONG = "^-[1-9]\\d*$"; //$NON-NLS-1$
	/**
	 * integer [0, MAX), only numeric
	 */
	public final static String REGEX_NUMERIC = "^\\d+$"; //$NON-NLS-1$
	/**
	 * decimal (-MAX, MAX)
	 */
	public final static String REGEX_DOUBLE = "^[-\\+]?\\d+\\.\\d+$"; //$NON-NLS-1$
	/**
	 * decimal (0.0, MAX)
	 */
	public final static String REGEX_POSITIVE_DOUBLE = "^\\+?([1-9]+\\.\\d+|0\\.\\d*[1-9])$"; //$NON-NLS-1$
	/**
	 * decimal (-MAX, -0.0)
	 */
	public final static String REGEX_NEGATIVE_DOUBLE = "^-([1-9]+\\.\\d+|0\\.\\d*[1-9])$"; //$NON-NLS-1$
	/**
	 * decimal + integer (-MAX, MAX)
	 */
	public final static String REGEX_REAL_NUMBER = "^[-\\+]?(\\d+|\\d+\\.\\d+)$"; //$NON-NLS-1$
	/**
	 * decimal + integer [0, MAX)
	 */
	public final static String REGEX_NON_NEGATIVE_REAL_NUMBER = "^\\+?(\\d+|\\d+\\.\\d+)$"; //$NON-NLS-1$

	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) { //$NON-NLS-1$
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}
	
	public static Object getNumber(String original) {
		
		Object object = null;
		
		object = isLong(original);
		if (null != object) {
			return (Long) object;
		}
		
		object = isDouble(original);
		if (null != object){
			return (Double)object;
		}
		
		return null;
	}

	public static boolean isNumeric(String orginal) {
		return isMatch(REGEX_NUMERIC, orginal) || isMatch(REGEX_DOUBLE, orginal);
	}

	public static Long isPositiveLong(String orginal) {
		if(isMatch(REGEX_POSITIVE_LONG, orginal)) {
			return Long.parseLong(orginal);
		}
		return null;
	}

	public static Long isNegativeLong(String orginal) {
		if(isMatch(REGEX_NEGATIVE_LONG, orginal)) {
			return Long.parseLong(orginal);
		}
		return null;
	}

	public static Long isLong(String orginal) {
		if(isMatch(REGEX_LONG, orginal)) {
			return Long.parseLong(orginal);
		}
		return null;
	}

	public static Double isPositiveDouble(String orginal) {
		if(isMatch(REGEX_POSITIVE_DOUBLE, orginal)) {
			return Double.parseDouble(orginal);
		}
		return null;
	}

	public static Double isNegativeDouble(String orginal) {
		if(isMatch(REGEX_NEGATIVE_DOUBLE, orginal)) {
			return Double.parseDouble(orginal);
		}
		return null;
	}

	public static Double isDouble(String orginal) {
		if(isMatch(REGEX_DOUBLE, orginal)) {
			return Double.parseDouble(orginal);
		}
		return null;
	}

	public static boolean isRealNumber(String orginal) {
		return isMatch(REGEX_REAL_NUMBER, orginal);
	}

	public static boolean isNonNegativeRealNumber(String orginal) {
		return isMatch(REGEX_NON_NEGATIVE_REAL_NUMBER, orginal);
	}

	public static boolean isPositiveRealNumber(String orginal) {
		return null == isPositiveDouble(orginal) || null == isPositiveLong(orginal);
	}

}
