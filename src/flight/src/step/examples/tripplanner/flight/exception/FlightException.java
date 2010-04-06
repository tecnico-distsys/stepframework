package step.examples.tripplanner.flight.exception;

public class FlightException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FlightException() { }

	public FlightException(String message) {
		super(message);
	}

	public FlightException(Throwable cause) {
		super(cause);
	}

	public FlightException(String message, Throwable cause) {
		super(message, cause);
	}
}
