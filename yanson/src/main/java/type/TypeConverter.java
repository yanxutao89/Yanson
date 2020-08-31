package type;

public interface TypeConverter<T> {

	T resolve(Class<T> clazz);
	
}
