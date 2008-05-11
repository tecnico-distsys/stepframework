package step.proto.mediator.exception;

public class DuplicateClientException extends MediatorException {
    private static final long serialVersionUID = 1L;

    public DuplicateClientException() { }

    public DuplicateClientException(String message) {
        super(message);
    }

    public DuplicateClientException(Throwable cause) {
        super(cause);
    }

    public DuplicateClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
