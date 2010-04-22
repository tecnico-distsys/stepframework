package step.framework.extensions;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *  This abstract class provides friendlier intercept methods for a
 *  server-side Web Service interceptor.
 */
public abstract class WebServiceInterceptorBaseServer extends WebServiceInterceptorBase {

    //
    //  Friendlier server-side methods that will have to be overriden
    //

    /**
     *  Intercept inbound request message.
     */
    public abstract boolean interceptInboundRequest(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;

    /**
     *  Intercept outbound response message.
     */
    public abstract boolean interceptOutboundResponse(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;

    /**
     *  Intercept outbound fault message.
     */
    public abstract boolean interceptOutboundFault(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException;


    //
    //  Members
    //

    /** logging */
    private Log log = LogFactory.getLog(WebServiceInterceptorBaseServer.class);


    //
    //  WebServiceInterceptor methods
    //

    /**
     *  Final implementation of intercept outbound message.
     *  Checks if interceptor is running on expected side
     *  and detects if it's a fault message.
     */
    public final boolean interceptInboundMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {

        checkServerSide(param);

        boolean isFault = param.isFaultSOAPMessage();
        if(isFault) {
            log.warn("ignoring inbound SOAP fault on the server-side - this message situation is not supposed to happen!");
            return true;
        } else {
            log.trace("mapping inbound to inbound request on the server-side");
            return interceptInboundRequest(param);
        }
    }

    /**
     *  Final implementation of intercept outbound message.
     *  Checks if interceptor is running on expected side
     *  and detects if it's a fault message.
     */
     public final boolean interceptOutboundMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {

        checkServerSide(param);

        boolean isFault = param.isFaultSOAPMessage();
        if(isFault) {
            log.trace("mapping outbound message to outbound fault on the server-side");
            return interceptOutboundFault(param);
        } else {
            log.trace("mapping outbound message to outbound response on the server-side");
            return interceptOutboundResponse(param);
        }
    }

    // Helper method to log a warning if interceptor is not being run on the server-side
    private boolean checkServerSide(WebServiceInterceptorParameter param) {
        boolean isServerSide = param.isServerSide();
        if(!isServerSide) {
            log.warn("Web Service interceptor extending server base is not being run on the server-side!");
        }
        return isServerSide;
    }

}
