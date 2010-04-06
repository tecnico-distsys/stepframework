package step.examples.tripplanner.mediator.service;

import org.joda.time.LocalDate;

import step.examples.tripplanner.mediator.domain.Client; 
import step.examples.tripplanner.mediator.domain.MediatorApp;
import step.examples.tripplanner.mediator.domain.Reservation;
import step.examples.tripplanner.mediator.view.*;
import step.framework.service.Service;

public class CreateReservationService extends Service<ReservationView> {

	private String clientId;
	private String clientName;
	private String flightCode;
	private LocalDate flightDate;
	private int reservationCode;
	

	public CreateReservationService(String clientId, String clientName,
			String flightCode, LocalDate flightDate, int reservationCode) {
		this.clientId = clientId;
		this.clientName = clientName;
		this.reservationCode = reservationCode;
	}

	@Override
	protected ReservationView action() {
		MediatorApp app = getRoot(); 
		Client client;

		client = app.getClient(clientId);
		if (client == null) {
			client = new Client(clientId, clientName);
			app.addClient(client);
		}
		
		Reservation reservation = client.createReservation(flightCode, flightDate, reservationCode);
		ReservationView view = new ReservationView(reservation.getCode(),
				new ClientView(client.getIdentification(), client.getName()));
		return view;
	}
}