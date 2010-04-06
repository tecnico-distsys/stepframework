package step.examples.tripplanner.mediator.domain;

import org.joda.time.LocalDate;

public class Reservation extends Reservation_Base {

	public Reservation(int code, String flightCode, LocalDate departure, int bookingCode) {
		super.setCode(code);
		setFlightCode(flightCode);
		setDeparture(departure);
		setBookingCode(bookingCode);
	}

	@Override
	public void setCode(int code) {
		throw new UnsupportedOperationException();
	}
}
