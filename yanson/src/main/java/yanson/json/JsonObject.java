package yanson.json;

import java.util.*;

import yanson.utils.PatternUtils;
import yanson.utils.ValidationUtils;

public class JsonObject extends HashMap<String, Object> implements JsonParser<JsonObject> {

	private static final long serialVersionUID = 4560188633954957114L;

	public JsonObject fromJson(String jsonStr) {

        try {
            ValidationUtils.isTrue(JsonUtil.isObject(jsonStr), String.format("Expect object, but found array"));
            StringBuilder sb = new StringBuilder();
            sb.append("\"").append(Constants.MAGIC).append("\"").append(Constants.COLON).append(jsonStr.trim());
            JsonObject jsonObject = new JsonObject();
            JsonHelper.readJson(sb.toString(), jsonObject);
            return (JsonObject) jsonObject.get(Constants.MAGIC);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonObject();

	}

	public String toJson(JsonObject jsonObject) {
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.LEFT_CURLY_BRACKET);
		sb.append(JsonHelper.toJsonSting(jsonObject, false));
		sb.append(Constants.RIGHT_CURLY_BRACKET);
		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), Constants.RIGHT_CURLY_BRACKET);
		return PatternUtils.commaRightSquareBracket(str, Constants.RIGHT_SQUARE_BRACKET);
	}

	public <T> T toJavaObject(Class<T> clazz) {
		return JsonHelper.toJavaObject(this, clazz, "");
	}

}
