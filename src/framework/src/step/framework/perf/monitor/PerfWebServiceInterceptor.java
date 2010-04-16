package step.framework.perf.monitor;

import java.io.*;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.logging.*;

import org.perf4j.*;

import step.framework.perf.monitor.*;

import step.framework.config.*;
import step.framework.context.*;
import step.framework.extensions.*;
import step.framework.ws.*;


/**
 *  This class implements the STEP Extensions Web Service Interceptor interface.
 *  Its purpose is intercept the application's SOAP messages.<br />
 *  <br />
 *  It must be configured in the STEP extensions' configuration files:
 *  extensions.properties and e.g. extension-perf.properties.<br />
 *  <br />
 */
public class PerfWebServiceInterceptor extends WebServiceInterceptorBase {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfWebServiceInterceptor.class);


    //
    //  WebServiceInterceptor
    //

    @Override
    public boolean interceptMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {
        try {
            log.trace("interceptMessage");

            // access SOAP message context
            SOAPMessageContext smc = param.getSOAPMessageContext();
            QName webServiceName = (QName) smc.get(MessageContext.WSDL_SERVICE);

            boolean isServerSide = param.isServerSide();
            boolean isOutbound = param.isOutboundSOAPMessage();
            boolean isFault = param.isFaultSOAPMessage();
            boolean isForcedBackToClient = param.isForcedBackToClient();

            if(log.isTraceEnabled()) {
                String serviceMessage = "Tracing Web Service" +
                                        (isServerSide ? "" : " client") +
                                        ": " + webServiceName;
                log.trace(serviceMessage);
                String messageMessage = (isOutbound ? "Outbound" : "Inbound") +
                                        " SOAP message" +
                                        (isFault ? " containing a Fault" : "") +
                                        (isForcedBackToClient ? " forced back to client" : "");
                log.trace(messageMessage);
            }

            if(isServerSide) {
                if(!isOutbound) {
                    // inbound
                    StopWatchHelper.getThreadStopWatch("wsi").start("wsi");
                } else {
                    // outbound
                    StopWatchHelper.getThreadStopWatch("wsi").stop("wsi");
                }
            }

        } finally {
            // should never stop a message processing
            return true;
        }

    }

}
