package json;

import java.util.*;

import utils.*;

import static json.Constants.MAGIC;
import static json.Constants.COLON;

public class JsonObject extends HashMap<String, Object> implements JsonParser<JsonObject> {

	private static final long serialVersionUID = 4560188633954957114L;

	public JsonObject fromJson(String json) {

        try {
            ValidationUtils.isTrue(!JsonUtil.isArray(json), String.format("Expect object, but found array"));
            StringBuilder sb = new StringBuilder();
            sb.append("\"").append(MAGIC).append("\"").append(COLON).append(json.trim());
            JsonObject jsonObject = new JsonObject();
            JsonHelper.readJson(jsonObject, sb.toString());
            return (JsonObject) jsonObject.get(MAGIC);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonObject();

	}

	public String toJson(JsonObject jsonObject) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(JsonHelper.toJsonSting(jsonObject, false));
		sb.append("}");
		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), "}");
		return PatternUtils.commaRightSquareBracket(str, "]");
	}

	public <T> T toJavaObject(Class<T> clazz) {
		return JsonHelper.toJavaObject(this, clazz, "");
	}

}
