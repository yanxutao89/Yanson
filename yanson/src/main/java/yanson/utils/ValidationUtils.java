package yanson.utils;

public final class ValidationUtils {
	public static void notNull(Object object, String message) throws IllegalArgumentException {
		if (null == object) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void isTrue(Boolean expression, String message) throws Exception {
		if (!expression) {
			throw new Exception(message);
		}
	}
}
