package yanson.json;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/19 21:23
 */
public class Json {

    private static final JsonObject JSON_OBJECT = new JsonObject();
    private static final JsonArray JSON_ARRAY = new JsonArray();

    public static JsonObject parseObject(String json) {
        return json == null ? null : JSON_OBJECT.fromJson(json);
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return JsonHelper.readJson(json, clazz);
    }

    public static JsonArray parseArray(String json) {
        return json == null ? null : JSON_ARRAY.fromJson(json);
    }

    public static String toJson(Object object){
        return null == object ? null : JsonHelper.toJsonSting(object, new StringBuilder());
    }

}
