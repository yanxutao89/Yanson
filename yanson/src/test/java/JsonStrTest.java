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


public class JsonStrTest {

	public static final List<String> JSON_LIST = new ArrayList<String>();
	private static String jsonStr;

	static {
		StringBuffer sb = new StringBuffer();
		InputStream is = ClassUtil.getDefaultClassLoader().getResourceAsStream("JsonStr.json");
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
		jsonStr = JSON_LIST.get(0);
	}

	@MyTest
	public void toJsonStr() throws Exception {
		// Yanson
		JsonObject yansonObject = Json.parseObject(jsonStr);
		String yansonStr = yansonObject.toJsonStr();
		// FastJson
		JSONObject fastJsonObject = JSONObject.parseObject(jsonStr);
		String fastJsonStr = fastJsonObject.toJSONString();
		// Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		JsonObject jacksonObject = objectMapper.readValue(jsonStr, JsonObject.class);
		String jacksonStr = objectMapper.writeValueAsString(jacksonObject);
		// Gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		com.google.gson.JsonObject gsonObject = gson.fromJson(jsonStr, com.google.gson.JsonObject.class);
		String gsonStr = gsonObject.toString();

		try {
			isTrue(Objects.equals(yansonStr, fastJsonStr), String.format("\nYanson:%s \nis not equal to \nFastJson:%s", yansonStr, fastJsonStr));
			isTrue(Objects.equals(fastJsonStr, jacksonStr), String.format("\nFastJson:%s \nis not equal to \nJackson:%s", fastJsonStr, jacksonStr));
			isTrue(Objects.equals(jacksonStr, gsonStr), String.format("\nJackson:%s \nis not equal to \nGson:%s", jacksonStr, gsonStr));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		AnnotationUtils.getExecutedTime("JsonStrTest", args);
	}

}

