import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import yanson.annotation.MyTest;
import yanson.json.Json;
import yanson.json.JsonObject;
import yanson.reflection.ClassUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static yanson.utils.ValidationUtils.isTrue;

public class JsonTest {

	public static final List<String> JSON_LIST = new ArrayList<String>();
	private static String json;

	static {
		StringBuffer sb = new StringBuffer();
		InputStream is = ClassUtil.getDefaultClassLoader().getResourceAsStream("Json.json");
		byte[] buffer = new byte[1024 * 8];
		int len;
		try {
			while ((len = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, len, "utf-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] jsons = sb.toString().split("\\*\\*\\*\\*\\*\\*\\*");
		for (String json : jsons) {
			JSON_LIST.add(json.trim());
		}
		json = JSON_LIST.get(0);
	}

	@MyTest
	public void tojson() throws Exception {
		// Yanson
		JsonObject yansonObject = Json.parseObject(json);
		String yansonStr = yansonObject.toJson();
		// FastJson
		JSONObject fastJsonObject = JSONObject.parseObject(json);
		String fastjson = fastJsonObject.toJSONString();
		// Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		JsonObject jacksonObject = objectMapper.readValue(json, JsonObject.class);
		String jacksonStr = objectMapper.writeValueAsString(jacksonObject);
		// Gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		com.google.gson.JsonObject gsonObject = gson.fromJson(json, com.google.gson.JsonObject.class);
		String gsonStr = gsonObject.toString();

		try {
			isTrue(Objects.equals(yansonStr, fastjson), String.format("\nYanson:%s \nis not equal to \nFastJson:%s", yansonStr, fastjson));
			isTrue(Objects.equals(fastjson, jacksonStr), String.format("\nFastJson:%s \nis not equal to \nJackson:%s", fastjson, jacksonStr));
			isTrue(Objects.equals(jacksonStr, gsonStr), String.format("\nJackson:%s \nis not equal to \nGson:%s", jacksonStr, gsonStr));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		AnnotationUtils.getExecutedTime("JsonTest", args);
	}

}

