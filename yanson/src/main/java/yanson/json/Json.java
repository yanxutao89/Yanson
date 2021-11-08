package yanson.json;


/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/19 21:23
 */
public class Json {
    private static final JsonObject jsonObject = new JsonObject();
    private static final JsonArray jsonArray = new JsonArray();

    public static JsonObject parseObject(String jsonStr) {
        return jsonStr == null ? null : jsonObject.fromJson(jsonStr);
    }

    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        return JsonHelper.readJson(jsonStr, clazz);
    }

    public static JsonArray parseArray(String jsonStr) {
        return jsonStr == null ? null : jsonArray.fromJson(jsonStr);
    }

    public static String toJsonString(Object object){
        return null == object ? "{}" : JsonHelper.toJsonSting(object, new StringBuilder());
    }
}
