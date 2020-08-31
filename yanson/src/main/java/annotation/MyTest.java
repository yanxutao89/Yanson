package annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marker for method executed time 
 * @author yanxt7
 *
 */
@Target(METHOD) 
@Retention(RUNTIME)
public @interface MyTest {
	
	/**
	 * method name
	 * @return
	 */
	String name() default "";
	
	/**
	 * execution amount
	 * @return
	 */
	int count() default 1;
	
}
