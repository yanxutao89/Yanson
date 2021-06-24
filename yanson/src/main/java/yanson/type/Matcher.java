package yanson.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/9/1 22:23
 */
public interface Matcher {
    boolean isMatched(Field field, Method method, String key);
}
