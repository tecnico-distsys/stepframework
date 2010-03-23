package org.tripplanner.mediator.service;

import org.tripplanner.mediator.domain.Client;
import org.tripplanner.mediator.domain.Reservation;
import org.tripplanner.mediator.exception.MediatorDomainException;
import org.tripplanner.mediator.exception.UnknownClientException;
import org.tripplanner.mediator.view.*;

public class CreateReservationService extends MediatorService<ReservationView> {

	private String clientId;
	private String clientName;
	private String reservationCode;

	public CreateReservationService(String clientId, String clientName,
			String reservationCode) {
		this.clientId = clientId;
		this.clientName = clientName;
		this.reservationCode = reservationCode;
	}

	@Override
	protected ReservationView action() throws MediatorDomainException {
		Client client;

		try {
			client = getMediator().getClient(clientId);
		} catch (UnknownClientException ex) {
			client = getMediator().createClient(clientId, clientName);
		}

		Reservation reservation = client.createReservation(reservationCode);
		ReservationView view = new ReservationView();
		view.setReservationCode(reservation.getCode());
		view.setClientId(reservation.getClient().getIdentification());
		return view;
	}

}