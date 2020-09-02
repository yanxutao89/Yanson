package json;

import type.TypeUtil;
import utils.StringUtils;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/9/2 19:39
 */
public class JsonUtil {

    private static final String COMMA = ":";
    private static final String OPEN_BRACE = "{";
    private static final String CLOSE_BRACE = "}";
    private static final String OPEN_BRACKET = "[";
    private static final String CLOSE_BRACKET = "]";

    public static int indexOfComma(String jsonStr){

        int index = -1;

        if (StringUtils.isEmpty(jsonStr)) {
            return index;
        }

        String[] commas = jsonStr.split(COMMA);
        if (commas.length < 2) {
            return index;
        } else if (commas.length == 2) {
            return commas[0].length();
        } else {
            for (int i = 1; i < commas.length; ++i) {
                if (isValidJsonKey(commas[i - 1]) && isValidJsonValue(commas[i])) {
                    return commas[i - 1].length();
                }
            }
        }

        return -1;

    }

    public static int indexOfBrace(String jsonStr){


        if (StringUtils.isEmpty(jsonStr)) {
            return -1;
        }

        return -1;
    }

    public static int indexOfBracket(String jsonStr){

        if (StringUtils.isEmpty(jsonStr)) {
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
            || "null".equals(json)
            || "true".equals(json)
            || "false".equals(json)
            || TypeUtil.isNumeric(json)) {
        }

        return false;
    }

}
