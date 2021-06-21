package yanson.type;

public interface TypeConverter<T> {

	T resolve(Class<T> clazz);
	
}
