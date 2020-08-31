package exception;

/**
 * Exception indicating that the json value is not supported
 * @author yanxt7
 *
 */
public class InvalidJsonValueFormatException extends JsonException {


	private static final long serialVersionUID = -5773267584310362898L;
	
	public InvalidJsonValueFormatException(String message) {
		super(message);
	}

	public InvalidJsonValueFormatException(String message, Throwable cause) {
		super(message, cause);
	}

}
