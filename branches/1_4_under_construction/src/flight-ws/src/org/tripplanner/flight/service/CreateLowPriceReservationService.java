package org.tripplanner.flight.service;

import org.tripplanner.flight.domain.*;
import org.tripplanner.flight.exception.*;
import org.tripplanner.flight.view.*;


/**
 *  This Flight application service
 *  creates a reservation for the lowest price flight available
 *  departing from and destined to the input cities.
 */
public class CreateLowPriceReservationService extends
        FlightBaseService<CreateLowPriceReservationOutput> {

    private CreateLowPriceReservationInput input;

    public CreateLowPriceReservationService(CreateLowPriceReservationInput input) {
        this.input = input;
    }

    @Override
    protected CreateLowPriceReservationOutput action() throws FlightDomainException {
        // access domain root
        FlightManager flightManager = getFlightReservationManager();

        // execute business logic
        FlightReservation fr = flightManager.reserveFlight(
            input.getDeparture(), input.getDestination(),
            input.getPassenger().getId(), input.getPassenger().getName());

        // return view
        CreateLowPriceReservationOutput output = new CreateLowPriceReservationOutput();
        ReservationView reservationView = new ReservationView();
        reservationView.setCode(fr.getCode());
		reservationView.setFlightNumber(fr.getFlight().getNumber());
        output.setReservation(reservationView);

        return output;
    }

}
