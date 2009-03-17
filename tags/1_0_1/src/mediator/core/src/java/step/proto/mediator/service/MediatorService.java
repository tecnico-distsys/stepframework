package step.proto.mediator.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.proto.mediator.domain.Mediator;
import step.proto.mediator.exception.MediatorDomainException;
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
