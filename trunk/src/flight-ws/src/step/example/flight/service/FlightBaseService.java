package step.example.flight.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.example.flight.domain.FlightManager;
import step.example.flight.exception.FlightDomainException;
import step.framework.service.LocalService;

public abstract class FlightBaseService<T> extends LocalService<T> {
	protected Log log;

	public FlightBaseService() {
		this.log = LogFactory.getLog(this.getClass());
	}

	protected final FlightManager getFlightReservationManager() {
		return FlightManager.getInstance();
	}

	@Override
	protected abstract T action() throws FlightDomainException;
}
