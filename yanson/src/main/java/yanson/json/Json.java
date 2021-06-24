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
        return jsonObject.fromJson(jsonStr);
    }

    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        return JsonHelper.readJson(jsonStr, clazz);
    }

    public static JsonArray parseArray(String jsonStr) {
        return jsonArray.fromJson(jsonStr);
    }

    public static String toJsonString(Object object){
        if (null == object) {
            return "{}";
        }

        return JsonHelper.toJsonSting(object, new StringBuilder());
    }
}
