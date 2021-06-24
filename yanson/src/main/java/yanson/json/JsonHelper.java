package yanson.json;

import yanson.exception.InvalidJsonFormatException;
import yanson.reflection.ClassUtil;
import yanson.reflection.Invoker;
import yanson.type.TypeUtil;
import yanson.utils.CollectionUtils;
import yanson.utils.StringUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
    public static <T> T toJavaObject(Object valueObject, Class<T> targetClass, String parent) {
        T instance = null;
        try {

            instance = ClassUtil.instantiateClass(targetClass, null,  null);

            if (valueObject instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) valueObject;
                if (!StringUtils.isEmpty(parent)) {
                    jsonObject = (JsonObject) jsonObject.get(parent);
                }

                Map<String, Invoker> invokerMap = ClassUtil.getInvokerMap(targetClass);
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (invokerMap.containsKey(key)) {
                        invokerMap.get(key).setValue(instance, value);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public static String toJsonSting(Object object, StringBuilder jsonStr) {
        if (object == null) {
            jsonStr.append("null");
        }
        else {
            if (object.getClass().isArray() || object instanceof Collection) {
                jsonStr.append(Constants.LEFT_SQUARE_BRACKET);
            }
            if (object instanceof Map) {
                jsonStr.append(Constants.LEFT_CURLY_BRACKET);
            }
            if (object instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) object;
                ArrayList<Map.Entry<String, Object>> entries = new ArrayList<>(map.entrySet());
                for (int i = 0; i < entries.size(); ++i) {
                    Map.Entry<String, Object> entry = entries.get(i);
                    jsonStr.append(Constants.DOUBLE_QUOTATIONS)
                            .append(entry.getKey())
                            .append(Constants.DOUBLE_QUOTATIONS)
                            .append(Constants.COLON);
                    toJsonSting(entry.getValue(), jsonStr);
                    if (i < entries.size() - 1) {
                        jsonStr.append(Constants.COMMA);
                    }
                }
            }
            else if (object.getClass().isArray()) {
                Object[] array = (Object[]) object;
                for (int i = 0; i < array.length; ++i) {
                    toJsonSting(array[i], jsonStr);
                    if (i < array.length - 1) {
                        jsonStr.append(Constants.COMMA);
                    }
                }
            }
            else if (object instanceof List) {
                List list = (List) object;
                for (int i = 0; i < list.size(); ++i) {
                    toJsonSting(list.get(i), jsonStr);
                    if (i < list.size() - 1) {
                        jsonStr.append(Constants.COMMA);
                    }
                }
            }
            else if (TypeUtil.isElementType(object.getClass())) {
                if (object instanceof String) {
                    jsonStr.append(Constants.DOUBLE_QUOTATIONS);
                }
                jsonStr.append(object);
                if (object instanceof String) {
                    jsonStr.append(Constants.DOUBLE_QUOTATIONS);
                }
            }
            if (object instanceof Map) {
                jsonStr.append(Constants.RIGHT_CURLY_BRACKET);
            }
            if (object.getClass().isArray() || object instanceof Collection) {
                jsonStr.append(Constants.RIGHT_SQUARE_BRACKET);
            }
        }
        return jsonStr.toString();
    }
}
