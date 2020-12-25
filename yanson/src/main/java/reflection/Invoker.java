package reflection;

import type.TypeUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/24 20:24
 */
public interface Invoker {

	String getName();

	void setValue(Object object, Object... args);

	Class getType();

	<T> T getValue(Object object, Class<T> clazz, Object... args);

	<T extends Annotation> T getAnnotation(Class<T> clazz);

	default void checkPermission(AccessibleObject accessibleObject) {
		if (!accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
	}

	default <T> T castObject(Object object, Class<T> clazz) {
		T instance = null;

		try {

			if (TypeUtil.isElementType(clazz)) {
				return TypeUtil.cast2Element(object, clazz);
			}

			if (TypeUtil.isCollectionType(clazz)) {
				return TypeUtil.cast2Collection(object, clazz, 16);
			}

			if (TypeUtil.isArrayType(clazz)) {
				return TypeUtil.cast2Array(object, clazz, 16);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		return instance;
	}

}
