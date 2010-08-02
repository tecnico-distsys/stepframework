package step.extension.hello;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.extensions.InterceptorException;
import step.framework.extensions.WebServiceInterceptor;

/**
 *  This is the Hello extension's web service interceptor.
 *  If properly configured, it captures all inbound and outbound SOAP messages.
 */
public class HelloWebServiceInterceptor extends WebServiceInterceptor {
	
	@Override
	public boolean interceptClientInbound(SOAPMessageContext smc) throws InterceptorException
	{
        String greeting = (String) getExtension().getProperty("greeting");

        System.out.println(greeting + " (intercepting inbound SOAP message)");

        return true;
	}

	@Override
	public boolean interceptClientOutbound(SOAPMessageContext smc) throws InterceptorException
	{
        String greeting = (String) getExtension().getProperty("greeting");

        System.out.println(greeting + " (intercepting outbound SOAP message)");

        return true;
	}

	@Override
	public boolean interceptServerInbound(SOAPMessageContext smc) throws InterceptorException
	{
        String greeting = (String) getExtension().getProperty("greeting");

        System.out.println(greeting + " (intercepting inbound SOAP message)");

        return true;
	}

	@Override
	public boolean interceptServerOutbound(SOAPMessageContext smc) throws InterceptorException
	{
        String greeting = (String) getExtension().getProperty("greeting");

        System.out.println(greeting + " (intercepting outbound SOAP message)");

        return true;
	}

}
