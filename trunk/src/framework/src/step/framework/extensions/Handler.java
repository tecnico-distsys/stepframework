package step.framework.extensions;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *  JAX-WS SOAP Handler that intercepts web service message flow
 *  and calls web service interceptor manager to invoke interceptors.
 */
public class Handler implements SOAPHandler<SOAPMessageContext> {

    //
    // Members
    //

    /** Web Service interceptor manager */
    private WebServiceInterceptorManager manager;

    /** Logging */
    private Log log = LogFactory.getLog(Handler.class);


    //
    // Constructors
    //
    public Handler() {
        manager = new WebServiceInterceptorManager();
    }


    //
    // SOAPHandler methods
    //
    public Set<QName> getHeaders() {
        log.trace("getHeaders()");
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        log.trace("handleMessage()");
        return manager.interceptHandleMessageWebServiceHandler(smc);
    }

    public boolean handleFault(SOAPMessageContext smc) {
        log.trace("handleFault()");
        return manager.interceptHandleFaultWebServiceHandler(smc);
    }

    public void close(MessageContext messageContext) {
        log.trace("close()");
        manager.interceptCloseWebServiceHandler(messageContext);
        return;
    }


}
