package step.examples.tripplanner.mediator.domain;

import org.joda.time.LocalDate;

public class Client extends Client_Base {

	public  Client(String identification, String name) {
		setIdentification(identification);
		setName(name);
	}
	
	public Reservation createReservation(String flightCode, LocalDate departure, int bookingCode) {
		int lastCode = getMediator().getLastReservationCode();
		Reservation reservation = new Reservation(++lastCode, flightCode, departure, bookingCode);
		addReservation(reservation);
		getMediator().setLastReservationCode(lastCode);

		return reservation;
	}

}
