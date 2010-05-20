package step.extension.hello;

import java.util.Properties;

import step.framework.domain.DomainException;
import step.framework.oldextensions.ServiceInterceptor;
import step.framework.oldextensions.ServiceInterceptorException;
import step.framework.oldextensions.ServiceInterceptorParameter;


/**
 *  This is the Hello extension's service interceptor.
 *  If properly configured, it is invoked before and after a service execution.
 */
public class HelloServiceInterceptor implements ServiceInterceptor {

    /** Greet before the service executes */
    public void interceptBefore(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {
        Properties extConfig = param.getExtension().getConfig();
        String greeting = extConfig.getProperty("greeting");
        System.out.println(greeting + " (before service execution)");
    }

    /** Greet after the service executes */
    public void interceptAfter(ServiceInterceptorParameter param)
    throws DomainException, ServiceInterceptorException {
        Properties extConfig = param.getExtension().getConfig();
        String greeting = extConfig.getProperty("greeting");
        System.out.println(greeting + " (after service execution)");
    }

}
