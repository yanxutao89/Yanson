package yanson.reflection;

import yanson.type.TypeUtil;
import yanson.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/24 20:26
 */
public class MethodInvoker implements Invoker {

	private Method delegate;

	public MethodInvoker(Method delegate) {
		this.delegate = delegate;
	}

	public String getProperty() {
		if (isWriteMethod()) {
			String property = this.delegate.getName();
			WriteMethodPrefix[] prefixes = WriteMethodPrefix.values();
			for (WriteMethodPrefix prefix : prefixes) {
				String prefixValue = prefix.getValue();
				if (property.startsWith(prefixValue)) {
					property = property.substring(prefixValue.length());
					if (!StringUtils.isEmpty(property)) {
						property = property.substring(0, 1).toLowerCase() + property.substring(1);
						return property;
					}
				}
			}
		}
		return null;
	}

	public void setValue(Object instance, Object value){
		checkPermission(delegate);
		try {
			Class type = getType();
			delegate.invoke(instance, TypeUtil.cast2Object(value, type, null));
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Class getType() {
		return this.delegate.getParameterTypes()[0];
	}

	@Override
	public <T> T getValue(Object instance, Class<T> clazz, Object value) {
		checkPermission(this.delegate);
		try {
			return (T) this.delegate.invoke(instance, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		return this.delegate.getAnnotation(clazz);
	}

	public boolean isWriteMethod() {
		String name = this.delegate.getName();

		if (StringUtils.isEmpty(name)) {
			return false;
		}

		boolean isWrite = false;
		WriteMethodPrefix[] prefixes = WriteMethodPrefix.values();
		for (WriteMethodPrefix prefix : prefixes) {
			isWrite = name.startsWith(prefix.getValue());
			if (isWrite) {
				break;
			}
		}

		return isWrite;
	}
}
