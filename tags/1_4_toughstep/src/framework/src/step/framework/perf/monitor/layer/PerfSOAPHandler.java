package step.framework.perf.monitor.layer;

import java.io.*;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;

import step.framework.ws.SOAPUtil;
import step.framework.ws.XMLUtil;


/**
 *  This class implements the SOAP Handler interface.
 *  Its purpose is intercept and monitor the SOAP layer of
 *  a STEP application before and after extensions.<br />
 *  <br />
 *  It must be configured in the jax-ws binding files.<br />
 *  <br />
 */
public class PerfSOAPHandler implements SOAPHandler<SOAPMessageContext> {

    //
    //  SOAPHandler
    //

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        return monitor(smc);
    }

    public boolean handleFault(SOAPMessageContext smc) {
        return monitor(smc);
    }

    public void close(MessageContext messageContext) {
        // nothing to clean up
    }


    private boolean monitor(SOAPMessageContext smc) {
        try {
            Boolean isOutbound =
                (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            SOAPMessage message = smc.getMessage();
            SOAPPart part = message.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();

            if(!isOutbound) {
                //
                // inbound message
                //

                // identify message payload
                String bodyName = null;
                SOAPElement element = SOAPUtil.getBodyPayload(message);
                if(element != null)
                    bodyName = element.getElementQName().getLocalPart();

                PerfLayerMonitor monitor = MonitorHelper.get();
                monitor.context("soap", "requestBodyName", bodyName);

                // compute request metrics
                XMLUtil.XMLMetrics requestMetrics = XMLUtil.computeMetrics(envelope);

                monitor.context("soap", "requestLogicalLength", Integer.toString(requestMetrics.getLogicalLength()));
                monitor.context("soap", "requestNodeCount", Integer.toString(requestMetrics.nodeCount));
                monitor.context("soap", "requestMaxDepth", Integer.toString(requestMetrics.maxDepth));

                // start clock
                monitor.enter("soap");

            } else {
                //
                // outbound message
                //

                // stop clock
                PerfLayerMonitor monitor = MonitorHelper.get();
                monitor.exit("soap");

                // identify fault payload (stays null if it does not exist)
                String faultName = null;
                SOAPElement element = SOAPUtil.getFaultPayload(message);
                if(element != null)
                    faultName = element.getElementQName().getLocalPart();

                monitor.context("soap", "responseFaultName", faultName);

                // compute response metrics
                XMLUtil.XMLMetrics responseMetrics = XMLUtil.computeMetrics(envelope);

                monitor.context("soap", "responseLogicalLength", Integer.toString(responseMetrics.getLogicalLength()));
                monitor.context("soap", "responseNodeCount", Integer.toString(responseMetrics.nodeCount));
                monitor.context("soap", "responseMaxDepth", Integer.toString(responseMetrics.maxDepth));

            }

        } finally {
            return true;
        }
    }

}
