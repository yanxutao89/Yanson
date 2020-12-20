package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

/**
 * Marker for field deserialization on JavaBean
 * @author yanxt7
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface JsonField {

	/**
	 * target property name
	 * @return
	 */
	String value();

	/**
	 * alternative keys
	 * @return
	 */
	String[] aliasNames() default {};

}
