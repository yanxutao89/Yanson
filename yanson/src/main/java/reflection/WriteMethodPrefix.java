package reflection;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/25 11:17
 */
public enum WriteMethodPrefix {

	SET("set"),
	IS("is");

	private String value;

	WriteMethodPrefix(String value) {
		this.value = value;
	}

	public String getValue(){
		return this.value;
	}

}
