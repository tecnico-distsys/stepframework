package org.tripplanner.flight.service;

import org.tripplanner.flight.domain.*;
import org.tripplanner.flight.exception.*;
import org.tripplanner.flight.view.*;


/**
 *  This Flight application service
 *  creates a single reservation for a specific flight.
 */
public class CreateSingleReservationService extends
        FlightBaseService<CreateSingleReservationOutput> {

    private CreateSingleReservationInput input;

    public CreateSingleReservationService(CreateSingleReservationInput input) {
        this.input = input;
    }

    @Override
    protected CreateSingleReservationOutput action() throws FlightDomainException {
        // access domain root
        FlightManager flightManager = getFlightReservationManager();

        // execute business logic
        // TODO use flight number instead of fixed origin and destination
        FlightReservation fr = flightManager.reserveFlight(
            "Lisbon", "New York", input.getPassenger().getId(), input.getPassenger().getName());

        // return view
        CreateSingleReservationOutput output = new CreateSingleReservationOutput();
        ReservationView reservationView = new ReservationView();
        reservationView.setCode(fr.getCode());
		reservationView.setFlightNumber(fr.getFlight().getNumber());
        output.setReservation(reservationView);

        return output;
    }

}
