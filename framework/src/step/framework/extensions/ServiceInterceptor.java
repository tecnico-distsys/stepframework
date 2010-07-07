package step.framework.extensions;


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
     *  @throws ServiceInterceptorException to abort service processing due to another condition
     */
    public void interceptBefore(ServiceInterceptorParameter param)
        throws ServiceInterceptorException;

    /**
     *  Executed just after service execution.<br />
     *  <br />
     *  @param param object that provides access to configuration and context data
     *  @throws ServiceInterceptorException to abort service processing due to another condition
     */
    public void interceptAfter(ServiceInterceptorParameter param)
        throws ServiceInterceptorException;

}
