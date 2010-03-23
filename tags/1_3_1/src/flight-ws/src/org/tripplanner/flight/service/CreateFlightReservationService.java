package org.tripplanner.flight.service;

import org.tripplanner.flight.domain.FlightManager;
import org.tripplanner.flight.domain.FlightReservation;
import org.tripplanner.flight.exception.FlightDomainException;
import org.tripplanner.flight.view.ReservationVoucher;

public class CreateFlightReservationService extends
		FlightBaseService<ReservationVoucher> {

	private String origin;
	private String destination;
	private String id;
	private String name;

	public CreateFlightReservationService(String origin, String destination,
			String id, String name) {
		this.origin = origin;
		this.destination = destination;
		this.id = id;
		this.name = name;
	}

	@Override
	protected ReservationVoucher action() throws FlightDomainException {
		FlightManager flightManager = getFlightReservationManager();
		FlightReservation fr = flightManager.reserveFlight(origin, destination,
				id, name);

		// return view
		ReservationVoucher voucher = new ReservationVoucher();
		voucher.setReservationCode(fr.getCode());
		voucher.setFlightNumber(fr.getFlight().getNumber());
		return voucher;
	}

}
