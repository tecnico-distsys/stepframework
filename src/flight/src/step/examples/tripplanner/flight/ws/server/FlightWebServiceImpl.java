package step.examples.tripplanner.flight.ws.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.examples.tripplanner.flight.exception.FlightException;
import step.examples.tripplanner.flight.service.BookFlightService;
import step.examples.tripplanner.flight.ws.view.ReservationVoucher;
//import step.examples.tripplanner.flight.ws.FlightFault;
import step.examples.tripplanner.flight.ws.FlightFault;
import step.examples.tripplanner.flight.ws.FlightFault_Exception;
import step.examples.tripplanner.flight.ws.FlightPortType;
import step.examples.tripplanner.flight.ws.Passenger;
import step.examples.tripplanner.flight.ws.ServiceError;
import step.examples.tripplanner.flight.ws.ServiceError_Exception;

@javax.jws.WebService(endpointInterface = "step.examples.tripplanner.flight.ws.FlightPortType")
public class FlightWebServiceImpl implements FlightPortType {

	/** logging */
	private Log log = LogFactory.getLog(FlightWebServiceImpl.class);

	/** create reservation */
	public ReservationVoucher createReservation(String departure,
			String arrival, Passenger passenger) throws FlightFault_Exception,
			ServiceError_Exception {

		try {
			// invoke local service
			BookFlightService service;
			service = new BookFlightService(departure, arrival,
					passenger.getId(), passenger.getName());
			step.examples.tripplanner.flight.view.ReservationVoucher voucher = service.execute();
			
			ReservationVoucher result = new ReservationVoucher();
			result.setFlightNumber(voucher.getFlightNumber());
			result.setReservationCode(voucher.getReservationCode());
			result.setFlightDate(voucher.getFlightDate());
			return result;
		} catch (FlightException e) {
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
