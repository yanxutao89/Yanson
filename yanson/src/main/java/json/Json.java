package json;

import java.util.Collection;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/19 21:23
 */
public class Json {

    private static final JsonObject jsonObject = new JsonObject();
    private static final JsonArray jsonArray = new JsonArray();

    public static JsonObject parseObject(String json) {
        return jsonObject.fromJson(json);
    }

    public static JsonArray parseArray(String json) {
        return jsonArray.fromJson(json);
    }

    public static String toJsonString(Object object){

        if (null == object) {
            return "{}";
        }

        return JsonHelper.toJsonSting(object, object instanceof Collection ? true : false);

    }

}
