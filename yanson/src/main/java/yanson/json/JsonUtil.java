package yanson.json;

import yanson.exception.InvalidJsonFormatException;
import yanson.type.TypeUtil;
import yanson.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JSON can represent four primitive types (strings, numbers, booleans, and null) and two structured types (objects and arrays).
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/9/2 19:39
 */
public final class JsonUtil {

    private JsonUtil() {
        throw new UnsupportedOperationException("The constructor can not be called outside");
    }

    /**
     * Check whether the json is object or not. An object is an unordered collection of zero or more name/value pairs,
     * where a name is a string and a value is a string, number, boolean, null, object, or array.
     * @param json the json text to be checked
     * @return true if object otherwise false
     */
    public static boolean isObject(String json) {
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        json = json.trim();
        return json.startsWith(Constants.LEFT_CURLY_BRACKET) && json.endsWith(Constants.RIGHT_CURLY_BRACKET);
    }

    /**
     * Check whether the json is array or not. An array is an ordered sequence of zero or more values.
     * @param json the json text to be checked
     * @return true if array otherwise false
     */
    public static boolean isArray(String json) {
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        json = json.trim();
        return json.startsWith(Constants.LEFT_SQUARE_BRACKET) && json.endsWith(Constants.RIGHT_SQUARE_BRACKET);
    }

    public static int indexOfColon(String json) {
        if (StringUtils.isEmpty(json)) {
            return -1;
        }
        json = json.trim();
        int index = json.indexOf(Constants.COLON);
        if (index != -1) {
            String nameToCheck = json.substring(0, index).trim();
            String valueToCheck = json.substring(index + 1).trim();
            while (!(isValidJsonName(nameToCheck) && isValidJsonValue(valueToCheck))) {
                index = json.indexOf(Constants.COLON, index);
                nameToCheck = json.substring(0, index);
                valueToCheck = json.substring(index + 1);
            }
        }
        return index;
    }

    public static int indexOfComma(String json) {
        return -1;
    }

    public static int indexOfBrace(String json) {
        if (StringUtils.isEmpty(json)) {
            return -1;
        }
        return -1;
    }

    public static int indexOfBracket(String json) {
        if (StringUtils.isEmpty(json)) {
            return -1;
        }
        return -1;
    }

    /**
     *
     * @param name
     * @return
     */
    public static boolean isValidJsonName(String name) {
        return isString(name);
    }

    /**
     * A JSON value MUST be an string, object, array, or number, or one of the following three literal names:false true null
     * The literal names MUST be lowercase. No other literal names are allowed.
     * value = false / null / true / object / array / number / string
     * false = %x66.61.6c.73.65 ; false
     * null  = %x6e.75.6c.6c ; null
     * true  = %x74.72.75.65 ; true
     * @param value
     * @return
     */
    public static boolean isValidJsonValue(String value) {
        return isString(value) || isObject(value) || isArray(value) || isNumber(value) || isBoolean(value) || isNull(value);
    }

    public static String getName(String json) {
        return json.substring(1, json.length() - 1);
    }

