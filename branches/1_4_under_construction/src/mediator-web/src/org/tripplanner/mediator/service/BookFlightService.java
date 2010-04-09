package org.tripplanner.mediator.service;

import step.framework.domain.DomainException;

import org.tripplanner.flight.view.PassengerView;
import org.tripplanner.flight.view.CreateLowPriceReservationInput;
import org.tripplanner.flight.view.CreateLowPriceReservationOutput;
import org.tripplanner.flight.ws.client.service.CreateLowPriceReservationService;

import org.tripplanner.mediator.exception.MediatorDomainException;
import org.tripplanner.mediator.service.CreateReservationService;
import org.tripplanner.mediator.view.*;

public class BookFlightService extends MediatorWorkflowService<ReservationView> {

    private String origin;
    private String destination;
    private String id;
    private String name;

    public BookFlightService(String origin, String destination,
        String id, String name) {

        this.origin = origin;
        this.destination = destination;
        this.id = id;
        this.name = name;
    }

    @Override
    protected ReservationView action() throws DomainException {
        try {
            // invoke flight's create single reservation service
            //
            CreateLowPriceReservationInput remoteInput =
                new CreateLowPriceReservationInput();
            remoteInput.setDeparture(origin);
            remoteInput.setDestination(destination);
            PassengerView passengerView = new PassengerView();
            passengerView.setId(id);
            passengerView.setName(name);
            remoteInput.setPassenger(passengerView);

            CreateLowPriceReservationService remoteService =
                new CreateLowPriceReservationService(remoteInput);

            CreateLowPriceReservationOutput remoteOutput =
                remoteService.execute();

            // invoke mediator's create reservation service
            CreateReservationService localService = new CreateReservationService(
                id, name, remoteOutput.getReservation().getCode());

            ReservationView reservation = localService.execute();

            // return mediator view
            return reservation;

        } catch (DomainException ex) {
            // alternatively each concrete exception case
            // could be handled in a separate catch
            throw new MediatorDomainException(ex.getMessage());
        }
    }

}
