package step.extension.trace;

import java.io.IOException;
import java.io.PrintStream;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.neethi.Policy;

import step.framework.config.ConfigUtil;
import step.framework.extensions.Extension;
import step.framework.extensions.InterceptorException;
import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.pipe.policy.PolicyPipeFactory;
import step.framework.policy.PolicyUtil;
import step.framework.ws.SOAPUtil;
import step.framework.ws.XMLUtil;

/**
 *  This is the Trace extension's web service interceptor.
 *  If properly configured, it captures all inbound and outbound SOAP messages.
 */
public class TraceWebServiceInterceptor extends WebServiceInterceptor {

    /**
     *   Prints the intercepted SOAP messages.
     */
	@SuppressWarnings("finally")
	public boolean interceptClientInbound(SOAPMessageContext smc) throws InterceptorException
	{
        // where to write the information
        PrintStream out = getPrintStream();

        try
        {
            printState(out, smc, false, false);
            printMessage(out, smc);
            printPolicy(out);
        }
        catch(SOAPException e)
        {
            out.println("<cannot access SOAP message>");
            out.println(e.getClass().getName() + " " + e.getMessage());
        }
        catch(RuntimeException e)
        {
            // print runtime exception because it will be swallowed by finally's return
            out.println("<runtime exception>");
            out.println(e.getClass().getName() + " " + e.getMessage());
            throw e;
        } 
        finally
        {
            // trace should never stop a message processing
            return true;
        }
	}

    /**
     *   Prints the intercepted SOAP messages.
     */
	@SuppressWarnings("finally")
	public boolean interceptClientOutbound(SOAPMessageContext smc) throws InterceptorException
	{
        // where to write the information
        PrintStream out = getPrintStream();

        try
        {
            printState(out, smc, false, true);
            printMessage(out, smc);
            printPolicy(out);
        }
        catch(SOAPException e)
        {
            out.println("<cannot access SOAP message>");
            out.println(e.getClass().getName() + " " + e.getMessage());
        }
        catch(RuntimeException e)
        {
            // print runtime exception because it will be swallowed by finally's return
            out.println("<runtime exception>");
            out.println(e.getClass().getName() + " " + e.getMessage());
            throw e;
        } 
        finally
        {
            // trace should never stop a message processing
            return true;
        }
	}

    /**
     *   Prints the intercepted SOAP messages.
     */
	@SuppressWarnings("finally")
	public boolean interceptServerInbound(SOAPMessageContext smc) throws InterceptorException
	{
        // where to write the information
        PrintStream out = getPrintStream();

        try
        {
            printState(out, smc, true, false);
            printMessage(out, smc);
            printPolicy(out);
        }
        catch(SOAPException e)
        {
            out.println("<cannot access SOAP message>");
            out.println(e.getClass().getName() + " " + e.getMessage());
        }
        catch(RuntimeException e)
        {
            // print runtime exception because it will be swallowed by finally's return
            out.println("<runtime exception>");
            out.println(e.getClass().getName() + " " + e.getMessage());
            throw e;
        } 
        finally
        {
            // trace should never stop a message processing
            return true;
        }
	}

    /**
     *   Prints the intercepted SOAP messages.
     */
	@SuppressWarnings("finally")
	public boolean interceptServerOutbound(SOAPMessageContext smc) throws InterceptorException
	{
        // where to write the information
        PrintStream out = getPrintStream();

        try
        {
            printState(out, smc, true, true);
            printMessage(out, smc);
            printPolicy(out);
        }
        catch(SOAPException e)
        {
            out.println("<cannot access SOAP message>");
            out.println(e.getClass().getName() + " " + e.getMessage());
        }
        catch(RuntimeException e)
        {
            // print runtime exception because it will be swallowed by finally's return
            out.println("<runtime exception>");
            out.println(e.getClass().getName() + " " + e.getMessage());
            throw e;
        } 
        finally
        {
            // trace should never stop a message processing
            return true;
        }
	}

    /** Get the configured output print stream */
    private PrintStream getPrintStream()
    {
        Extension ext = getExtension();
        synchronized(ext) {
            return (PrintStream) ext.getProperty("output.stream");
        }
    }
    
    private void printState(PrintStream out, SOAPMessageContext smc, boolean isServerSide, boolean isOutbound) throws SOAPException
    {
        // print part of SOAP message context
        QName webServiceName = (QName) smc.get(MessageContext.WSDL_SERVICE);

        boolean isFault = SOAPUtil.isFaultMessage(smc);
        boolean isForcedBackToClient = SOAPUtil.isForcedBackToClient(smc);

        out.println("Tracing Web Service" +
                    (isServerSide ? "" : " client") +
                    ": " + webServiceName);
        out.println((isOutbound ? "Outbound" : "Inbound") +
                    " SOAP message" +
                    (isFault ? " containing a Fault" : "") +
                    (isForcedBackToClient ? " forced back to client" : "") +
                    ":");
    }

    /** Prints the message to given output */
	private void printMessage(PrintStream out, SOAPMessageContext smc) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, SOAPException, IOException
	{
        // print SOAP message contents
        SOAPMessage soapMessage = smc.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // check printing style
        boolean prettyPrint = ConfigUtil.recognizeAsTrue((String) getExtension().getProperty("pretty-print"));
        if(prettyPrint) {
            XMLUtil.prettyPrint(soapPart, out);
        } else {
            soapMessage.writeTo(out);
            out.println();
        }		
	}

    /** Prints the policy to given output, if any */
	private void printPolicy(PrintStream out) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, SOAPException, IOException
	{		
        // retrieve policy from pipe context
        Policy policy = (Policy) getPipe().getConstant(PolicyPipeFactory.PIPE_POLICY_CONSTANT);
        
        if(policy == null)
        	return;
        
        out.println("Pipe policy:");

        // check printing style
        boolean prettyPrint = ConfigUtil.recognizeAsTrue((String) getExtension().getProperty("pretty-print"));
        if(prettyPrint) {
            XMLUtil.prettyPrint(PolicyUtil.toNode(policy), out);
        } else {
            out.println(PolicyUtil.toString(policy));
            out.println();
        }		
	}
}
