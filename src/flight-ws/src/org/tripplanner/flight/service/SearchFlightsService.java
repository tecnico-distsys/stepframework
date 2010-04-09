package org.tripplanner.flight.service;

import java.util.*;

import javax.xml.datatype.*;

import org.tripplanner.flight.domain.*;
import org.tripplanner.flight.exception.*;
import org.tripplanner.flight.view.*;

/**
 *  This Flight application service
 *  searches for flights departing from and destined to the input cities.
 *  The results are sorted by ascending price.
 */
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
        SortedSet<Flight> resultSet = flightManager.findAvailableFlights(
            input.getDepart(), input.getArrive());

        // return view
        SearchFlightsOutput output = new SearchFlightsOutput();
        List<FlightView> resultList = output.getFlights();
        for(Flight flight : resultSet) {
            // create FlightView from Flight data
            FlightView flightView = ViewHelper.convert(flight);
            // add to result list
            resultList.add(flightView);
        }
        return output;
    }

}
