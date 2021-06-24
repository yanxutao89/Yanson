package yanson.json;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/24 17:18
 */
public enum MetaType {
	PRIMITIVE("primitive"),
	STRUCTURED("structured");

	private String value;

	MetaType(String value) {
		this.value = value;
	}
}
