package step.framework.oldextensions;

import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.ws.SOAPUtil;


/**
 *  An instance of this class is provided as parameter
 *  to each web service interceptor
 *  and enables it to access configuration and context data.
 */
public class WebServiceInterceptorParameter extends InterceptorParameter {

    //
    //  Members
    //
    private SOAPMessageContext smc;


    //
    //  Constructors
    //
    WebServiceInterceptorParameter(ExtensionEngine engine,
                                   Extension extension,
                                   SOAPMessageContext smc) {
        super(engine, extension);
        ExtensionsUtil.throwIllegalArgIfNull(smc,
            "SOAP message context for web service interceptor parameter cannot be null");
        this.smc = smc;
    }


    //
    //  Context access methods
    //

    /**
     *  Access the current SOAP message context.
     */
    public SOAPMessageContext getSOAPMessageContext() {
        return this.smc;
    }

    /**
     *  Check if message is outbound.
     */
    public boolean isOutboundSOAPMessage() {
        return SOAPUtil.isOutboundMessage(this.smc);
    }

    /**
     *  Check if message is a SOAP Fault
     */
    public boolean isFaultSOAPMessage() {
        try {
            return SOAPUtil.isFaultMessage(this.smc);
        } catch(SOAPException se) {
            // throw an unchecked exception if SOAP message is in an illegal state
            throw new IllegalStateException(se);
        }
    }

    /**
     *  Check if message is being processed on the server-side.
     */
    public boolean isServerSide() {
        return SOAPUtil.isServerSideMessage(this.smc);
    }

    /**
     *  Check if message is being forced to go back to client.<br />
     *  <br />
     *  It can be forced by a return false,
     *  or by a thrown WebServiceInterceptorException,
     *  or by a thrown SOAPFaultException.<br />
     *  <br />
     *  This condition is only signaled on the machine (client/server)
     *  where it occurred.<br />
     */
    public boolean isForcedBackToClient() {
        return SOAPUtil.isForcedBackToClient(this.smc);
    }

}
