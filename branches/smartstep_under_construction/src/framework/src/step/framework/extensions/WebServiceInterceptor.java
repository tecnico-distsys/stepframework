package step.framework.extensions;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.extensions.exceptions.InterceptorException;

//TODO: annotations to define what web services it intercepts???
public abstract class WebServiceInterceptor extends Interceptor {
	
	//***********************************************************************
	//abstract methods that need to be implemented
	
	public abstract boolean interceptClientOutbound(SOAPMessageContext smc) throws InterceptorException;	
	public abstract boolean interceptServerInbound(SOAPMessageContext smc) throws InterceptorException;
	public abstract boolean interceptServerOutbound(SOAPMessageContext smc) throws InterceptorException;
	public abstract boolean interceptClientInbound(SOAPMessageContext smc) throws InterceptorException;
	
}
