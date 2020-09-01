package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for method executed time
 * @author yanxt7
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MyTest {

	/**
	 * target method name
	 * @return
	 */
	String name();

	/**
	 * execution amount
	 * @return
	 */
	int count() default 1;

}
