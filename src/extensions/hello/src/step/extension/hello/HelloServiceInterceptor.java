package step.extension.hello;

import step.framework.domain.DomainException;
import step.framework.extensions.InterceptorException;
import step.framework.extensions.ServiceInterceptor;
import step.framework.service.Service;

/**
 *  This is the Hello extension's service interceptor.
 *  If properly configured, it is invoked before and after a service execution.
 */
public class HelloServiceInterceptor extends ServiceInterceptor {

    /** Greet before the service executes */
    @SuppressWarnings("unchecked")
	public void interceptBefore(Service service) throws DomainException, InterceptorException
    {
        String greeting = (String) getExtension().getProperty("greeting");
        System.out.println(greeting + " (before service execution)");
    }

    /** Greet after the service executes */
    @SuppressWarnings("unchecked")
	public void interceptAfter(Service service) throws DomainException, InterceptorException
    {
        String greeting = (String) getExtension().getProperty("greeting");
        System.out.println(greeting + " (after service execution)");
    }

}
