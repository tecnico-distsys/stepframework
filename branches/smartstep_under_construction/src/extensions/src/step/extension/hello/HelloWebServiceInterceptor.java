package step.extension.hello;

import java.util.Properties;

import javax.xml.ws.soap.SOAPFaultException;

import step.framework.oldextensions.WebServiceInterceptor;
import step.framework.oldextensions.WebServiceInterceptorException;
import step.framework.oldextensions.WebServiceInterceptorParameter;


/**
 *  This is the Hello extension's web service interceptor.
 *  If properly configured, it captures all inbound and outbound SOAP messages.
 */
public class HelloWebServiceInterceptor implements WebServiceInterceptor {

    /** Greet when a message is intercepted (inbound or outbound) */
    public boolean interceptMessage(WebServiceInterceptorParameter param)
    throws SOAPFaultException, WebServiceInterceptorException {
        Properties extConfig = param.getExtension().getConfig();
        String greeting = extConfig.getProperty("greeting");

        boolean isOutbound = param.isOutboundSOAPMessage();

        System.out.println(greeting + " (intercepting " +
                           (isOutbound ? "outbound" : "inbound") +
                           " SOAP message)");

        return true;
    }

}
