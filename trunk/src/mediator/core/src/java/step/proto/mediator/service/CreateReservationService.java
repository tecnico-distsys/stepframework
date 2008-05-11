package step.proto.mediator.service;

import step.proto.mediator.domain.Client;
import step.proto.mediator.domain.Reservation;
import step.proto.mediator.exception.MediatorDomainException;
import step.proto.mediator.exception.UnknownClientException;
import step.proto.mediator.view.ReservationView;

public class CreateReservationService extends MediatorService<ReservationView> {

    private String clientId;
    private String clientName;
    private String reservationCode;
	
    public CreateReservationService(String clientId, String clientName, String reservationCode) {
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