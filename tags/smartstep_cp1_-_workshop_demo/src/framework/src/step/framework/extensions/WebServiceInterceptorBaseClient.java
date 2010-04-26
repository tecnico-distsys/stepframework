package step.framework.extensions;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *  This abstract class provides friendlier intercept methods for a
 *  client-side Web Service interceptor.
 */
public abstract class WebServiceInterceptorBaseClient extends WebServiceInterceptorBase {

    //
    //  Friendlier client-side methods that will have to be overriden
    //

    /**
     *  Intercept outbound request message.
     */
    public abstract boolean interceptOutboundRequest(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;

    /**
     *  Intercept inbound response message.
     */
    public abstract boolean interceptInboundResponse(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;

    /**
     *  Intercept inbound fault message.
     */
    public abstract boolean interceptInboundFault(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;


    //
    //  Members
    //

    /** logging */
    private Log log = LogFactory.getLog(WebServiceInterceptorBaseClient.class);


    //
    //  WebServiceInterceptor methods
    //

    /**
     *  Final implementation of intercept inbound message.
     *  Checks if interceptor is running on expected side
     *  and detects if it's a fault message.
     */
    public final boolean interceptInboundMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {

        checkClientSide(param);

        boolean isFault = param.isFaultSOAPMessage();
        if(isFault) {
            log.trace("mapping inbound message to inbound fault on the client-side");
            return interceptInboundFault(param);
        } else {
            log.trace("mapping inbound message to inbound response on the client-side");
            return interceptInboundResponse(param);
        }
    }

    /**
     *  Final implementation of intercept outbound message.
     *  Checks if interceptor is running on expected side
     *  and detects if it's a fault message.
     */
    public final boolean interceptOutboundMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {

       checkClientSide(param);

        boolean isFault = param.isFaultSOAPMessage();
        if(isFault) {
            log.warn("ignoring outbound SOAP fault on the client-side - this message situation is not supposed to happen!");
            return true;
        } else {
            log.trace("mapping outbound message to outbound request on the client-side");
            return interceptOutboundRequest(param);
        }
    }

    // Helper method to log a warning if interceptor is not being run on the client-side
    private boolean checkClientSide(WebServiceInterceptorParameter param) {
        boolean isClientSide = !(param.isServerSide());
        if(!isClientSide) {
            log.warn("Web Service interceptor extending client base is not being run on the client-side!");
        }
        return isClientSide;
    }

}
