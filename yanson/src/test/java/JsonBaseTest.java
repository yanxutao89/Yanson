import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import yanson.annotation.MyTest;
import yanson.json.Json;
import yanson.json.JsonArray;
import yanson.json.JsonObject;
import yanson.reflection.ClassUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonBaseTest {

	public static final List<String> JSON_LIST = new ArrayList<>();

	static {
		StringBuffer sb = new StringBuffer();
		InputStream is = ClassUtil.getDefaultClassLoader().getResourceAsStream("JsonTest.txt");
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
	}

	@MyTest
	public void test() throws Exception {
		for (String json : JSON_LIST) {
			yanson(json);
			fastJson(json);
			jackson(json);
			gson(json);
		}
	}

	public void yanson(String json) throws Exception {
		if (json.startsWith("[")) {
			JsonArray jsonArray = Json.parseArray(json);
			System.out.println(jsonArray);
		} else {
			JsonObject jsonObject = Json.parseObject(json);
			json = jsonObject.toJson();
			System.out.println("Yanson=" + json.length() + ":" + json);
		}
	}

	public void fastJson(String json) {
		if (json.startsWith("[")) {

		} else {
			JSONObject jsonObject = JSONObject.parseObject(json);
			json = jsonObject.toJSONString();
			System.out.println("FastJson=" + json.length() + ":" + json);
		}
	}

	public void jackson(String json) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		if (json.startsWith("[")) {

		} else {
			JsonObject jsonObject = objectMapper.readValue(json, JsonObject.class);
			json = objectMapper.writeValueAsString(jsonObject);
			System.out.println("Jackson=" + json.length() + ":" + json);
		}
	}

	public void gson(String json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (json.startsWith("[")) {

		} else {
			com.google.gson.JsonObject jsonObject = gson.fromJson(json, com.google.gson.JsonObject.class);
			json = jsonObject.toString();
			System.out.println("Gson=" + json.length() + ":" + json);
		}
	}

	public static void main(String[] args) throws Exception {
		AnnotationUtils.getExecutedTime("JsonBaseTest", args);
	}

}

