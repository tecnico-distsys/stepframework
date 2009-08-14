package step.framework.extensions;

import step.framework.domain.DomainException;


/**
 *  This interface specifies the methods
 *  an extension's service interceptor
 *  must implement to intercep service execution.
 */
public interface ServiceInterceptor extends Interceptor {

    /**
     *  Executed just before service execution.<br />
     *  <br />
     *  @param param object that provides access to configuration and context data
     *  @throws DomainException to abort service processing due to a domain condition
     *  @throws ServiceInterceptorException to abort service processing due to another condition
     */
    public void interceptBefore(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException;

    /**
     *  Executed just after service execution.<br />
     *  <br />
     *  @param param object that provides access to configuration and context data
     *  @throws DomainException to abort service processing due to a domain condition
     *  @throws ServiceInterceptorException to abort service processing due to another condition
     */
    public void interceptAfter(ServiceInterceptorParameter param)
        throws DomainException, ServiceInterceptorException;

}
