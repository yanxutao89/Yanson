import yanson.annotation.MyTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class AnnotationUtils {
	private static final int TABLE_LENGTH = 64;
	private static boolean HEAD = false;
	
	private AnnotationUtils() {
		throw new UnsupportedOperationException("The constructor can not be called outside");
	}
	
	public static void getExecutedTime(String className, Object[] args) throws Exception{
		Class<?> clazz = Class.forName(className);
		// Construct the target object
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		if(constructors.length == 0) {
			throw new Exception("The target must not be an interface, a primitive type, an array class or a void method.");
		}
		boolean canCallNewInstance = false;
		for (Constructor<?> constructor : constructors) {
			if(constructor.getAnnotatedParameterTypes().length == 0) {
				canCallNewInstance = true;
			}
		}
		if(canCallNewInstance) {
			Object instance = clazz.newInstance();
			// Find the methods annotated with the annotation
			for (Method method : clazz.getDeclaredMethods()) {
				if(method.isAnnotationPresent(MyTest.class)) {
					MyTest myTest = method.getAnnotation(MyTest.class);
					if(myTest == null) {
						continue;
					}
					String methodName = null; 
					if(myTest.name().length() > 0) {
						methodName = myTest.name();
					} else {
						methodName = method.getName();
					}
					
					int countOfCall = myTest.count();
					List<Double> timeList = new ArrayList<Double>(countOfCall);
					Double totalTime = 0.0;	
					for(int i = 0; i < countOfCall; ++i) {
						if (!HEAD) {
							System.out.println("methodName" + "\t\t\t\t\t\t\t" + "averageTime");
							HEAD = true;
						}
						
						long startTime = System.nanoTime();
						if(!method.isAccessible()) {
							method.setAccessible(true);
						}
						method.invoke(instance, args);
						double consumedTime = (System.nanoTime() - startTime) * 1e-9;
						timeList.add(consumedTime);
						totalTime += consumedTime;
					}
					StringBuffer sb = new StringBuffer();
					int interval = TABLE_LENGTH - methodName.length();
					for (int i = 0; i < interval; ++i) {
						sb.append(" ");
					}
					System.out.println(methodName + sb.toString() + totalTime / countOfCall);
				}
			} 
		}
		else {
			throw new Exception("The target class must be provided with a empty constructor");
		}
	}
}
