package json;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import type.TypeUtil;
import utils.*;

import static json.Configuration.MAGIC;
import static json.Configuration.SET_ON_NONNULL;

public class JsonObject extends HashMap<String, Object> implements JsonParser<JsonObject> {

	private static final long serialVersionUID = 4560188633954957114L;

	public JsonObject fromJson(String json) {

        try {
            ValidationUtils.isTrue(!JsonUtils.isArray(json), String.format("Expect object, but found array"));
            StringBuilder sb = new StringBuilder();
            sb.append("\"").append(MAGIC).append("\"").append(":").append(json.trim());
            JsonObject jsonObject = new JsonObject();
            JsonHelper.readJson(jsonObject, json);
            return (JsonObject) jsonObject.get(MAGIC);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonObject();

	}

	public String toJson(JsonObject o) {
		return null;
	}

    public <T> T toJavaObject(String jsonStr, Class<T> clazz) throws Exception {
		return (fromJson(jsonStr)).toJavaObject0(this, clazz, "");
	}

	@SuppressWarnings("unchecked")
	public <T> T toJavaObject0(Object object, Class<T> clazz, String parent) throws Exception {

		Object instance = clazz.newInstance();
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
										list.add(toJavaObject0(temp, classType, key));
									}
									method.invoke(instance, list);
								} else if (o instanceof Map) {
									Map map = (Map) o;
									for (Object temp : jsonArray) {
										map.putAll((Map) toJavaObject0(temp, classType, key));
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

		return (T) instance;
	}

	@Override
	public String toString() {
		return this.toJsonString();
	}

	public String toJsonString() {

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		recursive2String(sb, this, false);
		sb.append("}");
		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), "}");
		return PatternUtils.commaRightSquareBracket(str, "]");

	}

	@SuppressWarnings("unchecked")
	private void recursive2String(StringBuilder sb, Object object, boolean isList) {

		if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) object;
			for (Entry<String, Object> entry : map.entrySet()) {
				Object value = entry.getValue();
				if (value instanceof Map) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append("{");
					recursive2String(sb, value, false);
					sb.append("}");
				} else if (value instanceof String) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((String) value);
				} else if (value instanceof Integer) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Short) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Long) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof BigInteger) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Float) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Double) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof BigDecimal) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (value instanceof Boolean) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append(value);
				} else if (null == value) {
					if (SET_ON_NONNULL) {
						if (!isList) {
							sb.append("\"" + entry.getKey() + "\"");
							sb.append(":");
						}
					} else {

					}
				} else if (value instanceof List) {
					sb.append("\"" + entry.getKey() + "\"");
					sb.append(":");
					sb.append("[");
					List<?> list = (List<?>) value;
					for (Object o : list) {
						recursive2String(sb, o, true);
					}
					sb.append("]");
				}
				sb.append(",");
			}
		} else {
			return;
		}
	}

}
