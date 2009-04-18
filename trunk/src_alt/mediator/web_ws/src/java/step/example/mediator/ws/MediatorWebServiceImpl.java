package step.example.mediator.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.example.mediator.wsdl.*;
import step.example.mediator.exception.MediatorDomainException;
import step.example.mediator.view.*;
import step.example.mediator.service.CreateReservationService;

@javax.jws.WebService(endpointInterface = "step.example.mediator.wsdl.MediatorPortType")
public class MediatorWebServiceImpl implements MediatorPortType {

	/** logging */
	private Log log = LogFactory.getLog(MediatorWebServiceImpl.class);

	/** create reservation */
	public ReservationView createReservation(String clientId,
			String clientName, String reservationCode)
			throws MediatorFault_Exception, ServiceError_Exception {

		try {
			// invoke local service
			CreateReservationService service;
			service = new CreateReservationService(clientId, clientName,
					reservationCode);
			ReservationView view = service.execute();
			return view;
		} catch (MediatorDomainException e) {
			// log exception
			log.error(e.getMessage(), e);
			// convert domain exceptions to web service faults
			MediatorFault ff = new MediatorFault();
			ff.setFaultType(e.getClass().getName());
			throw new MediatorFault_Exception(e.getMessage(), ff, e);
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
