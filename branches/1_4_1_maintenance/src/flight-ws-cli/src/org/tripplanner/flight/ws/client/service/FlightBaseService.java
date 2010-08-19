package org.tripplanner.flight.ws.client.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tripplanner.flight.exception.FlightDomainException;
import step.framework.service.LocalService;

/**
 *  This is Flight application service stub base class.<br />
 *  <br />
 *  It may contain auxiliary methods used by 
 *  all stub services.
 */
public abstract class FlightBaseService<T> extends LocalService<T> {
	protected Log log;

	public FlightBaseService() {
		this.log = LogFactory.getLog(this.getClass());
	}

	@Override
	protected abstract T action() throws FlightDomainException;

}
