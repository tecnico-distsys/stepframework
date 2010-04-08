package org.tripplanner.mediator.service;

import step.framework.domain.DomainException;

import org.tripplanner.flight.view.*;
import org.tripplanner.flight.ws.client.service.CreateSingleReservationService;

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
            CreateSingleReservationInput csrInput = new CreateSingleReservationInput();
            // TODO set flight number
            // csrInput.setFlightNumber("123-TODO-testvalue");
            Passenger passenger = new Passenger();
            passenger.setId(id);
            passenger.setName(name);
            csrInput.setPassenger(passenger);

            CreateSingleReservationService csrService = new CreateSingleReservationService(csrInput);

            CreateSingleReservationOutput csrOutput = csrService.execute();

            // invoke mediator's create reservation service
            CreateReservationService crService = new CreateReservationService(
                id, name, csrOutput.getReservationVoucher().getReservationCode());

            ReservationView reservation = crService.execute();

            // return mediator view
            return reservation;

        } catch (DomainException ex) {
            // alternatively each concrete exception case
            // could be handled in a separate catch
            throw new MediatorDomainException(ex.getMessage());
        }
    }

}
