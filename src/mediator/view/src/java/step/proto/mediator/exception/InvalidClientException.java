package step.proto.mediator.exception;

public class InvalidClientException extends MediatorException {
    private static final long serialVersionUID = 1L;

    public InvalidClientException() { }

    public InvalidClientException(String message) {
        super(message);
    }

    public InvalidClientException(Throwable cause) {
        super(cause);
    }

    public InvalidClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
