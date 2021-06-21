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

	public String getName() {
		if (isWriteMethod()) {
			String name = this.delegate.getName();
			WriteMethodPrefix[] prefixes = WriteMethodPrefix.values();
			for (WriteMethodPrefix prefix : prefixes) {
				String prefixValue = prefix.getValue();
				if (name.startsWith(prefixValue)) {
					name = name.substring(prefixValue.length());
					if (null != name && name.length() > 0) {
						name = name.substring(0, 1).toLowerCase() + name.substring(1);
						return name;
					}
				}
			}
		}
		return null;
	}

	public void setValue(Object object, Object values){
		checkPermission(delegate);
		try {
			Class type = getType();
			delegate.invoke(object, TypeUtil.cast2Object(values, type, null));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Class getType() {
		return this.delegate.getParameterTypes()[0];
	}

	@Override
	public <T> T getValue(Object object, Class<T> clazz, Object args) {
		checkPermission(this.delegate);
		try {
			return (T) this.delegate.invoke(object, args);
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
