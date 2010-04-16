package step.framework.perf.monitor;

import java.io.*;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;

import org.apache.commons.logging.*;

import org.perf4j.*;

import step.framework.perf.monitor.*;
import step.framework.ws.SOAPUtil;


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
            Boolean isOutbound = 
                (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            SOAPMessage message = smc.getMessage();

            if(log.isTraceEnabled()) {
                log.trace((isOutbound ? "Outbound" : "Inbound") + " SOAP Message");
            }

            if(!isOutbound) {
                //
                // inbound message
                //
                
                // identify message payload
                String bodyName = null;
                SOAPElement element = SOAPUtil.getBodyPayload(message);
                if(element != null)
                    bodyName = element.getElementQName().getLocalPart();

                // store message name in message context
                smc.put("step.framework.perf.monitor.ws.bodyName", bodyName);

                // start clock
                StopWatchHelper.getThreadStopWatch("soap").start();
            } else {
                //
                // outbound message
                //
                
                // identify fault payload (stays null if it does not exist)
                String faultName = null;
                SOAPElement element = SOAPUtil.getFaultPayload(message);
                if(element != null)
                    faultName = element.getElementQName().getLocalPart();

                // retrieve body name
                String bodyName = (String) smc.get("step.framework.perf.monitor.ws.bodyName");

                // create SOAP message tag name
                StringBuilder tagNameBuilder = new StringBuilder();
                tagNameBuilder.append("soap");
                if(bodyName != null) {
                    tagNameBuilder.append(".").append(bodyName);
                    if(faultName != null) {
                        tagNameBuilder.append(".").append(faultName);
                    }
                }
                String tagName = tagNameBuilder.toString();

                // stop the clock and log using tag
                StopWatchHelper.getThreadStopWatch("soap").stop(tagName);
            }

        } finally {
            return true;
        }
    }

}
