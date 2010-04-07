package step.framework.perf.monitor.ws;

import java.io.*;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.*;

import org.perf4j.*;

import step.framework.perf.monitor.*;


/**
 *  This class implements the SOAP Handler interface.
 *  Its purpose is intercept and monitor the SOAP layer of
 *  a STEP application before and after extensions.<br />
 *  <br />
 *  It must be configured in the jax-ws binding files.<br />
 *  <br />
 */
public class PerfSOAPHandler implements SOAPHandler<SOAPMessageContext> {

    /** Logging */
    private static Log log = LogFactory.getLog(PerfSOAPHandler.class);


    //
    //  SOAPHandler
    //

    public Set<QName> getHeaders() {
        log.trace("getHeaders");
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        log.trace("handleMessage");
        return monitor(smc);
    }

    public boolean handleFault(SOAPMessageContext smc) {
        log.trace("handleFault");
        return monitor(smc);
    }

    public void close(MessageContext messageContext) {
        log.trace("close");
        // nothing to clean up
    }


    private boolean monitor(SOAPMessageContext smc) {
        try {
            Boolean isOutbound = (Boolean)
                smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

            if(log.isTraceEnabled()) {
                log.trace((isOutbound ? "Outbound" : "Inbound") + " SOAP Message");
            }

            //SOAPMessage message = smc.getMessage();

            if(!isOutbound) {
                // inbound
                StopWatchHelper.getThreadStopWatch("soap").start("soap");
            } else {
                // outbound
                StopWatchHelper.getThreadStopWatch("soap").stop("soap");
            }

        } finally {
            return true;
        }
    }

}
