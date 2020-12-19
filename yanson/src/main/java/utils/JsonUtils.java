package utils;

import exception.InvalidJsonFormatException;
import type.TypeUtil;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/9/2 19:39
 */
public class JsonUtils {

    private static final String COMMA = ":";
    private static final String OPEN_BRACE = "{";
    private static final String CLOSED_BRACE = "}";
    private static final String OPEN_BRACKET = "[";
    private static final String CLOSED_BRACKET = "]";

    public static int indexOfColon(String str){

        int index = -1;

        if (StringUtils.isEmpty(str)) {
            return index;
        }

        String[] commas = str.split(COMMA);
        if (commas.length < 2) {
            return index;
        } else {
            for (int i = 1; i < commas.length; ++i) {
                if (isValidJsonKey(commas[i - 1]) && isValidJsonValue(commas[i])) {
                    return commas[i - 1].length();
                }
            }
        }

        return -1;

    }

    public static int indexOfComma(String str) {

        return -1;
    }

    public static int indexOfBrace(String str){

        if (StringUtils.isEmpty(str)) {
            return -1;
        }

        return -1;
    }

    public static int indexOfBracket(String str){

        if (StringUtils.isEmpty(str)) {
            return -1;
        }

        return -1;
    }

    public static boolean isValidJsonKey(String keyToJudge){
        return null != keyToJudge;
    }

    public static boolean isValidJsonValue(String keyToJudge){

        String json = keyToJudge.trim();
        if (json.startsWith("\"") && json.endsWith("\"")
                || json.startsWith("{") && json.endsWith("}")
                || json.startsWith("[") && json.endsWith("]")
                || "null".equals(json)
                || "true".equals(json)
                || "false".equals(json)
                || TypeUtil.isNumeric(json)) {
            return true;
        }

        return false;
    }

    /**
     * check whether json is a array or not
     * @param json
     * @return true if json is a array otherwise false
     */
    public static boolean isArray(String json) throws Exception {
        ValidationUtils.isTrue(StringUtils.isNotEmpty(json), "Parameter 'json' must not be null or empty");
        json = json.trim();
        if (json.startsWith(OPEN_BRACE) && json.endsWith(CLOSED_BRACE)) {
            return false;
        }
        if (json.startsWith(OPEN_BRACKET) && json.endsWith(CLOSED_BRACKET)) {
            return true;
        }
        throw new InvalidJsonFormatException(String.format("Expected %s or %s at position 0, but found &s", OPEN_BRACE, OPEN_BRACKET, json.charAt(0)));
    }

    public static void main(String[] args) {
        String colon = "\"\\\"1\\\"\": 1";
        System.out.println(indexOfColon(colon));
        String comma = "\",";
        System.out.println(comma.length());
    }

}
