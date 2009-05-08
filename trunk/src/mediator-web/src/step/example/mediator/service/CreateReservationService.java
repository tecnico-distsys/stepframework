package step.example.mediator.service;

import step.example.mediator.domain.Client;
import step.example.mediator.domain.Reservation;
import step.example.mediator.exception.MediatorDomainException;
import step.example.mediator.exception.UnknownClientException;
import step.example.mediator.view.*;

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