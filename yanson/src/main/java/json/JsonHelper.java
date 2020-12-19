package json;

import exception.InvalidJsonFormatException;
import type.TypeUtil;
import utils.CollectionUtils;
import utils.StringUtils;

import java.util.List;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/19 21:28
 */
public class JsonHelper {

    public static void readJson(JsonObject jsonObject, String json) {

        if (!StringUtils.isEmpty(json) && !"isArrayEmptyOrSeparatedByComma".equals(json)) {

            String kvStr = json.trim();
            int separator = kvStr.indexOf(":");
            if (-1 == separator) {
                throw new InvalidJsonFormatException(String.format("expect key:value, but found %s", kvStr));
            }

            String keyStr = kvStr.substring(0, separator);
            String currKey = TypeUtil.getKey(keyStr);
            String valueStr = kvStr.substring(separator + 1).trim();

            // Object data
            if (valueStr.startsWith("{") && valueStr.endsWith("}")) {

                JsonObject currObject = (JsonObject) jsonObject.get(currKey);
                if (null == currObject) {
                    currObject = new JsonObject();
                    jsonObject.put(currKey, currObject);
                }

                valueStr = valueStr.substring(1, valueStr.length() - 1);
                List<String> keyValues = TypeUtil.formatKeyValues(valueStr);
                for (String keyValue : keyValues) {

                    readJson(currObject, keyValue);
                }

            // Array data
            } else if (valueStr.startsWith("[") && valueStr.endsWith("]")) {

                List<String> keyValues = TypeUtil.formatKeyValues(valueStr);

                if (!CollectionUtils.isEmpty(keyValues)) {

                    if ("isArrayEmptyOrSeparatedByComma".equals(keyValues.get(0))) {

                        jsonObject.put(currKey, TypeUtil.getValue(keyValues.get(1)));
                    } else {

                        JsonArray currArray = (JsonArray) jsonObject.get(currKey);
                        if (CollectionUtils.isEmpty(currArray)) {
                            currArray = new JsonArray(keyValues.size());
                            jsonObject.put(currKey, currArray);
                        }

                        for (int i = 0; i < keyValues.size(); i++) {

                            currArray.add(new JsonObject());
                            StringBuilder arrayObject = new StringBuilder();
                            arrayObject.append("\"").append(currKey).append("\"").append(":").append(keyValues.get(i));
                            readJson((JsonObject) currArray.get(i), arrayObject.toString());
                        }
                    }
                }

            // Non object data
            } else {
                jsonObject.put(currKey, TypeUtil.getValue(valueStr));
            }
        } else {
            return ;
        }

    }

}
