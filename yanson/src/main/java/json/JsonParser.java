package json;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/19 18:56
 */
public interface JsonParser<O> extends Parser {

    O fromJson(String json);

    String toJson(O o);

}
