package step.example.flight.service;

import step.example.flight.domain.FlightManager;
import step.example.flight.domain.FlightReservation;
import step.example.flight.exception.FlightDomainException;
import step.example.flight.view.ReservationVoucher;

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
