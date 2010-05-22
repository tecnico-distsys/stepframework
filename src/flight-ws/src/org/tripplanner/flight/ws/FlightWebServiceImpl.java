package org.tripplanner.flight.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tripplanner.flight.exception.FlightDomainException;
import org.tripplanner.flight.service.CreateFlightReservationService;
import org.tripplanner.flight.view.ReservationVoucher;
import org.tripplanner.flight.wsdl.FlightFault;
import org.tripplanner.flight.wsdl.FlightFault_Exception;
import org.tripplanner.flight.wsdl.FlightPortType;
import org.tripplanner.flight.wsdl.Passenger;
import org.tripplanner.flight.wsdl.ServiceError;
import org.tripplanner.flight.wsdl.ServiceError_Exception;

@javax.jws.WebService(endpointInterface = "org.tripplanner.flight.wsdl.FlightPortType")
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
