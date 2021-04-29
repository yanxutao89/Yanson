package reflection;

import type.TypeUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/24 20:24
 */
public class FieldInvoker implements Invoker {

	private Field delegate;

	public FieldInvoker(Field delegate) {
		this.delegate = delegate;
	}

	public String getName() {
		String name = this.delegate.getName();
		if ("serialVersionUID".equals(name)) {
			return null;
		}
		return name;
	}

	public void setValue(Object object, Object value){
		checkPermission(delegate);
		try {
			Class type = getType();
			delegate.set(object, TypeUtil.cast2Object(value, type));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Class getType() {
		return this.delegate.getType();
	}

	@Override
	public <T> T getValue(Object object, Class<T> clazz, Object args) {
		checkPermission(this.delegate);
		try {
			return (T) this.delegate.get(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		return this.delegate.getAnnotation(clazz);
	}
}
