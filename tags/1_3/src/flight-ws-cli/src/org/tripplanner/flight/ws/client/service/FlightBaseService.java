package org.tripplanner.flight.ws.client.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tripplanner.flight.exception.FlightDomainException;
import step.framework.service.LocalService;

public abstract class FlightBaseService<T> extends LocalService<T> {
	protected Log log;

	public FlightBaseService() {
		this.log = LogFactory.getLog(this.getClass());
	}

	// this class may contain auxiliary methods used in all remote invocation
	// services

	@Override
	protected abstract T action() throws FlightDomainException;

}
