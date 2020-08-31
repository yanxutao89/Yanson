package exception;

/**
 * Exception indicating that the json is not valid
 * @author yanxt7
 *
 */
public class InvalidJsonKeyValueFormatException extends JsonException {


	private static final long serialVersionUID = -5773267584310362898L;
	
	public InvalidJsonKeyValueFormatException(String message) {
		super(message);
	}

	public InvalidJsonKeyValueFormatException(String message, Throwable cause) {
		super(message, cause);
	}

}
