//package json;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import type.TypeUtil;
//import utils.PatternUtils;
//
//public class JsonObject00 extends HashMap<String, Object> {
//
//	private static final long serialVersionUID = 4560188633954957114L;
//	private static final boolean SET_ON_NONULL = false;
//	private static final String MAGIC = "luxkui";
//
//	public static Object parseObject(String jsonStr) {
//
//		JsonObject00 jsonObject = new JsonObject00();
//		StringBuffer sb = new StringBuffer();
//		boolean isArray = false;
//		String json = jsonStr.trim();
//		if (json.startsWith("[") && json.endsWith("]")) {
//			isArray = true;
//		}
//		sb.append("\"").append(MAGIC).append("\"").append(":").append(json);
//
//		try {
//			generateObject(jsonObject, sb.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		JsonObject00 retObj = (JsonObject00) jsonObject.get(MAGIC);
//		return isArray == false ? retObj : (json.JsonArray) retObj.get(MAGIC);
//	}
//
//	public static <T> T toJavaObject(String jsonStr, Class<T> clazz) throws Exception {
//
//		return ((JsonObject00) parseObject(jsonStr)).toJavaObject(clazz);
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> T toJavaObject(Class<T> clazz) throws Exception {
//
//		Object instance = clazz.newInstance();
//
//		Field[] fields = clazz.getDeclaredFields();
//
//		Method[] methods = clazz.getDeclaredMethods();
//
//		for (Map.Entry<String, Object> entry : this.entrySet()) {
//			String key = entry.getKey();
//			Object value = entry.getValue();
//			for (Field field : fields) {
//				if (key.equals(field.getName())) {
//					for (Method method : methods) {
//						String name = method.getName();
//						if (name.startsWith("set") && name.toLowerCase().contains(key.toLowerCase())) {
//							method.setAccessible(true);
//							Class<?>[] parameterTypes = method.getParameterTypes();
//							method.invoke(instance, TypeUtil.cast2Element(value, parameterTypes[0]));
//						}
//					}
//				}
//			}
//		}
//
//		return (T) instance;
//	}
//
//	private static void generateObject(JsonObject00 jsonObject, String jsonStr) throws Exception {
//
//		if (null != jsonStr && jsonStr.length() > 0) {
//
//			String kvStr = jsonStr.trim();
//			int separator = kvStr.indexOf(":");
//			if (-1 == separator) {
//				throw new Exception("InvalidJsonkeyValueFormatExpection, expect key:value, but found " + kvStr);
//			}
//
//			String keyStr = kvStr.substring(0, separator);
//			String valueStr = kvStr.substring(separator + 1).trim();
//			String currKey = getKey(keyStr);
//			JsonObject00 currObject = (JsonObject00) jsonObject.get(currKey);
//
//			if (null == currObject) {
//				currObject = new JsonObject00();
//				jsonObject.put(currKey, currObject);
//			}
//
//			if (valueStr.startsWith("[") && valueStr.endsWith("]")) { // Array data
//
//				List<String> keyValues = formatKVs(valueStr);
//				for (String keyValue : keyValues) {
//					keyStr = "\"" + currKey + "\"";
//					List<String> kvs = formatKVs(valueStr.substring(1, keyValue.length() - 1));
//					json.JsonArray currArray = new json.JsonArray(kvs.size());
//					currObject.put(currKey, currArray);
//
//					for (int i = 0; i < kvs.size(); i++) {
//						currArray.add(new JsonObject00());
//						StringBuffer arrayObject = new StringBuffer();
//						arrayObject.append("\"").append(currKey).append("\"").append(":").append(kvs.get(i));
//						generateObject((JsonObject00) currArray.get(i), arrayObject.toString());
//					}
//				}
//			} else if (valueStr.startsWith("{") && valueStr.endsWith("}")) { // Object data
//
//				valueStr = valueStr.substring(1, valueStr.length() - 1);
//				List<String> keyValues = formatKVs(valueStr);
//				for (String keyValue : keyValues) {
//					keyValue = keyValue.trim();
//					if (keyValue.endsWith("}")) {
//						generateObject((JsonObject00) currObject, keyValue);
//					}
//					if (keyValue.endsWith("]")) {
//
//						separator = keyValue.indexOf(":");
//						if (-1 == separator) {
//							throw new Exception(
//									"InvalidJsonkeyValueFormatExpection, expect key:value, but found " + kvStr);
//						}
//
//						keyStr = keyValue.substring(0, separator);
//						currKey = getKey(keyStr);
//						valueStr = keyValue.substring(separator + 1).trim();
//						List<String> kvs = formatKVs(valueStr.substring(1, valueStr.length() - 1));
//						json.JsonArray currArray = new json.JsonArray(kvs.size());
//						currObject.put(currKey, currArray);
//
//						for (int i = 0; i < kvs.size(); i++) {
//							currArray.add(new JsonObject00());
//							StringBuffer arrayObject = new StringBuffer();
//							arrayObject.append("\"").append(currKey).append("\"").append(":").append(kvs.get(i));
//							generateObject((JsonObject00) currArray.get(i), arrayObject.toString());
//						}
//					} else {
//
//						if (!(keyValue.endsWith("}") || keyValue.endsWith("]"))) {
//							separator = keyValue.indexOf(":");
//							if (-1 == separator) {
//								throw new Exception(
//										"InvalidJsonkeyValueFormatExpection, expect key:value, but found " + kvStr);
//							}
//
//							keyStr = keyValue.substring(0, separator);
//							valueStr = keyValue.substring(separator + 1).trim();
//							currObject.put(getKey(keyStr), TypeUtil.getValue(valueStr));
//						}
//					}
//				}
//			} else { // Non object data
//				currObject.put(currKey, TypeUtil.getValue(valueStr));
//			}
//		} else {
//			return ;
//		}
//	}
//
//	private static String getKey(String jsonStr) throws Exception {
//
//		String json = jsonStr.trim();
//
//		return json.substring(1, json.length() - 1);
//	}
//
//	private static List<String> formatKVs(String jsonStr) {
//
//		List<String> list = new ArrayList<String>();
//		String json = jsonStr.trim() + ',';
//		int bracketCount = 0;
//		StringBuffer sb = new StringBuffer();
//		for (char c : json.toCharArray()) {
//			if (c == '{') {
//				bracketCount++;
//			} else if (c == '}') {
//				bracketCount--;
//			}
//
//			sb.append(c);
//
//			if (bracketCount == 0 && c == ',') {
//				list.add(sb.substring(0, sb.length() - 1));
//				sb = new StringBuffer();
//			}
//		}
//
//		return list;
//	}
//
//	private static int charCount(String str, char ch) {
//
//		int count = 0;
//		for (char c : str.toCharArray()) {
//			if (c == ch) {
//				count++;
//			}
//		}
//
//		return count;
//	}
//
//	public static String toJsonStr(Object object) {
//
//		return null;
//	}
//
//	private static void vlidateJson(String jsonStr) {
//
//	}
//
//
//	@Override
//	public String toString() {
//		return this.toJsonString();
//	}
//
//	public String toJsonString() {
//
//		StringBuffer sb = new StringBuffer();
//		sb.append("{");
//		recursive2String(sb, this, false);
//		sb.append("}");
//		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), "}");
//		return PatternUtils.commaRightSquareBracket(str, "]");
//	}
//
//	@SuppressWarnings("unchecked")
//	private void recursive2String(StringBuffer sb, Object object, boolean isList) {
//
//		if (object instanceof Map) {
//			Map<String, Object> map = (Map<String, Object>) object;
//			for (Entry<String, Object> entry : map.entrySet()) {
//				Object value = entry.getValue();
//				if (value instanceof Map) {
//					if (!isList) {
//						sb.append("\"" + entry.getKey() + "\"");
//						sb.append(":");
//					}
//					sb.append("{");
//					recursive2String(sb, value, false);
//					sb.append("}");
//				} else if (value instanceof String) {
//					if (!isList) {
//						sb.append("\"" + entry.getKey() + "\"");
//						sb.append(":");
//					}
//					sb.append((String) value);
//				} else if (value instanceof Integer) {
//					if (!isList) {
//						sb.append("\"" + entry.getKey() + "\"");
//						sb.append(":");
//					}
//					sb.append((Integer) value);
//				} else if (value instanceof BigDecimal) {
//					if (!isList) {
//						sb.append("\"" + entry.getKey() + "\"");
//						sb.append(":");
//					}
//					sb.append((BigDecimal) value);
//				} else if (value instanceof Boolean) {
//					if (!isList) {
//						sb.append("\"" + entry.getKey() + "\"");
//						sb.append(":");
//					}
//					sb.append((Boolean) value);
//				} else if (null == value) {
//					if (SET_ON_NONULL) {
//						if (!isList) {
//							sb.append("\"" + entry.getKey() + "\"");
//							sb.append(":");
//						}
//					} else {
//
//					}
//				} else if (value instanceof List) {
//					sb.append("\"" + entry.getKey() + "\"");
//					sb.append(":");
//					sb.append("[");
//					List<?> list = (List<?>) value;
//					for (Object o : list) {
//						recursive2String(sb, o, true);
//					}
//					sb.append("]");
//				}
//				sb.append(",");
//			}
//		} else {
//			return;
//		}
//	}
//
//}
