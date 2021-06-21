package yanson.utils;

public final class StringUtils {

	private StringUtils() {
		throw new UnsupportedOperationException("The constructor can not be called outside");
	}

	public static boolean isEmpty(String text) {
		if (null == text || text.length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}

}
