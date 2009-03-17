package step.proto.flight.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.proto.flight.exception.FlightDomainException;
import step.proto.flight.service.CreateFlightReservationService;
import step.proto.flight.view.ReservationVoucher;
import step.proto.flight.wsdl.FlightFault;
import step.proto.flight.wsdl.FlightFault_Exception;
import step.proto.flight.wsdl.FlightPortType;
import step.proto.flight.wsdl.Passenger;
import step.proto.flight.wsdl.ServiceError;
import step.proto.flight.wsdl.ServiceError_Exception;


@javax.jws.WebService (endpointInterface="step.proto.flight.wsdl.FlightPortType")
public class FlightServiceImpl implements FlightPortType {

    /** logging */
    private Log log = LogFactory.getLog(FlightServiceImpl.class);

    /** create reservation */
    public ReservationVoucher createReservation(String departure,
                                                String arrival,
                                                Passenger passenger)
        throws FlightFault_Exception, ServiceError_Exception {

        try {
            // invoke local service
            CreateFlightReservationService service;
            service = new CreateFlightReservationService(departure,
                                                         arrival,
                                                         passenger.getId(),
                                                         passenger.getName());
            ReservationVoucher voucher = service.execute();
            return voucher;
        } catch(FlightDomainException e) {
            // log exception
            log.error(e.getMessage(), e);
            // convert domain exceptions to web service faults
 	    FlightFault ff = new FlightFault();
 	    ff.setFaultType(e.getClass().getName());
	    throw new FlightFault_Exception(e.getMessage(), ff, e);
        } catch(Exception e) {
            // log exception
            log.error(e.getMessage(), e);
            // return fallback web service fault
            throw new ServiceError_Exception("Service currently unavailable. Please try again later.",
                                             new ServiceError(), e);
        }
    }

}
