package yanson.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yanson.annotation.JsonField;
import yanson.exception.InvalidTypeCastException;
import yanson.json.Constants;
import yanson.json.JsonHelper;
import yanson.utils.StringUtils;
import yanson.utils.ValidationUtils;

public final class TypeUtil {
	private TypeUtil() {
		throw new UnsupportedOperationException("The constructor can not be called outside");
	}

	/**
	 *
	 * @param valueObject current JsonObject which holds the parsed value
	 * @param targetClass target class to be initialized
	 * @param genericClasses null if the target class does not have any generic classes
	 * @param <T> target instance
	 * @return
	 */
	public static <T> T cast2Object(Object valueObject, Class<T> targetClass, Class<T>... genericClasses) {
		T instance = null;

		try {
			if (isElementType(targetClass)) {
				instance = cast2Element(valueObject, targetClass);
			}
			else if (isCollectionType(targetClass)) {
				instance = cast2Collection(valueObject, targetClass, genericClasses);
			}
			else if (isArrayType(targetClass)) {
				instance = cast2Array(valueObject, targetClass);
			}
			else {
				instance = JsonHelper.toJavaObject(valueObject, targetClass, "");
			}
		}
		catch (Exception e) {
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
	public static <T> T cast2Array(Object valueObject, Class<T> targetClass) throws Exception {
		if (valueObject != null) {
			if (targetClass == Object[].class) {
				return (T) cast2Objects(valueObject);
			}
			if (targetClass == boolean[].class || targetClass == Boolean[].class) {
				return (T) cast2Booleans(valueObject);
			}
			if (targetClass == int[].class || targetClass == Integer[].class) {
				return (T) cast2Integers(valueObject);
			}
			if (targetClass == long[].class || targetClass == Long[].class) {
				return (T) cast2Longs(valueObject);
			}
			if (targetClass == short[].class || targetClass == Short[].class) {
				return (T) cast2Shorts(valueObject);
			}
			if (targetClass == BigInteger[].class) {
				return (T) cast2BigIntegers(valueObject);
			}
			if (targetClass == float[].class || targetClass == Float[].class) {
				return (T) cast2Float(valueObject);
			}
			if (targetClass == double[].class || targetClass == Double[].class) {
				return (T) cast2Doubles(valueObject);
			}
			if (targetClass == BigDecimal[].class) {
				return (T) cast2BigDecimals(valueObject);
			}
			if (targetClass == byte[].class || targetClass == Byte[].class) {
				return (T) cast2Bytes(valueObject);
			}
			if (targetClass == char[].class || targetClass == Character[].class) {
				return (T) cast2Characters(valueObject);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast2Collection(Object valueObject, Class<T> targetClass, Class<T>... genericClasses) throws Exception {
		if (valueObject != null) {
			if (targetClass == Map.class) {
				return (T) cast2Map(valueObject);
			}
			if (targetClass == List.class || targetClass == Collection.class || targetClass == Iterable.class) {
				return (T) cast2List(valueObject, genericClasses);
			}
		}

		return null;
	}

	public static String cast2String(Object valueObject) {
		if (valueObject instanceof String) {
			return (String) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), String.class.getName());
	}

	public static Boolean cast2Boolean(Object valueObject) {
		if (valueObject instanceof Boolean) {
			return (Boolean) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Boolean.class.getName());
	}

	public static Integer cast2Integer(Object valueObject) {
		if (valueObject instanceof BigDecimal) {
			return ((BigDecimal) valueObject).intValue();
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal.class.getName());
	}

	public static Long cast2Long(Object valueObject) {
		if (valueObject instanceof BigDecimal) {
			return ((BigDecimal) valueObject).longValue();
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal.class.getName());
	}

	public static Short cast2Short(Object valueObject) {
		if (valueObject instanceof BigDecimal) {
			return ((BigDecimal) valueObject).shortValue();
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal.class.getName());
	}

	public static BigInteger cast2BigInteger(Object valueObject) {
		if (valueObject instanceof BigDecimal) {
			return ((BigDecimal) valueObject).toBigInteger();
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal.class.getName());
	}

	public static Float cast2Float(Object valueObject) {
		if (valueObject instanceof BigDecimal) {
			return ((BigDecimal) valueObject).floatValue();
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal.class.getName());
	}

	public static Double cast2Double(Object valueObject) {
		if (valueObject instanceof BigDecimal) {
			return ((BigDecimal) valueObject).doubleValue();
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal.class.getName());
	}

	public static BigDecimal cast2BigDecimal(Object valueObject) {
		if (valueObject instanceof BigDecimal) {
			return (BigDecimal) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal.class.getName());
	}

	public static Byte cast2Byte(Object valueObject) {
		if (valueObject instanceof BigDecimal) {
			return ((BigDecimal) valueObject).byteValue();
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal.class.getName());
	}

	public static Character cast2Character(Object valueObject) {
		if (valueObject instanceof Character) {
			return (Character) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Character.class.getName());
	}

	public static Date cast2Date(Object valueObject) {
		if (valueObject instanceof Date) {
			return (Date) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Date.class.getName());
	}

	public static Map cast2Map(Object valueObject) {
		if (valueObject instanceof Map) {
			return  (Map) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Map.class.getName());
	}

	public static List cast2List(Object valueObject, Class... genericClasses) {
		if (valueObject instanceof Collection) {
			Collection collection = (Collection) valueObject;
			List list = new ArrayList(collection.size());
			for (Object object : collection) {
				list.add(TypeUtil.cast2Object(object, genericClasses[0], null));
			}
			return list;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Collection.class.getName());
	}

	public static Object[] cast2Objects(Object valueObject) {
		if (valueObject instanceof Object[]) {
			return (Object[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Object[].class.getName());
	}

	public static String[] cast2Strings(Object valueObject, int size) {
		String s = valueObject.toString();
		String[] strings = s.substring(1, s.length() - 1).split(Constants.COMMA);
		return strings;
	}

	public static Boolean[] cast2Booleans(Object valueObject) {
		if (valueObject instanceof Boolean[]) {
			return (Boolean[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Boolean[].class.getName());
	}

	public static Integer[] cast2Integers(Object valueObject) {
		if (valueObject instanceof Integer[]) {
			return (Integer[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Integer[].class.getName());
	}

	public static Long[] cast2Longs(Object valueObject) {
		if (valueObject instanceof Long[]) {
			return (Long[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Long[].class.getName());
	}

	public static Short[] cast2Shorts(Object valueObject) {
		if (valueObject instanceof Short[]) {
			return (Short[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Short[].class.getName());
	}

	public static BigInteger[] cast2BigIntegers(Object valueObject) {
		if (valueObject instanceof BigInteger[]) {
			return (BigInteger[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigInteger[].class.getName());
	}

	public static Float[] cast2Floats(Object valueObject) {
		if (valueObject instanceof Float[]) {
			return (Float[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Float[].class.getName());
	}

	public static Double[] cast2Doubles(Object valueObject) {
		if (valueObject instanceof Double[]) {
			return (Double[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Double[].class.getName());
	}

	public static BigDecimal[] cast2BigDecimals(Object valueObject) {
		if (valueObject instanceof BigDecimal[]) {
			return (BigDecimal[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), BigDecimal[].class.getName());
	}

	public static Byte[] cast2Bytes(Object valueObject) {
		if (valueObject instanceof Byte[]) {
			return (Byte[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Byte[].class.getName());
	}

	public static Character[] cast2Characters(Object valueObject) {
		if (valueObject instanceof Character[]) {
			return (Character[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Character[].class.getName());
	}

	public static Date[] cast2Dates(Object valueObject) {
		if (valueObject instanceof Date[]) {
			return (Date[]) valueObject;
		}
		throw new InvalidTypeCastException(valueObject.getClass().getName(), Date[].class.getName());
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
