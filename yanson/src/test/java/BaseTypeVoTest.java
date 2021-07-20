import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.BaseTypeVo;
import yanson.annotation.MyTest;
import yanson.json.Json;
import yanson.reflection.ClassUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static yanson.utils.ValidationUtils.isTrue;


public class BaseTypeVoTest {
	public static final List<String> JSON_LIST = new ArrayList<String>();
	private static String jsonStr;

	static {
		StringBuffer sb = new StringBuffer();
		InputStream is = ClassUtil.getDefaultClassLoader().getResourceAsStream("BaseTypeVo.json");
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
	public void toJsonObject() throws Exception {
		// Yanson
		BaseTypeVo yansonObject = Json.parseObject(jsonStr).toJavaObject(BaseTypeVo.class);
		String yansonStr = Json.toJsonString(yansonObject);
		// FastJson
		BaseTypeVo fastJsonObject = JSONObject.parseObject(jsonStr).toJavaObject(BaseTypeVo.class);
		String fastJsonStr = JSON.toJSONString(fastJsonObject);
		// Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		BaseTypeVo jacksonObject = objectMapper.readValue(jsonStr, BaseTypeVo.class);
		String jacksonStr = objectMapper.writeValueAsString(jacksonObject);
		// Gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		BaseTypeVo gsonObject = gson.fromJson(jsonStr, BaseTypeVo.class);
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
		AnnotationUtils.getExecutedTime("BaseTypeVoTest", args);
	}
}

