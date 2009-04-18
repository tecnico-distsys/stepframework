package step.example.mediator.ws.client.service;

import javax.xml.ws.WebServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.example.mediator.view.*;
import step.example.mediator.wsdl.*;
import step.example.mediator.exception.MediatorDomainException;
import step.example.mediator.ws.client.MediatorStubFactory;
import step.framework.exception.ExceptionParser;
import step.framework.exception.RemoteServiceException;
import step.framework.ws.StubFactoryException;

public class CreateReservationService extends
		MediatorBaseService<ReservationView> {

	/** logging */
	private Log log = LogFactory.getLog(CreateReservationService.class);

	private String clientId;
	private String clientName;
	private String reservationCode;

	public CreateReservationService(String clientId, String clientName,
			String reservationCode) {
		this.clientId = clientId;
		this.clientName = clientName;
		this.reservationCode = reservationCode;
	}

	@Override
	protected ReservationView action() throws MediatorDomainException {
		try {
			// create Web Service stub
			MediatorPortType port = MediatorStubFactory.getInstance()
					.getPortUsingConfig();

			ReservationView view = port.createReservation(this.clientId,
					this.clientName, this.reservationCode);

			log.debug("Reservation code: " + view.getReservationCode());
			log.debug("Reservation client id: " + view.getClientId());

			return view;

		} catch (MediatorFault_Exception e) {
			// remote domain exception
			log.error(e);
			MediatorDomainException ex = ExceptionParser.parse(e.getFaultInfo()
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
