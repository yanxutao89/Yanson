package yanson.type;

import yanson.utils.ValidationUtils;

public class StringTypeConverter implements TypeConverter<String> {

	public String resolve(Class<String> clazz) {
		ValidationUtils.notNull(clazz, "Parameter 'clazz' must not be null");
		return null;
	}

}
