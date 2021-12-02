package yanson.json;

import java.util.*;

import yanson.utils.ValidationUtils;

public class JsonObject extends HashMap<String, Object> implements JsonParser<JsonObject> {

	private static final long serialVersionUID = 4560188633954957114L;

	public JsonObject fromJson(String json) {
        try {
            ValidationUtils.isTrue(JsonUtil.isObject(json), String.format("Expect object, but found %s", json));
            StringBuilder sb = new StringBuilder();
            sb.append(Constants.DOUBLE_QUOTATIONS)
					.append(Constants.MAGIC)
					.append(Constants.DOUBLE_QUOTATIONS)
					.append(Constants.COLON)
					.append(json.trim());
            JsonObject jsonObject = new JsonObject();
            JsonHelper.readJson(sb.toString(), jsonObject);
            return (JsonObject) jsonObject.get(Constants.MAGIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonObject();
	}

	public String toJson(JsonObject jsonObject) {
		return JsonHelper.toJsonSting(jsonObject, new StringBuilder());
	}

	public String toJson() {
		return toJson(this);
	}

	public <T> T toJavaObject(Class<T> clazz) {
		return JsonHelper.toJavaObject(this, clazz, "");
	}

}
