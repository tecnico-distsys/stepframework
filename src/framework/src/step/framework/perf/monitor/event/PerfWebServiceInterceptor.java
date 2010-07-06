package step.framework.perf.monitor.event;

import java.io.*;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

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

    //
    //  WebServiceInterceptor
    //

    @Override
    public boolean interceptMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {
        try {
            // access SOAP message context
            SOAPMessageContext smc = param.getSOAPMessageContext();
            QName webServiceName = (QName) smc.get(MessageContext.WSDL_SERVICE);

            boolean isServerSide = param.isServerSide();
            boolean isOutbound = param.isOutboundSOAPMessage();
            boolean isFault = param.isFaultSOAPMessage();
            boolean isForcedBackToClient = param.isForcedBackToClient();

            if(isServerSide) {
                if(!isOutbound) {
                    // inbound
                    MonitorHelper.get().event("enter-wsi");
                } else {
                    // outbound
                    MonitorHelper.get().event("exit-wsi");
                }
            }

        } finally {
            // should never stop a message processing
            return true;
        }

    }

}
