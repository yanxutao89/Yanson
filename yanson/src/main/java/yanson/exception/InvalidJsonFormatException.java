package yanson.exception;

/**
 * Exception indicating that the com.json format is invalid
 * @author yanxt7
 *
 */
public class InvalidJsonFormatException extends JsonException {

	private static final long serialVersionUID = -5773267584310362898L;

	public InvalidJsonFormatException(String message) {
		super(message);
	}

	public InvalidJsonFormatException(String message, Throwable cause) {
		super(message, cause);
	}

}
