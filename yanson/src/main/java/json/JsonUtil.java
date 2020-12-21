package json;

import exception.InvalidJsonFormatException;
import type.TypeUtil;
import utils.StringUtils;
import utils.ValidationUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static json.Constants.*;
import static type.TypeUtil.getNumber;
import static type.TypeUtil.isNumeric;

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
public class JsonUtil {

    private JsonUtil() {

    }

    public static int indexOfColon(String jsonStr) {

        int index = -1;

        if (StringUtils.isEmpty(jsonStr)) {
            return index;
        }

        index = jsonStr.indexOf(COLON);
        if (index != -1) {
            String nameToCheck = jsonStr.substring(0, index);
            String valueToCheck = jsonStr.substring(index + 1);
            while (!(isValidJsonName(nameToCheck) && isValidJsonValue(valueToCheck))) {
                index = jsonStr.indexOf(COLON, index);
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
        return null != name;
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

        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")
                || value.startsWith(LEFT_CURLY_BRACKET) && value.endsWith(RIGHT_CURLY_BRACKET)
                || value.startsWith(LEFT_SQUARE_BRACKET) && value.endsWith(RIGHT_SQUARE_BRACKET)
                || "null".equals(value)
                || "true".equals(value)
                || "false".equals(value)
                || isNumber(value)) {
            return true;
        }

        return false;
    }

    /**
     * check whether the json is array or object
     * @param jsonStr the json text to be checked
     * @return true if array false if object
     * @throws Exception if the json text is neither array nor object
     */
    public static boolean isArray(String jsonStr) throws Exception {
        ValidationUtils.isTrue(StringUtils.isNotEmpty(jsonStr), "Parameter 'jsonStr' must not be null or empty");
        jsonStr = jsonStr.trim();
        if (jsonStr.startsWith(LEFT_CURLY_BRACKET) && jsonStr.endsWith(RIGHT_CURLY_BRACKET)) {
            return false;
        }
        if (jsonStr.startsWith(LEFT_SQUARE_BRACKET) && jsonStr.endsWith(RIGHT_SQUARE_BRACKET)) {
            return true;
        }
        throw new InvalidJsonFormatException(String.format("Expected %s or %s at position 0, but found &s", LEFT_CURLY_BRACKET, LEFT_CURLY_BRACKET, jsonStr.charAt(0)));
    }

    public static String getName(String jsonStr) {
        jsonStr = jsonStr.trim();
        return jsonStr.substring(1, jsonStr.length() - 1);
    }

    public static Object getValue(String jsonStr) throws InvalidJsonFormatException {

        jsonStr = jsonStr.trim();

        if (jsonStr.startsWith("\"") && jsonStr.endsWith("\"")) {
            if (jsonStr.startsWith("\"\"")) {
                jsonStr = jsonStr.substring(1);
            }
            if (jsonStr.endsWith("\"\"")) {
                jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
            }
            return jsonStr.substring(1, jsonStr.length() - 1);
        } else if ("null".equals(jsonStr)) {
            return null;
        } else if ("true".equals(jsonStr)) {
            return true;
        } else if ("false".equals(jsonStr)) {
            return false;
        } else if (isNumber(jsonStr)) {
            return getNumber(jsonStr);
        } else if (jsonStr.startsWith(LEFT_SQUARE_BRACKET) && jsonStr.endsWith(RIGHT_SQUARE_BRACKET)) {
            String[] strings = jsonStr.substring(1, jsonStr.length() - 1).split(",");
            if (strings != null && strings.length > 0) {
                Object[] objects = new Object[strings.length];
                for (int i = 0; i < strings.length; ++i) {
                    objects[i] = strings[i];
                }
                return objects;
            } else {
                return new Object[0];
            }
        }

        throw new InvalidJsonFormatException(String.format("Invalid json value type, supported types are %s, but found %s ",  Arrays.toString(SUPPORTED_VALUE_TYPES), jsonStr));
    }

    private static Class determineType4Array(String[] strings) {

        return Object.class;
    }

    public static List<String> formatNameValues(String jsonStr) {

        List<String> nameValues = new ArrayList<String>();
        if (isArrayEmptyOrSeparatedByComma(jsonStr)) {

            nameValues.add(ARRAY_VALUE_WITH_PRIMITIVE_TYPES);
            nameValues.add(jsonStr);
        } else {

            jsonStr = jsonStr.trim();
            if (jsonStr.startsWith(LEFT_SQUARE_BRACKET) && jsonStr.endsWith(RIGHT_SQUARE_BRACKET)) {
                jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
            }
            jsonStr = jsonStr + ',';
            int curlyBracketCount = 0;
            int squareBracketCount = -1;

            StringBuilder sb = new StringBuilder();
            for (char c : jsonStr.toCharArray()) {
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

    private static boolean isArrayEmptyOrSeparatedByComma(String jsonStr) {

        jsonStr = jsonStr.trim();
        if (jsonStr.startsWith(LEFT_SQUARE_BRACKET) && jsonStr.endsWith(RIGHT_SQUARE_BRACKET)) {

            jsonStr = jsonStr.substring(1, jsonStr.length() - 1).trim();
            return jsonStr.charAt(0) != '{' && jsonStr.charAt(jsonStr.length() - 1) != '}';
        }

        return false;
    }

    private static boolean isNumber(String jsonStr) {
        return TypeUtil.isRealNumber(jsonStr);
    }

    private static BigDecimal getNumber(String jsonStr) {
        return new BigDecimal(jsonStr);
    }

    public static void main(String[] args) {
        String colon = "\"\\\"1\\\"\": 1";
        System.out.println(indexOfColon(colon));
        String comma = "\",";
        System.out.println(comma.length());
    }

}
