package step.example.flight.ws.client.service;

import javax.xml.ws.WebServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.example.flight.exception.FlightDomainException;
import step.example.flight.view.ReservationVoucher;
import step.example.flight.ws.client.FlightStubFactory;
import step.example.flight.wsdl.FlightFault_Exception;
import step.example.flight.wsdl.FlightPortType;
import step.example.flight.wsdl.Passenger;
import step.example.flight.wsdl.ServiceError_Exception;
import step.framework.exception.ExceptionParser;
import step.framework.exception.RemoteServiceException;
import step.framework.ws.StubFactoryException;

public class CreateFlightReservationService extends
		FlightBaseService<ReservationVoucher> {

	/** logging */
	private Log log = LogFactory.getLog(CreateFlightReservationService.class);

	/* service members */
	private String origin;
	private String destination;
	private String id;
	private String name;

	/* constructor */
	public CreateFlightReservationService(String origin, String destination,
			String id, String name) {
		this.origin = origin;
		this.destination = destination;
		this.id = id;
		this.name = name;
	}

	@Override
	protected ReservationVoucher action() throws FlightDomainException {
		try {
			// create Web Service stub
			FlightPortType port = FlightStubFactory.getInstance()
					.getPortUsingConfig();

			// invoke remote service
			Passenger passenger = new Passenger();
			passenger.setId(this.id);
			passenger.setName(this.name);

			ReservationVoucher voucher = port.createReservation(this.origin,
					this.destination, passenger);

			log.debug("Reservation code: " + voucher.getReservationCode());
			log
					.debug("Reservation flight number: "
							+ voucher.getFlightNumber());

			return voucher;
		} catch (FlightFault_Exception e) {
			// remote domain exception
			log.error(e);
			FlightDomainException ex = ExceptionParser.parse(e.getFaultInfo()
					.getFaultType(), e.getMessage());
			throw ex;
		} catch (ServiceError_Exception e) {
			// remote service error
			log.error(e);
			throw new RemoteServiceException(e);
		} catch (StubFactoryException e) {
			// stub creation error
			log.error(e);
			throw new RemoteServiceException(e);
		} catch (WebServiceException e) {
			// communication error (wrong address, connection closed, ...)
			log.error(e);
			throw new RemoteServiceException(e);
		}

	}

}
