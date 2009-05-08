package step.example.mediator.exception;

public class DuplicateReservationException extends ClientException {
	private static final long serialVersionUID = 1L;

	public DuplicateReservationException() {
	}

	public DuplicateReservationException(String message) {
		super(message);
	}

	public DuplicateReservationException(Throwable cause) {
		super(cause);
	}

	public DuplicateReservationException(String message, Throwable cause) {
		super(message, cause);
	}
}
