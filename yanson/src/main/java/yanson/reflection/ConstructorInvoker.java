package yanson.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/25 13:54
 */
public class ConstructorInvoker implements Invoker {

	private Constructor delegate;

	public ConstructorInvoker(Constructor delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getName() {
		return this.delegate.getName();
	}

	@Override
	public void setValue(Object instance, Object value) {
		throw new UnsupportedOperationException("This method can not be called within ConstructorInvoker");
	}

	@Override
	public Class getType() {
		throw new UnsupportedOperationException("This method can not be called within ConstructorInvoker");
	}

	@Override
	public <T> T getValue(Object instance) {
		checkPermission(this.delegate);
		try {
			return (T) this.delegate.newInstance();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> clazz) {
		checkPermission(this.delegate);
		return (T) this.delegate.getAnnotation(clazz);
	}

}
