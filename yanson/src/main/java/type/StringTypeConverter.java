package type;

import static utils.ValidationUtils.notNull;

public class StringTypeConverter implements TypeConverter<String> {

	public String resolve(Class<String> clazz) {

		notNull(clazz, "Parameter 'clazz' must not be null");

		return null;
	}

}
