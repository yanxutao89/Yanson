package json;

import exception.InvalidJsonFormatException;
import reflection.ClassUtil;
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
            Field[] fields = clazz.getDeclaredFields();
            Method[] methods = clazz.getDeclaredMethods();

            if (object instanceof JsonObject) {

                JsonObject jsonObject = (JsonObject) object;
                if (!StringUtils.isEmpty(parent)) {
                    jsonObject = (JsonObject) jsonObject.get(parent);
                }
                for (Field field : fields) {

                    Class<?> type = field.getType();
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {

                        String key = entry.getKey();
                        Object value = entry.getValue();
                        for (Method method : methods) {

                            if (TypeUtil.isFieldMatched(field, method, key)) {
                                method.setAccessible(true);
                                if (TypeUtil.isElementType(type)) {
                                    Class<?>[] parameterTypes = method.getParameterTypes();
                                    method.invoke(instance, TypeUtil.cast2Element(value, parameterTypes[0]));
                                } else if (TypeUtil.isCollectionType(type)){
                                    JsonArray jsonArray = (JsonArray) jsonObject.get(key);
                                    int size = jsonArray.size();

                                    Class<?> classType = null;
                                    Type genericType = field.getGenericType();
                                    if (genericType instanceof ParameterizedType) {
                                        ParameterizedType parameterizedType = (ParameterizedType) genericType;
                                        classType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                                    }

                                    Object o = TypeUtil.cast2Collection(value, type, size);
                                    if (o instanceof List) {
                                        List list = (List) o;
                                        for (Object temp : jsonArray) {
                                            list.add(toJavaObject(temp, classType, key));
                                        }
                                        method.invoke(instance, list);
                                    } else if (o instanceof Map) {
                                        Map map = (Map) o;
                                        for (Object temp : jsonArray) {
                                            map.putAll((Map) toJavaObject(temp, classType, key));
                                        }
                                        method.invoke(instance, map);
                                    }
                                } else if (TypeUtil.isArrayType(type)) {
                                    Object o = jsonObject.get(key);
                                    Object[] objects = (Object[]) o;
                                    int size = objects.length;
                                    if (o instanceof boolean[] || o instanceof Boolean[]) {
                                        Boolean[] booleans = (Boolean[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            booleans[i] = (Boolean) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{booleans});
                                    } else if (o instanceof int[] || o instanceof Integer[]) {
                                        Integer[] integers = (Integer[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            integers[i] = (Integer) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{integers});
                                    }  else if (o instanceof short[] || o instanceof Short[]) {
                                        Short[] shorts = (Short[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            shorts[i] = (Short) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{shorts});
                                    } else if (o instanceof long[] || o instanceof Long[]) {
                                        Long[] longs = (Long[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            longs[i] = (Long) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{longs});
                                    } else if (o instanceof BigInteger[]) {
                                        BigInteger[] bigIntegers = (BigInteger[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            bigIntegers[i] = (BigInteger) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{bigIntegers});
                                    } else if (o instanceof float[] || o instanceof Float[]) {
                                        Float[] floats = (Float[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            floats[i] = (Float) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{floats});
                                    } else if (o instanceof double[] || o instanceof Double[]) {
                                        Double[] doubles = (Double[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            doubles[i] = (Double) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{doubles});
                                    } else if (o instanceof BigDecimal[]) {
                                        BigDecimal[] bigDecimals = (BigDecimal[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            bigDecimals[i] = (BigDecimal) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{bigDecimals});
                                    } else if (o instanceof byte[] || o instanceof Byte[]) {
                                        Byte[] bytes = (Byte[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            bytes[i] = (Byte) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{bytes});
                                    } else if (o instanceof char[] || o instanceof Character[]) {
                                        Character[] characters = (Character[]) o;
                                        for (int i = 0; i < size; ++i) {
                                            characters[i] = (Character) objects[i];
                                        }
                                        method.invoke(instance, new Object[]{characters});
                                    } else {
                                        method.invoke(instance, new Object[]{objects});
                                    }
                                }
                                break;
                            }
                        }
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
