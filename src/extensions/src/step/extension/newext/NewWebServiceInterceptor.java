package step.extension.newext;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.exceptions.InterceptorException;

public class NewWebServiceInterceptor extends WebServiceInterceptor {

	public boolean interceptClientInbound(SOAPMessageContext smc) throws InterceptorException
	{
		System.out.println("[DEBUG] Intercepting client inbound WebService: " + smc.get(SOAPMessageContext.WSDL_SERVICE));
		return true;
	}

	public boolean interceptClientOutbound(SOAPMessageContext smc) throws InterceptorException
	{
		System.out.println("[DEBUG] Intercepting client outbound WebService: " + smc.get(SOAPMessageContext.WSDL_SERVICE));
		return true;
	}

	public boolean interceptServerInbound(SOAPMessageContext smc) throws InterceptorException
	{
		System.out.println("[DEBUG] Intercepting server inbound WebService: " + smc.get(SOAPMessageContext.WSDL_SERVICE));
		return true;
	}

	public boolean interceptServerOutbound(SOAPMessageContext smc) throws InterceptorException
	{
		System.out.println("[DEBUG] Intercepting server outbound WebService: " + smc.get(SOAPMessageContext.WSDL_SERVICE));
		return true;
	}

}
