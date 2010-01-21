package org.tripplanner.mediator.service;

import org.tripplanner.flight.view.ReservationVoucher;
import org.tripplanner.flight.ws.client.service.CreateFlightReservationService;
import org.tripplanner.mediator.exception.MediatorDomainException;
import org.tripplanner.mediator.service.CreateReservationService;
import org.tripplanner.mediator.view.*;
import step.framework.domain.DomainException;

public class BookFlightService extends MediatorWorkflowService<ReservationView> {
	private String origin;
	private String destination;
	private String id;
	private String name;

	public BookFlightService(String origin, String destination, String id,
			String name) {
		this.origin = origin;
		this.destination = destination;
		this.id = id;
		this.name = name;
	}

	@Override
	protected ReservationView action() throws DomainException {
		try {
			ReservationVoucher voucher = new CreateFlightReservationService(
					origin, destination, id, name).execute();
			ReservationView reservation = new CreateReservationService(id,
					name, voucher.getReservationCode()).execute();
			return reservation;
		} catch (DomainException ex) { // alternatively each concrete exception
			// case could be handled in a separate
			// catch
			throw new MediatorDomainException(ex.getMessage());
		}
	}
}