    public static Object getValue(String json) throws InvalidJsonFormatException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        json = json.trim();
        if (isNull(json)) {
            return getNull(json);
        } else if (isString(json)) {
            while (isMarkedWithDoubleQuotations(json)) {
                json = json.substring(1, json.length() - 1);
            }
            return json;
        } else if (isNumber(json)) {
            return getNumber(json);
        } else if (isBoolean(json)) {
            return getBoolean(json);
        } else if (isArray(json)) {
            String[] array = json.substring(1, json.length() - 1).split(Constants.COMMA);
            if (array != null && array.length > 0) {
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; ++i) {
                    objects[i] = getValue(array[i]);
                }
                return objects;
            } else {
                return new Object[0];
            }
        }
        throw new InvalidJsonFormatException(String.format("Invalid json value type, supported types are %s, but found %s ",  Arrays.toString(Constants.SUPPORTED_VALUE_TYPES), json));
    }

    public static <T> T getValue(String json, Class<T> clazz) throws InvalidJsonFormatException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        if (isString(json)) {
            while (isMarkedWithDoubleQuotations(json)) {
                json = json.substring(1, json.length() - 1);
            }
            return castString(json, clazz);
        } else if (isNull(json)) {
            return castString(json, clazz);
        } else if (isNumber(json)) {
            return castString(json, clazz);
        } else if (isArray(json)) {
            String[] array = json.substring(1, json.length() - 1).split(",");
            if (array != null && array.length > 0) {
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; ++i) {
                    objects[i] = getValue(array[i]);
                }
                return (T)objects;
            } else {
                return (T)new Object[0];
            }
        }
        throw new InvalidJsonFormatException(String.format("Invalid json value type, supported types are %s, but found %s ",  Arrays.toString(Constants.SUPPORTED_VALUE_TYPES), json));
    }

    private static <T> T castString(String json, Class<T> clazz){
        T instance = null;
        return instance;
    }

    private static Class determineType4Array(String[] strings) {
        return Object.class;
    }

    public static List<String> formatNameValues(String json) {
        List<String> nameValues = new ArrayList<>();
        if (isArrayEmptyOrSeparatedByComma(json)) {
            nameValues.add(Constants.ARRAY_VALUE_WITH_PRIMITIVE_TYPES);
            nameValues.add(json);
        } else {
            if (isArray(json)) {
                json = json.substring(1, json.length() - 1);
            }
            json = json + Constants.COMMA;
            int curlyBracketCount = 0;
            int squareBracketCount = -1;

            StringBuilder sb = new StringBuilder();
            for (char c : json.toCharArray()) {
                if (c == '{') {
                    curlyBracketCount++;
                } else if (c == '}') {
                    curlyBracketCount--;
                } else if (c == '[') {
                    if (squareBracketCount == -1) {
                        squareBracketCount = 0;
                    }
                    squareBracketCount++;
                } else if (c == ']') {
                    squareBracketCount--;
                }
                sb.append(c);
                if (c == ',' && curlyBracketCount == 0 && (squareBracketCount == -1 || squareBracketCount == 0)) {
                    nameValues.add(sb.substring(0, sb.length() - 1));
                    sb = new StringBuilder();
                }
            }
        }
        return nameValues;
    }

    private static boolean isArrayEmptyOrSeparatedByComma(String json) {
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        json = json.trim();
        if (isArray(json)) {
            json = json.substring(1, json.length() - 1).trim();
            if (isArray(json)) {
                return isMarkedWithDoubleQuotations(json);
            } else {
                return !isObject(json);
            }
        }
        return false;
    }

    /**
     * A string is a sequence of zero or more Unicode characters [UNICODE].
     * @param json
     * @return
     */
    private static boolean isString(String json) {
        return isMarkedWithDoubleQuotations(json);
    }

    private static boolean isMarkedWithDoubleQuotations(String json) {
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        json = json.trim();
        return json.startsWith(Constants.DOUBLE_QUOTATIONS) && json.endsWith(Constants.DOUBLE_QUOTATIONS);
    }

    private static boolean isNumber(String json) {
        return TypeUtil.isRealNumber(json);
    }

    public static BigDecimal getNumber(String json) {
        return isNumber(json) ? new BigDecimal(json) : null;
    }

    public static <T> T getNumber(String json, Class<T> clazz) {
        BigDecimal number = getNumber(json);
        if (null != number) {
            if (clazz == byte.class || clazz == Byte.class) {
                return (T) (Byte) number.byteValue();
            }
            if (clazz == short.class || clazz == Short.class) {
                return (T) (Short) number.shortValue();
            }
            if (clazz == int.class || clazz == Integer.class) {
                return (T) (Integer) number.intValue();
            }
            if (clazz == long.class || clazz == Long.class) {
                return (T) (Long) number.longValue();
            }
            if (clazz == float.class || clazz == Float.class) {
                return (T) (Float) number.floatValue();
            }
            if (clazz == double.class || clazz == Double.class) {
                return (T) (Double) number.doubleValue();
            }
            if (clazz == BigInteger.class) {
                return (T) number.toBigInteger();
            }
            if (clazz == BigDecimal.class) {
                return (T) number;
            }
        }
        return null;
    }

    public static boolean isBoolean(String json) {
        return "true".equals(json) || "false".equals(json);
    }

    private static boolean getBoolean(String json) {
        return isBoolean(json) ? Boolean.valueOf(json) : null;
    }

    public static boolean isNull(String json) {
        return null == json || "null".equals(json);
    }

    public static Object getNull(String json) {
        if (isNull(json)) {
            return null;
        }
        throw new InvalidJsonFormatException(String.format("Expected 'null', but found %s", json));
    }

}
