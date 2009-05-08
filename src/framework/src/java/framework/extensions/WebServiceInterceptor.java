package step.framework.extensions;

import javax.xml.ws.soap.SOAPFaultException;


/**
 *  This interface specifies the methods
 *  an extension's web service interceptor
 *  must implement to intercept web service messages.
 */
public interface WebServiceInterceptor extends Interceptor {

    /**
     *  Executed when a web service message is intercepted.<br />
     *  <br />
     *  @param param gives access to configuration and context data, namely
     *         the SOAP message, it's direction, etc.
     *
     *  @return true to continue processing message normally or
     *          false to stop a message going to the server and
     *          make it go towards the client,
     *          keeping its contents just as they are
     *          (this can be used to implement a response message cache)
     *  @throws SOAPFaultException to cancel normal message processing and
     *                             place a specific SOAP fault in the message body
     *  @throws WebServiceInterceptorException to cancel normal message processing
     *                                         and place a SOAP fault with the
     *                                         exception's error message in
     *                                         the message body
     */
    public boolean interceptMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;

}
