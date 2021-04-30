package utils;

import java.util.Arrays;
import java.util.Collection;

public class CollectionUtils {

	private CollectionUtils() {
		throw new UnsupportedOperationException("The constructor can not be called outside");
	}
	
	public static boolean isEmpty(Collection<?> collection) {
		return null == collection || collection.size() == 0;
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
	
	public static boolean isEmpty(Object[] array) {
		return isEmpty(Arrays.asList(array));
	}

}
