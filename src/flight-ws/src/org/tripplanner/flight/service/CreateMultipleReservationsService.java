package org.tripplanner.flight.service;

import java.util.*;

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
        List<PassengerView> passengerList = input.getPassengers();

        CreateMultipleReservationsOutput output = new CreateMultipleReservationsOutput();
        List<ReservationView> reservationList = output.getReservations();

        for(PassengerView passenger : passengerList) {
            FlightReservation flightReservation = flightManager.reserveFlight(
                input.getFlightNumber(), passenger.getId(), passenger.getName());

            ReservationView reservationView = ViewHelper.convert(flightReservation);
            reservationList.add(reservationView);
        }

        // return view
        return output;
    }

}
