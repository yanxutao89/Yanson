package yanson.utils;

import java.util.regex.Pattern;

public final class PatternUtils {
	private static final Pattern COMMA_RIGHT_CURLY_BRACKET = Pattern.compile("(,}){1}");
	private static final Pattern COMMA_RIGHT_SQUARE_BRACKET = Pattern.compile("(,]){1}");

	public static String commaRightCurlyBracket(String message, String replacement) {

		if (StringUtils.isEmpty(message)) {
			return "";
		}

		return COMMA_RIGHT_CURLY_BRACKET.matcher(message).replaceAll(replacement);
	}

	public static String commaRightSquareBracket(String message, String replacement) {
		if (StringUtils.isEmpty(message)) {
			return "";
		}

		return COMMA_RIGHT_SQUARE_BRACKET.matcher(message).replaceAll(replacement);
	}
}
