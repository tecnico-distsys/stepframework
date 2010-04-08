package org.tripplanner.flight.service;

import org.tripplanner.flight.domain.*;
import org.tripplanner.flight.exception.*;
import org.tripplanner.flight.view.*;


public class SearchFlightsService extends
        FlightBaseService<SearchFlightsOutput> {

    private SearchFlightsInput input;

    public SearchFlightsService(SearchFlightsInput input) {
        this.input = input;
    }

    @Override
    protected SearchFlightsOutput action() throws FlightDomainException {
        // access domain root
        FlightManager flightManager = getFlightReservationManager();

        // execute business logic
        //FlightReservation fr = flightManager.reserveFlight(origin, destination, id, name);

        // return view
        SearchFlightsOutput output = new SearchFlightsOutput();
        return output;
    }

}
