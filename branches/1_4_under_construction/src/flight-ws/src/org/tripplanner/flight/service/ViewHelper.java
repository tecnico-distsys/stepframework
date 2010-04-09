package org.tripplanner.flight.service;

import java.util.*;

import javax.xml.datatype.*;

import org.tripplanner.flight.domain.*;
import org.tripplanner.flight.view.*;

/**
 *  This helper class contains methods to
 *  convert domain objects to views.
 */
class ViewHelper {

    /** Create Flight view from Flight entity */
    static FlightView convert(Flight flight) {
        FlightView flightView = new FlightView();

        flightView.setNumber(flight.getNumber());

        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(flight.getDateTime());
        XMLGregorianCalendar xgcal = null;
        try {
            xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch(DatatypeConfigurationException dce) {
            throw new RuntimeException("Could not convert date to XML format", dce);
        }
        flightView.setDate(xgcal);

        flightView.setDepart(flight.getOrigin().getCity());
        flightView.setArrive(flight.getDestination().getCity());

        CurrencyView currencyView = new CurrencyView();
        currencyView.setCode("EUR");
        currencyView.setValue(flight.getPricePerPassenger());
        flightView.setPrice(currencyView);

        return flightView;
    }

}
