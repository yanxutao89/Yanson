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
 * A string is a sequence of zero or more Unicode characters [UNICODE].
 * An object is an unordered collection of zero or more name/value pairs, where a name is a string and a value is a string, number, boolean, null, object, or array.
 * An array is an ordered sequence of zero or more values.
 * The terms "object" and "array" come from the conventions of JavaScript, which is referenced from rfc4627
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/9/2 19:39
 */
public final class JsonUtil {

    private JsonUtil() {
        throw new UnsupportedOperationException("The constructor can not be called outside");
    }

    public static int indexOfColon(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return -1;
        }

        int index = jsonStr.indexOf(Constants.COLON);
        if (index != -1) {
            String nameToCheck = jsonStr.substring(0, index);
            String valueToCheck = jsonStr.substring(index + 1);
            while (!(isValidJsonName(nameToCheck) && isValidJsonValue(valueToCheck))) {
                index = jsonStr.indexOf(Constants.COLON, index);
                nameToCheck = jsonStr.substring(0, index);
                valueToCheck = jsonStr.substring(index + 1);
            }
        }

        return index;
    }

    public static int indexOfComma(String jsonStr) {

        return -1;
    }

    public static int indexOfBrace(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return -1;
        }

        return -1;
    }

    public static int indexOfBracket(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
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
     * A JSON value MUST be an object, array, number, or string, or one of the following three literal names:false null true
     * The literal names MUST be lowercase. No other literal names are allowed.
     * value = false / null / true / object / array / number / string
     * false = %x66.61.6c.73.65 ; false
     * null  = %x6e.75.6c.6c ; null
     * true  = %x74.72.75.65 ; true
     * @param value
     * @return
     */
    public static boolean isValidJsonValue(String value) {
        return isString(value) || isNumber(value) || isBoolean(value) || isNull(value) || isObject(value) || isArray(value);
    }

    public static String getName(String jsonStr) {
        jsonStr = jsonStr.trim();
        return jsonStr.substring(1, jsonStr.length() - 1);
    }

    public static Object getValue(String jsonStr) throws InvalidJsonFormatException {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }

        jsonStr = jsonStr.trim();

        if (isString(jsonStr)) {
            while (isMarkedWithDoubleQuotations(jsonStr)) {
                jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
            }
            return jsonStr;
        }
        else if (isNull(jsonStr)) {
            return getNull(jsonStr);
        }
        else if (isNumber(jsonStr)) {
            return getNumber(jsonStr);
        }
        else if (isArray(jsonStr)) {
            String[] strings = jsonStr.substring(1, jsonStr.length() - 1).split(",");
            if (strings != null && strings.length > 0) {
                Object[] objects = new Object[strings.length];
                for (int i = 0; i < strings.length; ++i) {
                    objects[i] = strings[i];
                }
                return objects;
            }
            else {
                return new Object[0];
            }
        }

        throw new InvalidJsonFormatException(String.format("Invalid com.json value com.type, supported types are %s, but found %s ",  Arrays.toString(Constants.SUPPORTED_VALUE_TYPES), jsonStr));
    }

    public static <T> T getValue(String jsonStr, Class<T> clazz) throws InvalidJsonFormatException {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }

        jsonStr = jsonStr.trim();

        if (isString(jsonStr)) {
            while (isMarkedWithDoubleQuotations(jsonStr)) {
                jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
            }
            return castString(jsonStr, clazz);
        }
        else if (isNull(jsonStr)) {
            return castString(jsonStr, clazz);
        }
        else if (isNumber(jsonStr)) {
            return castString(jsonStr, clazz);
        }
        else if (isArray(jsonStr)) {
            String[] strings = jsonStr.substring(1, jsonStr.length() - 1).split(",");
            if (strings != null && strings.length > 0) {
                Object[] objects = new Object[strings.length];
                for (int i = 0; i < strings.length; ++i) {
                    objects[i] = strings[i];
                }
                return castString(jsonStr, clazz);
            }
            else {
                return castString(jsonStr, clazz);
            }
        }

        throw new InvalidJsonFormatException(String.format("Invalid com.json value com.type, supported types are %s, but found %s ",  Arrays.toString(Constants.SUPPORTED_VALUE_TYPES), jsonStr));
    }

    private static <T> T castString(String jsonStr, Class<T> clazz){
        T instance = null;

        return instance;
    }

    private static Class determineType4Array(String[] strings) {
        return Object.class;
    }

    public static List<String> formatNameValues(String jsonStr) {
        List<String> nameValues = new ArrayList<String>();

        if (isArrayEmptyOrSeparatedByComma(jsonStr)) {
            nameValues.add(Constants.ARRAY_VALUE_WITH_PRIMITIVE_TYPES);
            nameValues.add(jsonStr);
        }
        else {
            jsonStr = jsonStr.trim();
            if (isArray(jsonStr)) {
                jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
            }
            jsonStr = jsonStr + Constants.COMMA;
            int curlyBracketCount = 0;
            int squareBracketCount = -1;

            StringBuilder sb = new StringBuilder();
            for (char c : jsonStr.toCharArray()) {
                if (c == '{') {
                    curlyBracketCount++;
                }
                else if (c == '}') {
                    curlyBracketCount--;
                }
                else if (c == '[') {
                    if (squareBracketCount == -1) {
                        squareBracketCount = 0;
                    }
                    squareBracketCount++;
                }
                else if (c == ']') {
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

    private static boolean isArrayEmptyOrSeparatedByComma(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return false;
        }

        jsonStr = jsonStr.trim();
        if (isArray(jsonStr)) {
            jsonStr = jsonStr.substring(1, jsonStr.length() - 1).trim();
            return !isObject(jsonStr);
        }

        return false;
    }

    private static boolean isString(String jsonStr) {
        return StringUtils.isNotEmpty(jsonStr);
    }

    private static boolean isMarkedWithDoubleQuotations(String jsonStr) {
        return jsonStr.startsWith(Constants.DOUBLE_QUOTATIONS) && jsonStr.endsWith(Constants.DOUBLE_QUOTATIONS);
    }

    private static boolean isNumber(String jsonStr) {
        return TypeUtil.isRealNumber(jsonStr);
    }

    public static BigDecimal getNumber(String jsonStr) {
        return isNumber(jsonStr) ? new BigDecimal(jsonStr) : null;
    }

    public static <T> T getNumber(String jsonStr, Class<T> clazz) {
        BigDecimal number = getNumber(jsonStr);
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

    public static boolean isBoolean(String jsonStr) {
        return "true".equals(jsonStr) || "false".equals(jsonStr);
    }

    private static boolean getBoolean(String jsonStr) {
        return isBoolean(jsonStr) ? Boolean.valueOf(jsonStr) : null;
    }

    public static boolean isNull(String jsonStr) {
        return "null".equals(jsonStr);
    }

    public static Object getNull(String jsonStr) {
        if (isNull(jsonStr)) {
            return null;
        }
        throw new InvalidJsonFormatException(String.format("Expected 'null', but found %s", jsonStr));
    }

    /**
     * check whether the com.json is object or not
     * @param jsonStr the com.json text to be checked
     * @return true if object otherwise false
     * @throws Exception if the com.json text is object or not
     */
    public static boolean isObject(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return false;
        }

        jsonStr = jsonStr.trim();
        return jsonStr.startsWith(Constants.LEFT_CURLY_BRACKET) && jsonStr.endsWith(Constants.RIGHT_CURLY_BRACKET);
    }

    /**
     * check whether the com.json is array or not
     * @param jsonStr the com.json text to be checked
     * @return true if array otherwise false
     * @throws Exception if the com.json text is array or not
     */
    public static boolean isArray(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return false;
        }

        return jsonStr.startsWith(Constants.LEFT_SQUARE_BRACKET) && jsonStr.endsWith(Constants.RIGHT_SQUARE_BRACKET);
    }

    public static void main(String[] args) {
        String colon = "\"\\\"1\\\"\": 1";
        System.out.println(indexOfColon(colon));

        String comma = "\",";
        System.out.println(comma.length());
    }

}
