package yanson.json;

import yanson.exception.InvalidJsonFormatException;
import yanson.reflection.ClassUtil;
import yanson.reflection.Invoker;
import yanson.utils.CollectionUtils;
import yanson.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static yanson.json.Configuration.SET_ON_NONNULL;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/19 21:28
 */
public final class JsonHelper {

    private JsonHelper() {
        throw new UnsupportedOperationException("The constructor can not be called outside");
    }

    public static JsonObject readJson(String jsonStr, JsonObject jsonObject) {
        if (!StringUtils.isEmpty(jsonStr)) {

            String nameValue = jsonStr.trim();
            int separator = JsonUtil.indexOfColon(nameValue);
            if (-1 == separator) {
                throw new InvalidJsonFormatException(String.format("Expect name:value, but found %s", nameValue));
            }

            String currName = JsonUtil.getName(nameValue.substring(0, separator));
            String currValue = nameValue.substring(separator + 1).trim();

            // Object data
            if (JsonUtil.isObject(currValue)) {
                JsonObject currObject = (JsonObject) jsonObject.get(currName);
                if (null == currObject) {
                    currObject = new JsonObject();
                    jsonObject.put(currName, currObject);
                }

                currValue = currValue.substring(1, currValue.length() - 1);
                List<String> nameValues = JsonUtil.formatNameValues(currValue);
                for (String nv : nameValues) {
                    readJson(nv, currObject);
                }
            }

            // Array data
            else if (JsonUtil.isArray(currValue)) {

                List<String> nameValues = JsonUtil.formatNameValues(currValue);
                if (!CollectionUtils.isEmpty(nameValues)) {
                    if (Constants.ARRAY_VALUE_WITH_PRIMITIVE_TYPES.equals(nameValues.get(0))) {
                        jsonObject.put(currName, JsonUtil.getValue(nameValues.get(1)));
                    }
                    else {
                        JsonArray tempArray = (JsonArray) jsonObject.get(currName);
                        JsonArray currArray = (JsonArray) jsonObject.get(currName);

                        int arrSize = nameValues.size();
                        if (CollectionUtils.isEmpty(tempArray)) {
                            tempArray = new JsonArray(arrSize);
                            jsonObject.put(currName, tempArray);
                        }

                        if (CollectionUtils.isEmpty(currArray)) {
                            currArray = new JsonArray(arrSize);
                        }

                        for (int i = 0; i < arrSize; ++i) {
                            tempArray.add(new JsonObject());
                            StringBuilder arrayObject = new StringBuilder();
                            arrayObject.append(Constants.DOUBLE_QUOTATIONS).append(currName).append(Constants.DOUBLE_QUOTATIONS).append(Constants.COLON).append(nameValues.get(i));
                            readJson(arrayObject.toString(), (JsonObject) tempArray.get(i));
                            currArray.add(((JsonObject)((JsonArray)jsonObject.get(currName)).get(i)).get(currName));
                            if (i == arrSize - 1) {
                                jsonObject.remove(currName);
                                jsonObject.put(currName, currArray);
                            }
                        }
                    }
                }
            }

            // Others
            else {
                jsonObject.put(currName, JsonUtil.getValue(currValue));
            }
        }

        else {
            return null;
        }

        return jsonObject;
    }

    public static <T> T readJson(String jsonStr, Class<T> clazz) {
        T instance = null;
        try {
            instance = new JsonObject().fromJson(jsonStr).toJavaObject(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public static <T> T toJavaObject(Object object, Class<T> clazz, String parent) {
        T instance = null;
        try {

            instance = ClassUtil.instantiateClass(clazz, null,  null);

            if (object instanceof JsonObject) {

                JsonObject jsonObject = (JsonObject) object;
                if (!StringUtils.isEmpty(parent)) {
                    jsonObject = (JsonObject) jsonObject.get(parent);
                }

                Map<String, Invoker> invokerMap = ClassUtil.getInvokerMap(clazz);
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (invokerMap.containsKey(key)) {
                        invokerMap.get(key).setValue(instance, value);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public static String toJsonSting(Object object, boolean isList) {
        StringBuilder sb = new StringBuilder();
        if (object instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) object;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Map) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(Constants.LEFT_CURLY_BRACKET);
                    toJsonSting(value, false);
                    sb.append(Constants.RIGHT_CURLY_BRACKET);
                }
                else if (value instanceof String) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append((String) value);
                }
                else if (value instanceof Integer) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(value);
                }
                else if (value instanceof Short) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(value);
                }
                else if (value instanceof Long) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(value);
                }
                else if (value instanceof BigInteger) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(value);
                }
                else if (value instanceof Float) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(value);
                }
                else if (value instanceof Double) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(value);
                }
                else if (value instanceof BigDecimal) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(value);
                }
                else if (value instanceof Boolean) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(Constants.COLON);
                    }
                    sb.append(value);
                }
                else if (null == value) {
                    if (SET_ON_NONNULL) {
                        if (!isList) {
                            sb.append("\"" + entry.getKey() + "\"");
                            sb.append(Constants.COLON);
                        }
                    } else {

                    }
                }
                else if (value instanceof Collection) {
                    sb.append("\"" + entry.getKey() + "\"");
                    sb.append(Constants.COLON);
                    sb.append(Constants.LEFT_SQUARE_BRACKET);
                    Collection<?> collection = (Collection) value;
                    for (Object o : collection) {
                        toJsonSting(o, o instanceof Collection || o instanceof Object[]);
                    }
                    sb.append(Constants.RIGHT_CURLY_BRACKET);
                }
                sb.append(Constants.COMMA);
            }
        }
        else if (object instanceof Collection){
            Collection collection = (Collection) object;
            for (Object element : collection) {
                toJsonSting(element, false);
            }
        }
        else {
            return "";
        }

        return sb.toString();
    }

}
