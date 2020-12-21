package json;

import utils.PatternUtils;
import utils.ValidationUtils;

import java.util.ArrayList;

import static json.Constants.*;


public class JsonArray extends ArrayList<Object> implements JsonParser<JsonArray> {

	private static final long serialVersionUID = -7694911553868661587L;

	private static final int DEFAULT_SIZE = 16;

	public JsonArray() {
		super(DEFAULT_SIZE);
	}

	public JsonArray(int size) {
		super(size);
	}

	public JsonArray fromJson(String json) {

		try {
			StringBuilder sb = new StringBuilder();
			ValidationUtils.isTrue(JsonUtil.isArray(json), String.format("Expect array, but found object"));
			sb.append("\"").append(MAGIC).append("\"").append(COLON).append(json.trim());
			JsonObject jsonObject = new JsonObject();
			JsonHelper.readJson(sb.toString(), jsonObject);
			return (JsonArray) jsonObject.get(MAGIC);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JsonArray();

	}

	public String toJson(JsonArray jsonArray) {
		StringBuilder sb = new StringBuilder();
		sb.append(LEFT_SQUARE_BRACKET);
		sb.append(JsonHelper.toJsonSting(jsonArray, true));
		sb.append(RIGHT_SQUARE_BRACKET);
		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), RIGHT_CURLY_BRACKET);
		return PatternUtils.commaRightSquareBracket(str, RIGHT_SQUARE_BRACKET);
	}
}
