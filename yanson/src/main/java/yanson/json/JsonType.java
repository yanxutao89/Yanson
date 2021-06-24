package yanson.json;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/24 16:26
 */
public enum JsonType {
	STRING("string", MetaType.PRIMITIVE),
	NUMBER("number", MetaType.PRIMITIVE),
	BOOLEAN("boolean", MetaType.PRIMITIVE),
	NULL("null", MetaType.PRIMITIVE),
	OBJECT("object", MetaType.STRUCTURED),
	ARRAY("array", MetaType.STRUCTURED);

	private String value;
	private MetaType type;

	JsonType(String value, MetaType type) {
		this.value = value;
		this.type = type;
	}
}
