package step.framework.extensions;


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
        throws ServiceInterceptorException {
    }

    /**
     *  Empty implementation of after service execution.<br />
     *  <br />
     */
    public void interceptAfter(ServiceInterceptorParameter param)
        throws ServiceInterceptorException {
    }

}
