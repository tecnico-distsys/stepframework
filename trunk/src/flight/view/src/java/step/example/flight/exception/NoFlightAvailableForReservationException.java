package step.example.flight.exception;


public class NoFlightAvailableForReservationException extends
		FlightDomainException {

	private static final long serialVersionUID = 1L;

	public NoFlightAvailableForReservationException() {
	}

	public NoFlightAvailableForReservationException(String message) {
		super(message);
	}

	public NoFlightAvailableForReservationException(Throwable cause) {
		super(cause);
	}

	public NoFlightAvailableForReservationException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
