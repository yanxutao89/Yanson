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
	private boolean isRead;

	public MethodInvoker(Method delegate) {
		this.delegate = delegate;
	}

	public MethodInvoker(Method delegate, boolean isRead) {
		this.delegate = delegate;
		this.isRead = isRead;
	}

	public String getName() {
		if (isRead) {
			if (isReadMethod()) {
				String name = this.delegate.getName();
				ReadMethodPrefix[] prefixes = ReadMethodPrefix.values();
				for (ReadMethodPrefix prefix : prefixes) {
					String prefixValue = prefix.getValue();
					if (name.startsWith(prefixValue) && this.delegate.getParameterTypes().length == 0) {
						name = name.substring(prefixValue.length());
						if (!StringUtils.isEmpty(name)) {
							name = name.substring(0, 1).toLowerCase() + name.substring(1);
							return name;
						}
					}
				}
			}
		} else {
			if (isWriteMethod()) {
				String name = this.delegate.getName();
				WriteMethodPrefix[] prefixes = WriteMethodPrefix.values();
				for (WriteMethodPrefix prefix : prefixes) {
					String prefixValue = prefix.getValue();
					if (name.startsWith(prefixValue) && this.delegate.getParameterTypes().length == 1) {
						name = name.substring(prefixValue.length());
						if (!StringUtils.isEmpty(name)) {
							name = name.substring(0, 1).toLowerCase() + name.substring(1);
							return name;
						}
					}
				}
			}
		}
		return null;
	}

	public void setValue(Object instance, Object value){
		checkPermission(delegate);
		try {
			if (isWriteMethod()) {
				Class type = getType();
				delegate.invoke(instance, TypeUtil.cast2Object(value, type, null));
			}
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
	public <T> T getValue(Object instance) {
		checkPermission(this.delegate);
		try {
			if (isReadMethod()) {
				return (T) this.delegate.invoke(instance);
			}
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

	private boolean isWriteMethod() {
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

	private boolean isReadMethod() {
		String name = this.delegate.getName();
		if (StringUtils.isEmpty(name)) {
			return false;
		}
		boolean isRead = false;
		ReadMethodPrefix[] prefixes = ReadMethodPrefix.values();
		for (ReadMethodPrefix prefix : prefixes) {
			isRead = name.startsWith(prefix.getValue());
			if (isRead) {
				break;
			}
		}
		return isRead;
	}

}
