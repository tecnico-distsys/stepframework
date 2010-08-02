package step.extension.errors;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import step.framework.extensions.InterceptorException;
import step.framework.extensions.WebServiceInterceptor;
import step.framework.ws.SOAPUtil;

/**
 *  This is the Errors extension's web service interceptor.
 *  If properly configured, it captures all inbound and outbound SOAP messages.
 */
public class ErrorsWebServiceInterceptor extends WebServiceInterceptor {

	@Override
	public boolean interceptClientInbound(SOAPMessageContext smc) throws InterceptorException
	{
        // when to cause the error
        final String WHEN_PROP_NAME = "web-service-interceptor.error-when";
        String errorWhen = (String) getExtension().getProperty(WHEN_PROP_NAME);
        if(errorWhen == null)
        {
            System.out.println("no web service interceptor error when specified (property " +
                               WHEN_PROP_NAME + "); skipping");
            return true;
        }
        
        if(!errorWhen.equalsIgnoreCase("client-inbound"))
        	return true;
        
        String situation = "interceptMessage-" + errorWhen.toLowerCase();
        
        return handle(smc, situation);
	}

	@Override
	public boolean interceptClientOutbound(SOAPMessageContext smc) throws InterceptorException
	{
        // when to cause the error
        final String WHEN_PROP_NAME = "web-service-interceptor.error-when";
        String errorWhen = (String) getExtension().getProperty(WHEN_PROP_NAME);
        if(errorWhen == null)
        {
            System.out.println("no web service interceptor error when specified (property " +
                               WHEN_PROP_NAME + "); skipping");
            return true;
        }
        
        if(!errorWhen.equalsIgnoreCase("client-outbound"))
        	return true;
        
        String situation = "interceptMessage-" + errorWhen.toLowerCase();
        
        return handle(smc, situation);
	}

	@Override
	public boolean interceptServerInbound(SOAPMessageContext smc) throws InterceptorException
	{
        // when to cause the error
        final String WHEN_PROP_NAME = "web-service-interceptor.error-when";
        String errorWhen = (String) getExtension().getProperty(WHEN_PROP_NAME);
        if(errorWhen == null)
        {
            System.out.println("no web service interceptor error when specified (property " +
                               WHEN_PROP_NAME + "); skipping");
            return true;
        }
        
        if(!errorWhen.equalsIgnoreCase("server-inbound"))
        	return true;
        
        String situation = "interceptMessage-" + errorWhen.toLowerCase();
        
        return handle(smc, situation);
	}

	@Override
	public boolean interceptServerOutbound(SOAPMessageContext smc) throws InterceptorException
	{
        // when to cause the error
        final String WHEN_PROP_NAME = "web-service-interceptor.error-when";
        String errorWhen = (String) getExtension().getProperty(WHEN_PROP_NAME);
        if(errorWhen == null)
        {
            System.out.println("no web service interceptor error when specified (property " +
                               WHEN_PROP_NAME + "); skipping");
            return true;
        }
        
        if(!errorWhen.equalsIgnoreCase("server-outbound"))
        	return true;
        
        String situation = "interceptMessage-" + errorWhen.toLowerCase();
        
        return handle(smc, situation);
	}
	
	private boolean handle(SOAPMessageContext smc, String situation) throws InterceptorException
	{
        // return
        final String RETURN_PROP_NAME = "web-service-interceptor.return";
        String returnPropertyValue = (String) getExtension().getProperty(RETURN_PROP_NAME);
        if(returnPropertyValue != null && returnPropertyValue.equalsIgnoreCase("false"))
            return false;
        
        // throw
        final String EXCEPTION_PROP_NAME = "web-service-interceptor.throw";
        String exceptionClassName = (String) getExtension().getProperty(EXCEPTION_PROP_NAME);
        if(exceptionClassName == null || exceptionClassName.trim().length() == 0)
        {
            System.out.println("no web service interceptor throw exception specified (property " +
                               EXCEPTION_PROP_NAME + "); skipping");
            return true;
        }

        try
        {
            if(exceptionClassName.equals("javax.xml.ws.soap.SOAPFaultException")) // special case: SOAPFaultException
                throwSOAPFaultException(situation, smc);
            else
                ThrowExceptionUtil.throwException(exceptionClassName, situation);
        }
        catch(InterceptorException ie)
        {
            throw ie;
        }
        catch(RuntimeException rte)
        {
            throw rte;
        }
        catch(Exception e)
        {
            System.out.println("exception type not declared in throws; wrapping exception in runtime exception");
            throw new RuntimeException(e);
        }

        return true;  
	}

    private void throwSOAPFaultException(String message, SOAPMessageContext smc) throws SOAPFaultException, SOAPException, IOException
    {
        // create SOAP message
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage soapMessage = mf.createMessage();

        // access SOAP body
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPBody soapBody = soapEnvelope.getBody();

        // create SOAP Fault
        SOAPFault soapFault = soapBody.addFault();

        // set message
        String faultMessage = this.getClass().getSimpleName() + " generated exception in method " + message;
        soapFault.setFaultString(faultMessage);

        // set code according to context
        QName faultCode = SOAPUtil.suggestFaultCode(smc);
        soapFault.setFaultCode(faultCode);

        // throw SOAP fault exception
        throw new SOAPFaultException(soapFault);
    }
}
