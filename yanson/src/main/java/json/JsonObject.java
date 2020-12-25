package json;

import java.util.*;

import utils.*;

import static json.Constants.*;

public class JsonObject extends HashMap<String, Object> implements JsonParser<JsonObject> {

	private static final long serialVersionUID = 4560188633954957114L;

	public JsonObject fromJson(String jsonStr) {

        try {
            ValidationUtils.isTrue(JsonUtil.isObject(jsonStr), String.format("Expect object, but found array"));
            StringBuilder sb = new StringBuilder();
            sb.append("\"").append(MAGIC).append("\"").append(COLON).append(jsonStr.trim());
            JsonObject jsonObject = new JsonObject();
            JsonHelper.readJson(sb.toString(), jsonObject);
            return (JsonObject) jsonObject.get(MAGIC);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonObject();

	}

	public String toJson(JsonObject jsonObject) {
		StringBuilder sb = new StringBuilder();
		sb.append(LEFT_CURLY_BRACKET);
		sb.append(JsonHelper.toJsonSting(jsonObject, false));
		sb.append(RIGHT_CURLY_BRACKET);
		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), RIGHT_CURLY_BRACKET);
		return PatternUtils.commaRightSquareBracket(str, RIGHT_SQUARE_BRACKET);
	}

	public <T> T toJavaObject(Class<T> clazz) {
		return JsonHelper.toJavaObject(this, clazz, "");
	}

}
