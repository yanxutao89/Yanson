package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker for field deserialization
 * @author yanxt7
 *
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface JsonField {

	/**
	 * field name
	 * @return
	 */
	String name() default "";

	/**
	 * alternative field names
	 * @return
	 */
	String[] aliasNames() default {};

}
