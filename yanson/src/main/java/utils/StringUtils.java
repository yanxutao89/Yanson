package utils;

public class StringUtils {
	
	public static boolean isEmpty(String text) {
		
		if (null == text || text.length() == 0) {
			return true;
		}
		
		return false;
	}

}
