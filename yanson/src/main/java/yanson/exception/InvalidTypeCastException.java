package yanson.exception;

public class InvalidTypeCastException extends JsonException {
    private static final long serialVersionUID = 388887993933763622L;

    public InvalidTypeCastException(String message) {
        super(message);
    }

    public InvalidTypeCastException(String from, String to) {
        super(String.format("%s can not be cast to %s", from, to));
    }

    public InvalidTypeCastException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTypeCastException(String from, String to, Throwable cause) {
        super(String.format("%s can not be cast to %s", from, to), cause);
    }
}
