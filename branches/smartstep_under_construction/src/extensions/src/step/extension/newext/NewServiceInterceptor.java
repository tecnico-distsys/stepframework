package step.extension.newext;

import step.framework.domain.DomainException;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.InterceptorException;
import step.framework.service.Service;

public class NewServiceInterceptor extends ServiceInterceptor {

	@SuppressWarnings("unchecked")
	public void interceptAfter(Service svc) throws DomainException, InterceptorException
	{
		System.out.println("[DEBUG] Intercepting after Service: " + svc.getClass().getSimpleName());
	}

	@SuppressWarnings("unchecked")
	public void interceptBefore(Service svc) throws DomainException, InterceptorException
	{
		System.out.println("[DEBUG] Intercepting before Service: " + svc.getClass().getSimpleName());
	}

}
