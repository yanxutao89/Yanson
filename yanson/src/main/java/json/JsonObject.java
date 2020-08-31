package json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.InvalidJsonKeyValueFormatException;
import type.TypeUtil;
import utils.CollectionUtils;
import utils.PatternUtils;
import utils.StringUtils;

public class JsonObject extends HashMap<String, Object> {

	private static final long serialVersionUID = 4560188633954957114L;

	private static final boolean SET_ON_NONULL = true;
	private static final String MAGIC = "luxkui";

	public static Object parseObject(String jsonStr) {

		JsonObject jsonObject = new JsonObject();
		StringBuffer sb = new StringBuffer();
		boolean isArray = false;

		String json = jsonStr.trim();
		if (json.startsWith("[") && json.endsWith("]")) {
			isArray = true;
		}
		sb.append("\"").append(MAGIC).append("\"").append(":").append(json);

		try {
			generateObject(jsonObject, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonObject retObj = (JsonObject) jsonObject.get(MAGIC);
		return isArray == false ? retObj : (JsonArray) retObj.get(MAGIC);
	}

	private static void generateObject(JsonObject jsonObject, String jsonStr) throws Exception {

		if (!StringUtils.isEmpty(jsonStr)) {

			String kvStr = jsonStr.trim();
			int separator = kvStr.indexOf(":");
			if (-1 == separator) {
				throw new InvalidJsonKeyValueFormatException(String.format("expect key:value, but found %s", kvStr));
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

					generateObject(currObject, keyValue);
				}

			// Array data
			} else if (valueStr.startsWith("[") && valueStr.endsWith("]")) {

				List<String> keyValues = TypeUtil.formatKeyValues(valueStr.substring(1, valueStr.length() - 1));

				if (!CollectionUtils.isEmpty(keyValues)) {

					JsonArray currArray = (JsonArray) jsonObject.get(currKey);
					if (CollectionUtils.isEmpty(currArray)) {
						currArray = new JsonArray(keyValues.size());
						jsonObject.put(currKey, currArray);
					}

					for (int i = 0; i < keyValues.size(); i++) {

						currArray.add(new JsonObject());
						StringBuilder arrayObject = new StringBuilder();
						arrayObject.append("\"").append(currKey).append("\"").append(":").append(keyValues.get(i));
						generateObject((JsonObject) currArray.get(i), arrayObject.toString());
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

	public static <T> T toJavaObject(String jsonStr, Class<T> clazz) throws Exception {
		return ((JsonObject) parseObject(jsonStr)).toJavaObject(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> T toJavaObject(Class<T> clazz) throws Exception {

		Object instance = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();

		for (Map.Entry<String, Object> entry : this.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			for (Field field : fields) {
				if (key.equals(field.getName())) {
					for (Method method : methods) {
						String name = method.getName();
						if (name.startsWith("set") && name.toLowerCase().contains(key.toLowerCase())) {
							method.setAccessible(true);
							Class<?>[] parameterTypes = method.getParameterTypes();
							method.invoke(instance, TypeUtil.cast(value, parameterTypes[0]));
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

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		recursive2String(sb, this, false);
		sb.append("}");
		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), "}");
		return PatternUtils.commaRightSquareBracket(str, "]");

	}

	@SuppressWarnings("unchecked")
	private void recursive2String(StringBuffer sb, Object object, boolean isList) {

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
					sb.append((Integer) value);
				} else if (value instanceof Short) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((Short) value);
				} else if (value instanceof Long) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((Long) value);
				} else if (value instanceof BigInteger) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((BigInteger) value);
				} else if (value instanceof Float) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((Float) value);
				} else if (value instanceof Double) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((Double) value);
				} else if (value instanceof BigDecimal) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((BigDecimal) value);
				} else if (value instanceof Boolean) {
					if (!isList) {
						sb.append("\"" + entry.getKey() + "\"");
						sb.append(":");
					}
					sb.append((Boolean) value);
				} else if (null == value) {
					if (SET_ON_NONULL) {
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
