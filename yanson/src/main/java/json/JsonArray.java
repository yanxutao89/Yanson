package json;

import java.util.ArrayList;

public class JsonArray extends ArrayList<Object> {
	
	private static final long serialVersionUID = -7694911553868661587L;
	
	private static final int DEFAULT_SIZE = 16;

	public JsonArray() {
		super(DEFAULT_SIZE);
	}
	
	public JsonArray(int size) {
		super(size);
	}

}
