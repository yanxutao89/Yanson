package yanson.reflection;

import yanson.type.TypeUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

	public void setValue(Object instance, Object value){
		checkPermission(delegate);
		try {
			Class type = getType();
			Class[] classes = null;
			if (delegate.getGenericType() instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) delegate.getGenericType()).getActualTypeArguments();
				if (actualTypeArguments != null && actualTypeArguments.length > 0) {
					classes = new Class[actualTypeArguments.length];
					for (int i = 0; i < actualTypeArguments.length; ++i) {
						classes[i] = (Class) actualTypeArguments[i];
					}
				}
			}
			delegate.set(instance, TypeUtil.cast2Object(value, type, classes));
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Class getType() {
		return this.delegate.getType();
	}

	@Override
	public <T> T getValue(Object instance) {
		checkPermission(this.delegate);
		try {
			return (T) this.delegate.get(instance);
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		return this.delegate.getAnnotation(clazz);
	}
}
