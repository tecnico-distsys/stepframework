package org.tripplanner.flight.domain;

import java.util.Comparator;

public class FlightComparator implements Comparator<Flight> {

    public int compare(Flight f1, Flight f2) {
        return f1.getPricePerPassenger().compareTo(f2.getPricePerPassenger());
    }

}
