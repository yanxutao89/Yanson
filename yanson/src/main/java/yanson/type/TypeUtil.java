package yanson.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yanson.annotation.JsonField;
import yanson.json.Constants;
import yanson.json.JsonHelper;
import yanson.utils.StringUtils;
import yanson.utils.ValidationUtils;

public class TypeUtil {

	private TypeUtil() {
		throw new UnsupportedOperationException("The constructor can not be called outside");
	}

	public static <T> T cast2Object(Object valueObject, Class<T> targetClass, Class<T>... genericClasses) {
		T instance = null;

		try {

			if (isElementType(targetClass)) {
				instance = cast2Element(valueObject, targetClass);
			}
			else if (TypeUtil.isCollectionType(targetClass)) {
				instance = cast2Collection(valueObject, 16, targetClass, genericClasses);
			}
			else if (TypeUtil.isArrayType(targetClass)) {
				instance = cast2Array(valueObject, targetClass, 16);
			}
			else {
				instance = JsonHelper.toJavaObject(valueObject, targetClass, "");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast2Element(Object valueObject, Class<T> targetClass) throws Exception {

		if (valueObject != null) {
			if (targetClass == String.class) {
				return (T) cast2String(valueObject);
			}
			if (targetClass == boolean.class || targetClass == Boolean.class) {
				return (T) cast2Boolean(valueObject);
			}
			if (targetClass == int.class || targetClass == Integer.class) {
				return (T) cast2Integer(valueObject);
			}
			if (targetClass == short.class || targetClass == Short.class) {
				return (T) cast2Short(valueObject);
			}
			if (targetClass == long.class || targetClass == Long.class) {
				return (T) cast2Long(valueObject);
			}
			if (targetClass == BigInteger.class) {
				return (T) cast2BigInteger(valueObject);
			}
			if (targetClass == float.class || targetClass == Float.class) {
				return (T) cast2Float(valueObject);
			}
			if (targetClass == double.class || targetClass == Double.class) {
				return (T) cast2Double(valueObject);
			}
			if (targetClass == BigDecimal.class) {
				return (T) cast2BigDecimal(valueObject);
			}
			if (targetClass == byte.class || targetClass == Byte.class) {
				return (T) cast2Byte(valueObject);
			}
			if (targetClass == char.class || targetClass == Character.class) {
				return (T) cast2Character(valueObject);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast2Array(Object valueObject, Class<T> targetClass, int size) throws Exception {

		if (valueObject != null) {
			if (targetClass == Object[].class) {
				return (T) cast2Objects(valueObject, size);
			}
			if (targetClass == boolean[].class || targetClass == Boolean[].class) {
				return (T) cast2Booleans(valueObject, size);
			}
			if (targetClass == int[].class || targetClass == Integer[].class) {
				return (T) cast2Integers(valueObject, size);
			}
			if (targetClass == long[].class || targetClass == Long[].class) {
				return (T) cast2Longs(valueObject, size);
			}
			if (targetClass == short[].class || targetClass == Short[].class) {
				return (T) cast2Shorts(valueObject, size);
			}
			if (targetClass == BigInteger[].class) {
				return (T) cast2BigIntegers(valueObject, size);
			}
			if (targetClass == float[].class || targetClass == Float[].class) {
				return (T) cast2Float(valueObject);
			}
			if (targetClass == double[].class || targetClass == Double[].class) {
				return (T) cast2Doubles(valueObject, size);
			}
			if (targetClass == BigDecimal[].class) {
				return (T) cast2BigDecimals(valueObject, size);
			}
			if (targetClass == byte[].class || targetClass == Byte[].class) {
				return (T) cast2Bytes(valueObject, size);
			}
			if (targetClass == char[].class || targetClass == Character[].class) {
				return (T) cast2Characters(valueObject, size);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast2Collection(Object valueObject, int size, Class<T> targetClass, Class<T>... genericClasses) throws Exception {

		if (valueObject != null) {
			if (targetClass == Map.class) {
				return (T) cast2Map(valueObject, size);
			}
			if (targetClass == List.class || targetClass == Collection.class || targetClass == Iterable.class) {
				return (T) cast2List(valueObject, size, genericClasses);
			}
		}

		return null;
	}

	public static String cast2String(Object valueObject) {
		return valueObject.toString();
	}

	public static Boolean cast2Boolean(Object valueObject) {
		return Boolean.valueOf(valueObject.toString());
	}

	public static Integer cast2Integer(Object valueObject) {
		BigDecimal bd = new BigDecimal(valueObject.toString());
		return bd.intValue();
	}

	public static Long cast2Long(Object valueObject) {
		BigDecimal bd = new BigDecimal(valueObject.toString());
		return bd.longValue();
	}

	public static Short cast2Short(Object valueObject) {
		BigDecimal bd = new BigDecimal(valueObject.toString());
		return bd.shortValue();
	}

	public static BigInteger cast2BigInteger(Object valueObject) {
		BigDecimal bd = new BigDecimal(valueObject.toString());
		return bd.toBigInteger();
	}

	public static Float cast2Float(Object valueObject) {
		BigDecimal bd = new BigDecimal(valueObject.toString());
		return bd.floatValue();
	}

	public static Double cast2Double(Object valueObject) {
		BigDecimal bd = new BigDecimal(valueObject.toString());
		return bd.doubleValue();
	}

	public static BigDecimal cast2BigDecimal(Object valueObject) {
		return new BigDecimal(valueObject.toString());
	}

	public static Byte cast2Byte(Object valueObject) {
		BigDecimal bd = new BigDecimal(valueObject.toString());
		return bd.byteValue();
	}

	public static Character cast2Character(Object valueObject) {
		return (Character) valueObject;
	}

	public static Date cast2Date(Object valueObject) {
		return (Date) valueObject;
	}

	public static Map cast2Map(Object valueObject, int size) {
		return new HashMap(size);
	}

	public static List cast2List(Object valueObject, int size, Class... genericClasses) {
		if (valueObject instanceof List) {
			List objects = (List) valueObject;
			List list = new ArrayList(objects.size());
			for (Object o : objects) {
				Object o2 = TypeUtil.cast2Object(o, genericClasses[0], null);
				list.add(o2);
			}
			return list;
		}
		return new ArrayList(size);
	}

	public static Object[] cast2Objects(Object valueObject, int size) {
		if (valueObject instanceof Object[]) {
			return (Object[]) valueObject;
		}
		return null;
	}

	public static String[] cast2Strings(Object valueObject, int size) {
		String s = valueObject.toString();
		String[] strings = s.substring(1, s.length() - 1).split(Constants.COMMA);
		return strings;
	}

	public static Boolean[] cast2Booleans(Object valueObject, int size) {
		Boolean[] booleans = new Boolean[size];
		return booleans;
	}

	public static Integer[] cast2Integers(Object valueObject, int size) {
		Integer[] integers = new Integer[size];
		return integers;
	}

	public static Long[] cast2Longs(Object valueObject, int size) {
		Long[] longs = new Long[size];
		return longs;
	}

	public static Short[] cast2Shorts(Object valueObject, int size) {
		Short[] shorts = new Short[size];
		return shorts;
	}

	public static BigInteger[] cast2BigIntegers(Object valueObject, int size) {
		BigInteger[] bigIntegers = new BigInteger[size];
		return bigIntegers;
	}

	public static Float[] cast2Floats(Object valueObject, int size) {
		Float[] floats = new Float[size];
		return floats;
	}

	public static Double[] cast2Doubles(Object valueObject, int size) {
		Double[] doubles = new Double[size];
		return doubles;
	}

	public static BigDecimal[] cast2BigDecimals(Object valueObject, int size) {
		BigDecimal[] bigDecimals = new BigDecimal[size];
		return bigDecimals;
	}

	public static Byte[] cast2Bytes(Object valueObject, int size) {
		Byte[] bytes = new Byte[size];
		return bytes;
	}

	public static Character[] cast2Characters(Object valueObject, int size) {
		Character[] characters = new Character[size];
		return characters;
	}

	public static Date[] cast2Dates(Object valueObject, int size) {
		Date[] dates = new Date[size];
		return dates;
	}

	public static boolean isFieldMatched(Field field, Method method, String name) throws Exception {

		ValidationUtils.isTrue(!StringUtils.isEmpty(name), "Parameter 'name' must not be null or empty");

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
