package step.framework.oldextensions;

import step.framework.domain.DomainException;


/**
 *  This abstract class provides a default implementation for
 *  Service interceptor methods.
 */
public abstract class ServiceInterceptorBase implements ServiceInterceptor {

    /**
     *  Empty implementation of before service execution.<br />
     *  <br />
     */
    public void interceptBefore(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException {
    }

    /**
     *  Empty implementation of after service execution.<br />
     *  <br />
     */
    public void interceptAfter(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException {
    }

}
