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

	void setValue(Object instance, Object value);

	Class getType();

	<T> T getValue(Object instance, Class<T> clazz, Object value);

	<T extends Annotation> T getAnnotation(Class<T> clazz);

	default void checkPermission(AccessibleObject accessibleObject) {
		if (!accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
	}
}
