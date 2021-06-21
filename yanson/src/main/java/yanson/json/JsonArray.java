package yanson.json;

import yanson.utils.PatternUtils;
import yanson.utils.ValidationUtils;

import java.util.ArrayList;


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
			sb.append("\"").append(Constants.MAGIC).append("\"").append(Constants.COLON).append(json.trim());
			JsonObject jsonObject = new JsonObject();
			JsonHelper.readJson(sb.toString(), jsonObject);
			return (JsonArray) jsonObject.get(Constants.MAGIC);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JsonArray();

	}

	public String toJson(JsonArray jsonArray) {
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.LEFT_SQUARE_BRACKET);
		sb.append(JsonHelper.toJsonSting(jsonArray, true));
		sb.append(Constants.RIGHT_SQUARE_BRACKET);
		String str = PatternUtils.commaRightCurlyBracket(sb.toString(), Constants.RIGHT_CURLY_BRACKET);
		return PatternUtils.commaRightSquareBracket(str, Constants.RIGHT_SQUARE_BRACKET);
	}
}
