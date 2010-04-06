package step.examples.tripplanner.flight.ws.client.service;

import javax.xml.ws.WebServiceException;

import step.examples.tripplanner.flight.exception.FlightException;
import step.examples.tripplanner.flight.view.ReservationVoucher;
import step.examples.tripplanner.flight.ws.client.FlightStubFactory;
import step.examples.tripplanner.flight.ws.FlightFault_Exception;
import step.examples.tripplanner.flight.ws.FlightPortType;
import step.examples.tripplanner.flight.ws.Passenger;
import step.examples.tripplanner.flight.ws.ServiceError_Exception;
import step.framework.exception.ExceptionParser;
import step.framework.exception.RemoteServiceException;
import step.framework.service.Service;
import step.framework.ws.StubFactoryException;

public class BookFlightService extends
		Service<ReservationVoucher> {

	/* service members */
	private String origin;
	private String destination;
	private String id;
	private String name;

	/* constructor */
	public BookFlightService(String origin, String destination,
			String id, String name) {
		this.origin = origin;
		this.destination = destination;
		this.id = id;
		this.name = name;
	}

	@Override
	protected step.examples.tripplanner.flight.view.ReservationVoucher action() {
		try {
			// create Web Service stub
			FlightPortType port = FlightStubFactory.getInstance()
					.getPortUsingConfig();

			// invoke remote service
			Passenger passenger = new Passenger();
			passenger.setId(this.id);
			passenger.setName(this.name);

			step.examples.tripplanner.flight.ws.view.ReservationVoucher voucher = port.createReservation(this.origin,
					this.destination, passenger);

			log.debug("Reservation code: " + voucher.getReservationCode());
			log
					.debug("Reservation flight number: "
							+ voucher.getFlightNumber());

			// return view
			return new ReservationVoucher(voucher.getReservationCode(), voucher.getFlightNumber(),
					voucher.getFlightDate());
		} catch (FlightFault_Exception e) {
			// remote domain exception
			log.error(e);
			FlightException ex = ExceptionParser.parse(e.getFaultInfo()
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
