package type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import annotation.JsonField;
import exception.InvalidJsonFormatException;
import utils.StringUtils;

import static json.Constants.SUPPORTED_VALUE_TYPES;
import static utils.ValidationUtils.isTrue;

public class TypeUtil {

	private TypeUtil() {
		throw new UnsupportedOperationException("The constructor can not be called outside");
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast2Element(Object object, Class<T> clazz) throws Exception {

		if (object != null) {
			if (clazz == String.class) {
				return (T) cast2String(object);
			}
			if (clazz == boolean.class || clazz == Boolean.class) {
				return (T) cast2Boolean(object);
			}
			if (clazz == int.class || clazz == Integer.class) {
				return (T) cast2Integer(object);
			}
			if (clazz == short.class || clazz == Short.class) {
				return (T) cast2Short(object);
			}
			if (clazz == long.class || clazz == Long.class) {
				return (T) cast2Long(object);
			}
			if (clazz == BigInteger.class) {
				return (T) cast2BigInteger(object);
			}
			if (clazz == float.class || clazz == Float.class) {
				return (T) cast2Float(object);
			}
			if (clazz == double.class || clazz == Double.class) {
				return (T) cast2Double(object);
			}
			if (clazz == BigDecimal.class) {
				return (T) cast2BigDecimal(object);
			}
			if (clazz == byte.class || clazz == Byte.class) {
				return (T) cast2Byte(object);
			}
			if (clazz == char.class || clazz == Character.class) {
				return (T) cast2Character(object);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast2Array(Object object, Class<T> clazz, int size) throws Exception {

		if (object != null) {
			if (clazz == Object[].class) {
				return (T) cast2Strings(object, size);
			}
			if (clazz == boolean[].class || clazz == Boolean[].class) {
				return (T) cast2Booleans(object, size);
			}
			if (clazz == int[].class || clazz == Integer[].class) {
				return (T) cast2Integers(object, size);
			}
			if (clazz == long[].class || clazz == Long[].class) {
				return (T) cast2Longs(object, size);
			}
			if (clazz == short[].class || clazz == Short[].class) {
				return (T) cast2Shorts(object, size);
			}
			if (clazz == BigInteger[].class) {
				return (T) cast2BigIntegers(object, size);
			}
			if (clazz == float[].class || clazz == Float[].class) {
				return (T) cast2Float(object);
			}
			if (clazz == double[].class || clazz == Double[].class) {
				return (T) cast2Doubles(object, size);
			}
			if (clazz == BigDecimal[].class) {
				return (T) cast2BigDecimals(object, size);
			}
			if (clazz == byte[].class || clazz == Byte[].class) {
				return (T) cast2Bytes(object, size);
			}
			if (clazz == char[].class || clazz == Character[].class) {
				return (T) cast2Characters(object, size);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast2Collection(Object object, Class<T> clazz, int size) throws Exception {

		if (object != null) {
			if (clazz == Map.class) {
				return (T) cast2Map(object, size);
			}
			if (clazz == List.class || clazz == Collection.class || clazz == Iterable.class) {
				return (T) cast2List(object, size);
			}
		}

		return null;
	}

	public static String cast2String(Object object) {
		return (String) object;
	}

	public static Boolean cast2Boolean(Object object) {
		return (Boolean) object;
	}

	public static Integer cast2Integer(Object object) {
		BigDecimal bd = (BigDecimal) object;
		return bd.intValue();
	}

	public static Long cast2Long(Object object) {
		BigDecimal bd = (BigDecimal) object;
		return bd.longValue();
	}

	public static Short cast2Short(Object object) {
		BigDecimal bd = (BigDecimal) object;
		return bd.shortValue();
	}

	public static BigInteger cast2BigInteger(Object object) {
		BigDecimal bd = (BigDecimal) object;
		return bd.toBigInteger();
	}

	public static Float cast2Float(Object object) {
		BigDecimal bd = (BigDecimal) object;
		return bd.floatValue();
	}

	public static Double cast2Double(Object object) {
		BigDecimal bd = (BigDecimal) object;
		return bd.doubleValue();
	}

	public static BigDecimal cast2BigDecimal(Object object) {
		return new BigDecimal(object.toString());
	}

	public static Byte cast2Byte(Object object) {
		BigDecimal bd = (BigDecimal) object;
		return bd.byteValue();
	}

	public static Character cast2Character(Object object) {
		return (Character) object;
	}

	public static Date cast2Date(Object object) {
		return (Date) object;
	}

	public static Map cast2Map(Object object, int size) {
		return new HashMap(size);
	}

	public static List cast2List(Object object, int size) {
		return new ArrayList(size);
	}

	public static String[] cast2Strings(Object object, int size) {
		String[] strings = new String[size];
		return strings;
	}

	public static Boolean[] cast2Booleans(Object object, int size) {
		Boolean[] booleans = new Boolean[size];
		return booleans;
	}

	public static Integer[] cast2Integers(Object object, int size) {
		Integer[] integers = new Integer[size];
		return integers;
	}

	public static Long[] cast2Longs(Object object, int size) {
		Long[] longs = new Long[size];
		return longs;
	}

	public static Short[] cast2Shorts(Object object, int size) {
		Short[] shorts = new Short[size];
		return shorts;
	}

	public static BigInteger[] cast2BigIntegers(Object object, int size) {
		BigInteger[] bigIntegers = new BigInteger[size];
		return bigIntegers;
	}

	public static Float[] cast2Floats(Object object, int size) {
		Float[] floats = new Float[size];
		return floats;
	}

	public static Double[] cast2Doubles(Object object, int size) {
		Double[] doubles = new Double[size];
		return doubles;
	}

	public static BigDecimal[] cast2BigDecimals(Object object, int size) {
		BigDecimal[] bigDecimals = new BigDecimal[size];
		return bigDecimals;
	}

	public static Byte[] cast2Bytes(Object object, int size) {
		Byte[] bytes = new Byte[size];
		return bytes;
	}

	public static Character[] cast2Characters(Object object, int size) {
		Character[] characters = new Character[size];
		return characters;
	}

	public static Date[] cast2Dates(Object object, int size) {
		Date[] dates = new Date[size];
		return dates;
	}

	public static boolean isFieldMatched(Field field, Method method, String name) throws Exception {

		isTrue(!StringUtils.isEmpty(name), "Parameter 'name' must not be null or empty");

		boolean isMatched = name.equals(field.getName());
		JsonField jsonField = field.getAnnotation(JsonField.class);
		if (null != jsonField) {
			List<String> aliasKeys = Arrays.asList(jsonField.aliasNames());
			String propertyName = jsonField.value();
			isMatched |= aliasKeys.contains(name) && propertyName.equals(field.getName());
		}

		if (isMatched) {
			String methodName = method.getName();
			if ((methodName.startsWith("set") || methodName.startsWith("is")) && methodName.toLowerCase().contains(name.toLowerCase())) {
				return true;
			}
		}

		return false;

	}

	public static boolean hasSetterMethod(Class<?> clazz, String property) {
		return false;
	}

	public static String capitalizesFirstLetter(String property) {

		if (StringUtils.isEmpty(property)) {
			return "";
		}
		if (Character.isLowerCase(property.charAt(0))) {
			property = Character.toUpperCase(property.charAt(0)) + property.substring(1);
		}

		return property;
	}

	private static final Set<Class<?>> PRIMITIVE_TYPE_SET = new HashSet<Class<?>>();
	private static final Set<Class<?>> ELEMENT_TYPE_SET = new HashSet<Class<?>>();
	private static final Set<Class<?>> ARRAY_TYPE_SET = new HashSet<Class<?>>();
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

		ARRAY_TYPE_SET.add(Object[].class);
		ARRAY_TYPE_SET.add(String[].class);
		ARRAY_TYPE_SET.add(byte[].class);
		ARRAY_TYPE_SET.add(char[].class);
		ARRAY_TYPE_SET.add(int[].class);
		ARRAY_TYPE_SET.add(short[].class);
		ARRAY_TYPE_SET.add(long[].class);
		ARRAY_TYPE_SET.add(float[].class);
		ARRAY_TYPE_SET.add(double[].class);
		ARRAY_TYPE_SET.add(boolean[].class);

		COLLECTION_TYPE_SET.add(Map.class);
		COLLECTION_TYPE_SET.add(List.class);
		COLLECTION_TYPE_SET.add(Collection.class);
		COLLECTION_TYPE_SET.add(Iterable.class);

	}

	public static boolean isPrimitiveType(Class<?> clazz) {
		return clazz.isPrimitive() || PRIMITIVE_TYPE_SET.contains(clazz);
	}

	public static boolean isElementType(Class<?> clazz) {
		return isPrimitiveType(clazz) || ELEMENT_TYPE_SET.contains(clazz);
	}

	public static boolean isArrayType(Class<?> clazz) {
		return clazz.isArray() || null != clazz.getComponentType() || ARRAY_TYPE_SET.contains(clazz);
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

	private static boolean isMatch(String regex, String original) {
		if (original == null || original.trim().equals("")) { //$NON-NLS-1$
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(original);
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

	public static boolean isNumeric(String original) {
		return isMatch(REGEX_NUMERIC, original) || isMatch(REGEX_NEGATIVE_LONG, original) || isMatch(REGEX_DOUBLE, original) || isMatch(REGEX_NEGATIVE_DOUBLE, original);
	}

	public static Long isPositiveLong(String original) {
		if(isMatch(REGEX_POSITIVE_LONG, original)) {
			return Long.parseLong(original);
		}
		return null;
	}

	public static Long isNegativeLong(String original) {
		if(isMatch(REGEX_NEGATIVE_LONG, original)) {
			return Long.parseLong(original);
		}
		return null;
	}

	public static Long isLong(String original) {
		if(isMatch(REGEX_LONG, original)) {
			return Long.parseLong(original);
		}
		return null;
	}

	public static Double isPositiveDouble(String original) {
		if(isMatch(REGEX_POSITIVE_DOUBLE, original)) {
			return Double.parseDouble(original);
		}
		return null;
	}

	public static Double isNegativeDouble(String original) {
		if(isMatch(REGEX_NEGATIVE_DOUBLE, original)) {
			return Double.parseDouble(original);
		}
		return null;
	}

	public static Double isDouble(String original) {
		if(isMatch(REGEX_DOUBLE, original)) {
			return Double.parseDouble(original);
		}
		return null;
	}

	public static boolean isRealNumber(String original) {
		return isMatch(REGEX_REAL_NUMBER, original);
	}

	public static boolean isNonNegativeRealNumber(String original) {
		return isMatch(REGEX_NON_NEGATIVE_REAL_NUMBER, original);
	}

	public static boolean isPositiveRealNumber(String original) {
		return null == isPositiveDouble(original) || null == isPositiveLong(original);
	}

}
