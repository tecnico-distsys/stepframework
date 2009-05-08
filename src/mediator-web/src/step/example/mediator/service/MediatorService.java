package step.example.mediator.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.example.mediator.domain.Mediator;
import step.example.mediator.exception.MediatorDomainException;
import step.framework.service.LocalService;

public abstract class MediatorService<T> extends LocalService<T> {
	protected Log log;

	public MediatorService() {
		this.log = LogFactory.getLog(this.getClass());
	}

	protected final Mediator getMediator() {
		return Mediator.getMediator();
	}

	@Override
	protected abstract T action() throws MediatorDomainException;

}
