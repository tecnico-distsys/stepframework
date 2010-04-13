package org.tripplanner.mediator.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.domain.DomainException;
import step.framework.service.LocalService;

/**
 * Workflow services should not access directly any specific domain. They should
 * only invoke other services to do that.
 */
public abstract class MediatorWorkflowService<T> extends LocalService<T> {
	protected Log log;

	public MediatorWorkflowService() {
		this.log = LogFactory.getLog(this.getClass());
	}

	@Override
	protected abstract T action() throws DomainException;

}
