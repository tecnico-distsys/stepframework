package step.framework.extensions;

import step.framework.domain.DomainException;
import step.framework.service.Service;

//TODO: annotations to define what services it intercepts???
public abstract class ServiceInterceptor extends Interceptor {
	
	//***********************************************************************
	//abstract methods that need to be implemented
	
	@SuppressWarnings("unchecked")
	public abstract void interceptBefore(Service svc) throws DomainException, InterceptorException;	
	@SuppressWarnings("unchecked")
	public abstract void interceptAfter(Service svc) throws DomainException, InterceptorException;
}
