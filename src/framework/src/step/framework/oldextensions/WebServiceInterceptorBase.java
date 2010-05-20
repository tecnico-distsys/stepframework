package step.framework.oldextensions;

import javax.xml.ws.soap.SOAPFaultException;


/**
 *  This abstract class provides friendlier intercept methods for a
 *  Web Service interceptor.
 */
public abstract class WebServiceInterceptorBase implements WebServiceInterceptor {

    //
    //  Friendlier methods that will have to be overriden
    //

    /**
     *  Intercept inbound message.
     */
    public abstract boolean interceptInboundMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;

    /**
     *  Intercept outbound message.
     */
    public abstract boolean interceptOutboundMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;


    //
    //  WebServiceInterceptor method
    //

    /**
     *  Final implementation of intercept message.
     *  Checks message direction and calls appropriate method.
     */
    public final boolean interceptMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {

        boolean isOutbound = param.isOutboundSOAPMessage();

        if(isOutbound) {
            return interceptOutboundMessage(param);
        } else {
            return interceptInboundMessage(param);
        }
    }

}
