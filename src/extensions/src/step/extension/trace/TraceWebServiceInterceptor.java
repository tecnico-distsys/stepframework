package step.extension.trace;

import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import step.framework.config.ConfigUtil;
import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.WebServiceInterceptorException;
import step.framework.extensions.WebServiceInterceptorParameter;
import step.framework.ws.XMLUtil;

/**
 *  This is the Trace extension's web service interceptor.
 *  If properly configured, it captures all inbound and outbound SOAP messages.
 */
public class TraceWebServiceInterceptor implements WebServiceInterceptor {

    /**
     *   Prints the intercepted SOAP messages.
     */
    @SuppressWarnings("finally")
	public boolean interceptMessage(WebServiceInterceptorParameter param)
    throws SOAPFaultException, WebServiceInterceptorException {
        // where to write the information
        PrintStream out = getPrintStream(param);

        try {
            // access SOAP message context
            SOAPMessageContext smc = param.getSOAPMessageContext();

            // print part of SOAP message context
            QName webServiceName = (QName) smc.get(MessageContext.WSDL_SERVICE);

            boolean isServerSide = param.isServerSide();
            boolean isOutbound = param.isOutboundSOAPMessage();
            boolean isFault = param.isFaultSOAPMessage();
            boolean isForcedBackToClient = param.isForcedBackToClient();

            out.println("Tracing Web Service" +
                        (isServerSide ? "" : " client") +
                        ": " + webServiceName);
            out.println((isOutbound ? "Outbound" : "Inbound") +
                        " SOAP message" +
                        (isFault ? " containing a Fault" : "") +
                        (isForcedBackToClient ? " forced back to client" : "") +
                        ":");

            // print SOAP message contents
            SOAPMessage soapMessage = smc.getMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            // check printing style
            Properties extConfig = param.getExtension().getConfig();
            boolean prettyPrint = ConfigUtil.recognizeAsTrue(extConfig.getProperty("pretty-print"));
            if(prettyPrint) {
                XMLUtil.prettyPrint(soapPart, out);
            } else {
                soapMessage.writeTo(out);
                out.println();
            }

        } catch(SOAPException e) {
            out.println("<cannot access SOAP message>");
            out.println(e.getClass().getName() + " " + e.getMessage());

        } catch(RuntimeException e) {
            // print runtime exception because it will be swallowed by finally's return
            out.println("<runtime exception>");
            out.println(e.getClass().getName() + " " + e.getMessage());
            throw e;

        } finally {
            // trace should never stop a message processing
            return true;
        }
    }

    /** Get the configured output print stream */
    private PrintStream getPrintStream(WebServiceInterceptorParameter param) {
        Map<String,Object> extContext = param.getExtension().getContext();
        synchronized(extContext) {
            return (PrintStream) extContext.get("output");
        }
    }

}
