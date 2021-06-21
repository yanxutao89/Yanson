package yanson.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/24 20:24
 */
public interface Invoker {

	String getName();

	void setValue(Object object, Object args);

	Class getType();

	<T> T getValue(Object object, Class<T> clazz, Object args);

	<T extends Annotation> T getAnnotation(Class<T> clazz);

	default void checkPermission(AccessibleObject accessibleObject) {
		if (!accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
	}

}
