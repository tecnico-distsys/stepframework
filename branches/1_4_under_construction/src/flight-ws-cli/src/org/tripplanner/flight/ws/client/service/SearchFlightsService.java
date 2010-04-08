package org.tripplanner.flight.ws.client.service;

import javax.xml.ws.WebServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.exception.ExceptionParser;
import step.framework.exception.RemoteServiceException;
import step.framework.ws.StubFactoryException;

import org.tripplanner.flight.exception.*;
import org.tripplanner.flight.view.*;
import org.tripplanner.flight.ws.client.FlightStubFactory;
import org.tripplanner.flight.wsdl.*;


public class SearchFlightsService extends
        FlightBaseService<SearchFlightsOutput> {

    private SearchFlightsInput input;

    public SearchFlightsService(SearchFlightsInput input) {
        this.input = input;
    }

    @Override
    protected SearchFlightsOutput action() throws FlightDomainException {
        try {
            // create Web Service stub
            FlightPortType port =
                FlightStubFactory.getInstance().getPortUsingConfig();

            // invoke remote Web Service
            SearchFlightsOutput output = port.searchFlights(input);
            return output;

        } catch (FlightFault_Exception e) {
            // recover remote domain exception
            FlightDomainException de =
                ExceptionParser.parse(
                    e.getFaultInfo().getFaultType(), e.getMessage());
            throw de;
        } catch (ServiceUnavailable_Exception e) {
            // remote service
            throw new RemoteServiceException(e);
        } catch (StubFactoryException e) {
            // stub creation error
            throw new RemoteServiceException(e);
        } catch (WebServiceException e) {
            // communication error (wrong address, connection closed, etc)
            throw new RemoteServiceException(e);
        }

    }

}
