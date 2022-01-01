package yanson.json;

import yanson.exception.InvalidJsonFormatException;
import yanson.reflection.ClassUtil;
import yanson.reflection.Invoker;
import yanson.reflection.InvokerType;
import yanson.type.TypeUtil;
import yanson.utils.CollectionUtils;
import yanson.utils.StringUtils;

import java.util.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/19 21:28
 */
public final class JsonHelper {

    private JsonHelper() {
        throw new UnsupportedOperationException("The constructor can not be called outside");
    }

    public static JsonObject readJson(String json, JsonObject jsonObject) {
        if (!StringUtils.isEmpty(json)) {
            json = json.trim();
            int separator = JsonUtil.indexOfColon(json);
            if (-1 == separator) {
                throw new InvalidJsonFormatException(String.format("Expect name:value, but found %s", json));
            }
            String currName = JsonUtil.getName(json.substring(0, separator).trim());
            String currValue = json.substring(separator + 1).trim();

            if (JsonUtil.isObject(currValue)) {
                JsonObject currObject = (JsonObject) jsonObject.get(currName);
                if (null == currObject) {
                    currObject = new JsonObject();
                    jsonObject.put(currName, currObject);
                }
                currValue = currValue.substring(1, currValue.length() - 1);
                List<String> nameValues = JsonUtil.formatNameValues(currValue.trim());
                for (String nv : nameValues) {
                    readJson(nv, currObject);
                }
            } else if (JsonUtil.isArray(currValue)) {
                List<String> nameValues = JsonUtil.formatNameValues(currValue.trim());
                if (!CollectionUtils.isEmpty(nameValues)) {
                    if (Constants.ARRAY_VALUE_WITH_PRIMITIVE_TYPES.equals(nameValues.get(0))) {
                        jsonObject.put(currName, JsonUtil.getValue(nameValues.get(1).trim()));
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
            } else {
                jsonObject.put(currName, JsonUtil.getValue(currValue.trim()));
            }
        } else {
            return null;
        }
        return jsonObject;
    }

    public static <T> T readJson(String json, Class<T> clazz) {
        T instance = null;
        try {
            instance = new JsonObject().fromJson(json).toJavaObject(clazz);
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
                Map<String, Invoker> invokerMap = ClassUtil.getInvokerMap(targetClass, InvokerType.ALL);
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
    public static String toJsonSting(Object object, StringBuilder json) {
        if (object == null) {
            json.append("null");
        } else {
            Class<?> objectClass = object.getClass();
            if ((objectClass.isArray() && !objectClass.getComponentType().isPrimitive()) || object instanceof Collection) {
                json.append(Constants.LEFT_SQUARE_BRACKET);
            }
            if (object instanceof Map) {
                json.append(Constants.LEFT_CURLY_BRACKET);
            }
            if (object instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) object;
                ArrayList<Map.Entry<String, Object>> entries = new ArrayList<>(map.entrySet());
                for (int i = 0; i < entries.size(); ++i) {
                    Map.Entry<String, Object> entry = entries.get(i);
                    json.append(Constants.DOUBLE_QUOTATIONS)
                            .append(entry.getKey())
                            .append(Constants.DOUBLE_QUOTATIONS)
                            .append(Constants.COLON);
                    toJsonSting(entry.getValue(), json);
                    if (i < entries.size() - 1) {
                        json.append(Constants.COMMA);
                    }
                }
            } else if (objectClass.isArray()) {
                if (object instanceof Object[]) {
                    Object[] array = (Object[]) object;
                    for (int i = 0; i < array.length; ++i) {
                        toJsonSting(array[i], json);
                        if (i < array.length - 1) {
                            json.append(Constants.COMMA);
                        }
                    }
                } else if (objectClass.getComponentType().isPrimitive()) {
                    Class<?> componentType = objectClass.getComponentType();
                    if (componentType == boolean.class) {
                        json.append(Arrays.toString((boolean[])object));
                    } else if (componentType == byte.class) {
                        json.append(Arrays.toString((byte[])object));
                    } else if (componentType == char.class) {
                        json.append(Arrays.toString((char[]) object));
                    } else if (componentType == short.class) {
                        json.append(Arrays.toString((short[]) object));
                    } else if (componentType == int.class) {
                        json.append(Arrays.toString((int[]) object));
                    } else if (componentType == long.class) {
                        json.append(Arrays.toString((long[]) object));
                    } else if (componentType == float.class) {
                        json.append(Arrays.toString((float[]) object));
                    } else if (componentType == double.class) {
                        json.append(Arrays.toString((double[]) object));
                    }
                }
            } else if (object instanceof List) {
                List list = (List) object;
                for (int i = 0; i < list.size(); ++i) {
                    toJsonSting(list.get(i), json);
                    if (i < list.size() - 1) {
                        json.append(Constants.COMMA);
                    }
                }
            } else if (TypeUtil.isElementType(objectClass)) {
                if (object instanceof String) {
                    json.append(Constants.DOUBLE_QUOTATIONS);
                }
                json.append(object);
                if (object instanceof String) {
                    json.append(Constants.DOUBLE_QUOTATIONS);
                }
            } else {
                Map<String, Invoker> invokerMap = ClassUtil.getInvokerMap(objectClass, InvokerType.FIELD);
                if (invokerMap.size() == 0) {
                    json.append("\"" + object.toString() + "\"");
                } else {
                    JsonObject jsonObject = new JsonObject();
                    for (Map.Entry<String, Invoker> entry : invokerMap.entrySet()) {
                        jsonObject.put(entry.getKey(), entry.getValue().getValue(object));
                    }
                    toJsonSting(jsonObject, json);
                }
            }
            if (object instanceof Map) {
                json.append(Constants.RIGHT_CURLY_BRACKET);
            }
            if ((objectClass.isArray() && !objectClass.getComponentType().isPrimitive()) || object instanceof Collection) {
                json.append(Constants.RIGHT_SQUARE_BRACKET);
            }
        }
        return json.toString();
    }

}
