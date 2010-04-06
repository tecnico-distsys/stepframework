package step.examples.tripplanner.flight.service;

import java.util.Set;

import step.examples.tripplanner.flight.domain.Airport;
import step.examples.tripplanner.flight.domain.FlightApp;
import step.examples.tripplanner.flight.domain.Reservation;
import step.examples.tripplanner.flight.view.ReservationVoucher;

import step.framework.service.Service;

public class BookFlightService extends Service<ReservationVoucher> {
	private String origin;
	private String destination;
	private String passport;
	private String name;
	
	public BookFlightService(String origin, String destination, String passport, String name) {
		this.origin = origin;
		this.destination = destination;
		this.passport = passport;
		this.name = name;		
	}

	@Override
	protected ReservationVoucher action() {
		FlightApp app = getRoot();

		Set<Airport> departFrom = app.findAirport(origin);
		Set<Airport> arriveAt = app.findAirport(destination);
		
		Reservation fr = app.getAirline().reserveFlight(departFrom, arriveAt,
				app.getAirline().createPassenger(passport, name));
		
		// return view
		return new ReservationVoucher(fr.getCode(), fr.getFlight().getFlight().getCodeAsString(),
				fr.getFlight().getDeparture().toDate());
	}

}
