package step.framework.extensions;

import javax.xml.ws.soap.SOAPFaultException;


/**
 *  This abstract class provides a default implementation for
 *  Web Service interceptor methods.
 */
public abstract class WebServiceInterceptorBase implements WebServiceInterceptor {

    /**
     *  Empty implementation of intercept message.
     *  Returns true so message processing can proceed.
     */
    public boolean interceptMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {
        return true;
    }

}
