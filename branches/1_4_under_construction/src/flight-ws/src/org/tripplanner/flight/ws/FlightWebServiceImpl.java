package org.tripplanner.flight.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tripplanner.flight.exception.*;
import org.tripplanner.flight.service.*;
import org.tripplanner.flight.view.*;
import org.tripplanner.flight.wsdl.*;

@javax.jws.WebService(endpointInterface = "org.tripplanner.flight.wsdl.FlightPortType")
public class FlightWebServiceImpl implements FlightPortType {

    /** logging */
    private Log log = LogFactory.getLog(FlightWebServiceImpl.class);


    //
    //  Web Service operations
    //

    public CreateLowPriceReservationOutput createLowPriceReservation(CreateLowPriceReservationInput input)
        throws FlightFault_Exception, ServiceUnavailable_Exception {

        log.debug("createLowPriceReservation");
        CreateLowPriceReservationOutput output = null;
        try {
            // invoke local service
            output = new CreateLowPriceReservationService(input).execute();
            return output;

        } catch (FlightDomainException e) {
            handleDomainException(e);
        } catch (Exception e) {
            handleOtherException(e);
        }
        return output;
    }


    public SearchFlightsOutput searchFlights(SearchFlightsInput input)
        throws FlightFault_Exception, ServiceUnavailable_Exception {

        log.debug("searchFlights");
        SearchFlightsOutput output = null;
        try {
            // invoke local service
            output = new SearchFlightsService(input).execute();
            return output;

        } catch (FlightDomainException e) {
            handleDomainException(e);
        } catch (Exception e) {
            handleOtherException(e);
        }
        return output;
    }


    public CreateSingleReservationOutput createSingleReservation(CreateSingleReservationInput input)
        throws FlightFault_Exception, ServiceUnavailable_Exception {

        log.debug("createSingleReservation");
        CreateSingleReservationOutput output = null;
        try {
            // invoke local service
            output = new CreateSingleReservationService(input).execute();
            return output;

        } catch (FlightDomainException e) {
            handleDomainException(e);
        } catch (Exception e) {
            handleOtherException(e);
        }
        return output;
    }


    public CreateMultipleReservationsOutput createMultipleReservations(CreateMultipleReservationsInput input)
        throws FlightFault_Exception, ServiceUnavailable_Exception {

        log.debug("createMultipleReservations");
        CreateMultipleReservationsOutput output = null;
        try {
            // invoke local service
            output = new CreateMultipleReservationsService(input).execute();
            return output;

        } catch (FlightDomainException e) {
            handleDomainException(e);
        } catch (Exception e) {
            handleOtherException(e);
        }
        return output;
    }


    //
    //  Helper methods
    //

    /** Handling of domain exceptions */
    private void handleDomainException(FlightDomainException e) throws FlightFault_Exception {
        // log exception
        log.error(e.getMessage());
        log.debug("Domain exception details", e);
        // throw fault
        log.trace("Converting domain exception to web service fault");
        FlightFault ff = new FlightFault();
        ff.setFaultType(e.getClass().getName());
        throw new FlightFault_Exception(e.getMessage(), ff, e);
    }

    /** Handling of other exceptions */
    private void handleOtherException(Exception e) throws ServiceUnavailable_Exception {
        // log exception
        log.error(e.getMessage());
        log.debug("Exception details", e);
        // throw fallback fault
        throw new ServiceUnavailable_Exception(
                "Service currently unavailable. Please try again later.",
                new ServiceUnavailable(), e);
    }

}
