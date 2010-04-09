package org.tripplanner.flight.service;

import org.tripplanner.flight.domain.*;
import org.tripplanner.flight.exception.*;
import org.tripplanner.flight.view.*;


/**
 *  This Flight application service
 *  creates multiple reservations for a specific flight.
 *  Either all or none of the reservations are created.
 */
public class CreateMultipleReservationsService extends
        FlightBaseService<CreateMultipleReservationsOutput> {

    private CreateMultipleReservationsInput input;

    public CreateMultipleReservationsService(CreateMultipleReservationsInput input) {
        this.input = input;
    }

    @Override
    protected CreateMultipleReservationsOutput action() throws FlightDomainException {
        // access domain root
        FlightManager flightManager = getFlightReservationManager();

        // execute business logic
        //FlightReservation fr = flightManager.reserveFlight(origin, destination, id, name);

        // return view
        CreateMultipleReservationsOutput output = new CreateMultipleReservationsOutput();
        return output;
    }

}
