package step.example.mediator.ws.client.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.example.mediator.exception.MediatorDomainException;
import step.framework.service.LocalService;

public abstract class MediatorBaseService<T> extends LocalService<T> {
	protected Log log;

	public MediatorBaseService() {
		this.log = LogFactory.getLog(this.getClass());
	}

	// this class may contain auxiliary methods used in all remote invocation
	// services

	@Override
	protected abstract T action() throws MediatorDomainException;

}
