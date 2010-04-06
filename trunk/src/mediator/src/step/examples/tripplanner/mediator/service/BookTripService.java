package step.examples.tripplanner.mediator.service;

import org.joda.time.LocalDate;

import step.examples.tripplanner.flight.exception.NoAvailableFlightsException;
import step.examples.tripplanner.flight.view.ReservationVoucher;
import step.examples.tripplanner.flight.ws.client.service.BookFlightService;
import step.examples.tripplanner.mediator.exception.NoFlightAvailableException;
import step.examples.tripplanner.mediator.service.CreateReservationService;
import step.examples.tripplanner.mediator.view.*;
import step.framework.service.Service;

public class BookTripService extends Service<ReservationView> {
	private String origin;
	private String destination;
	private String id;
	private String name;

	public BookTripService(String origin, String destination, String id,
			String name) {
		this.origin = origin;
		this.destination = destination;
		this.id = id;
		this.name = name;
	}

	@Override
	protected ReservationView action() {
		try {
			ReservationVoucher voucher = new BookFlightService(
					origin, destination, id, name).execute();
			ReservationView reservation = new CreateReservationService(id,
					name, voucher.getFlightNumber(), new LocalDate(voucher.getFlightDate()), voucher.getReservationCode()).execute();
			return reservation;
		} catch (NoAvailableFlightsException ex) {
			throw new NoFlightAvailableException(ex);
		}
	}
}
