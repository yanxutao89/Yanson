package yanson.reflection;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/25 11:17
 */
public enum ReadMethodPrefix {
	GET("get"),
	IS("is");

	private String value;

	ReadMethodPrefix(String value) {
		this.value = value;
	}

	public String getValue(){
		return this.value;
	}
}
