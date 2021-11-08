package yanson.exception;

/**
 * @author yanxt7
 *
 */
public class JsonException extends RuntimeException {

	private static final long serialVersionUID = -8079073315158391806L;
	
	public JsonException(String message) {
		super(message);
	}
	
	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}

}
