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
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static yanson.utils.ValidationUtils.isTrue;

public class BaseTypeVoTest {

	public static final List<String> JSON_LIST = new ArrayList<String>();
	private static String json;

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
		json = JSON_LIST.get(0);
	}

	@MyTest
	public void toJsonObject() throws Exception {
		// Yanson
		BaseTypeVo yansonObject = Json.parseObject(json).toJavaObject(BaseTypeVo.class);
		String yansonStr = Json.toJson(yansonObject);
		// FastJson
		BaseTypeVo fastJsonObject = JSONObject.parseObject(json).toJavaObject(BaseTypeVo.class);
		String fastjson = JSON.toJSONString(fastJsonObject);
		// Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		BaseTypeVo jacksonObject = objectMapper.readValue(json, BaseTypeVo.class);
		String jacksonStr = objectMapper.writeValueAsString(jacksonObject);
		// Gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		BaseTypeVo gsonObject = gson.fromJson(json, BaseTypeVo.class);
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
		AnnotationUtils.getExecutedTime("BaseTypeVoTest", args);
	}

}

