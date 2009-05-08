package step.example.flight.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.example.flight.exception.FlightDomainException;
import step.example.flight.service.CreateFlightReservationService;
import step.example.flight.view.ReservationVoucher;
import step.example.flight.wsdl.FlightFault;
import step.example.flight.wsdl.FlightFault_Exception;
import step.example.flight.wsdl.FlightPortType;
import step.example.flight.wsdl.Passenger;
import step.example.flight.wsdl.ServiceError;
import step.example.flight.wsdl.ServiceError_Exception;

@javax.jws.WebService(endpointInterface = "step.example.flight.wsdl.FlightPortType")
public class FlightWebServiceImpl implements FlightPortType {

	/** logging */
	private Log log = LogFactory.getLog(FlightWebServiceImpl.class);

	/** create reservation */
	public ReservationVoucher createReservation(String departure,
			String arrival, Passenger passenger) throws FlightFault_Exception,
			ServiceError_Exception {

		try {
			// invoke local service
			CreateFlightReservationService service;
			service = new CreateFlightReservationService(departure, arrival,
					passenger.getId(), passenger.getName());
			ReservationVoucher voucher = service.execute();
			return voucher;
		} catch (FlightDomainException e) {
			// log exception
			log.error(e.getMessage(), e);
			// convert domain exceptions to web service faults
			FlightFault ff = new FlightFault();
			ff.setFaultType(e.getClass().getName());
			throw new FlightFault_Exception(e.getMessage(), ff, e);
		} catch (Exception e) {
			// log exception
			log.error(e.getMessage(), e);
			// return fallback web service fault
			throw new ServiceError_Exception(
					"Service currently unavailable. Please try again later.",
					new ServiceError(), e);
		}
	}

}
