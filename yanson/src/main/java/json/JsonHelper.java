package json;

import exception.InvalidJsonFormatException;
import reflection.ClassUtil;
import reflection.Invoker;
import type.TypeUtil;
import utils.CollectionUtils;
import utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static json.Configuration.SET_ON_NONNULL;
import static json.Constants.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/19 21:28
 */
public class JsonHelper {

    public static JsonObject readJson(String jsonStr, JsonObject jsonObject) {

        if (!StringUtils.isEmpty(jsonStr) && !ARRAY_VALUE_WITH_PRIMITIVE_TYPES.equals(jsonStr)) {

            String nameValue = jsonStr.trim();
            int separator = JsonUtil.indexOfColon(nameValue);
            if (-1 == separator) {
                throw new InvalidJsonFormatException(String.format("Expect name:value, but found %s", nameValue));
            }

            String name = nameValue.substring(0, separator);
            String currName = JsonUtil.getName(name);
            String value = nameValue.substring(separator + 1).trim();

            // Object data
            if (value.startsWith(LEFT_CURLY_BRACKET) && value.endsWith(RIGHT_CURLY_BRACKET)) {

                JsonObject currObject = (JsonObject) jsonObject.get(currName);
                if (null == currObject) {
                    currObject = new JsonObject();
                    jsonObject.put(currName, currObject);
                }

                value = value.substring(1, value.length() - 1);
                List<String> nameValues = JsonUtil.formatNameValues(value);
                for (String nv : nameValues) {
                    readJson(nv, currObject);
                }

            // Array data
            } else if (value.startsWith(LEFT_SQUARE_BRACKET) && value.endsWith(RIGHT_SQUARE_BRACKET)) {

                List<String> nameValues = JsonUtil.formatNameValues(value);
                if (!CollectionUtils.isEmpty(nameValues)) {

                    if (ARRAY_VALUE_WITH_PRIMITIVE_TYPES.equals(nameValues.get(0))) {
                        jsonObject.put(currName, JsonUtil.getValue(nameValues.get(1)));
                    } else {
                        JsonArray currArray = (JsonArray) jsonObject.get(currName);
                        if (CollectionUtils.isEmpty(currArray)) {
                            currArray = new JsonArray(nameValues.size());
                            jsonObject.put(currName, currArray);
                        }

                        for (int i = 0; i < nameValues.size(); ++i) {
                            currArray.add(new JsonObject());
                            StringBuilder arrayObject = new StringBuilder();
                            arrayObject.append("\"").append(currName).append("\"").append(COLON).append(nameValues.get(i));
                            readJson(arrayObject.toString(), (JsonObject) currArray.get(i));
                        }
                    }
                }
            // Non object data
            } else {
                jsonObject.put(currName, JsonUtil.getValue(value));
            }
        } else {
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
                        sb.append(COLON);
                    }
                    sb.append(LEFT_CURLY_BRACKET);
                    toJsonSting(value, false);
                    sb.append(RIGHT_CURLY_BRACKET);
                } else if (value instanceof String) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append((String) value);
                } else if (value instanceof Integer) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append(value);
                } else if (value instanceof Short) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append(value);
                } else if (value instanceof Long) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append(value);
                } else if (value instanceof BigInteger) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append(value);
                } else if (value instanceof Float) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append(value);
                } else if (value instanceof Double) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append(value);
                } else if (value instanceof BigDecimal) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append(value);
                } else if (value instanceof Boolean) {
                    if (!isList) {
                        sb.append("\"" + entry.getKey() + "\"");
                        sb.append(COLON);
                    }
                    sb.append(value);
                } else if (null == value) {
                    if (SET_ON_NONNULL) {
                        if (!isList) {
                            sb.append("\"" + entry.getKey() + "\"");
                            sb.append(COLON);
                        }
                    } else {

                    }
                } else if (value instanceof Collection) {
                    sb.append("\"" + entry.getKey() + "\"");
                    sb.append(COLON);
                    sb.append(LEFT_SQUARE_BRACKET);
                    Collection<?> collection = (Collection) value;
                    for (Object o : collection) {
                        toJsonSting(o, o instanceof Collection || o instanceof Object[]);
                    }
                    sb.append(RIGHT_CURLY_BRACKET);
                }
                sb.append(COMMA);
            }
        } else if (object instanceof Collection){
            Collection collection = (Collection) object;
            for (Object element : collection) {
                toJsonSting(element, false);
            }
        } else {
            return "";
        }

        return sb.toString();

    }

}
