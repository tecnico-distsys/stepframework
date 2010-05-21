package step.framework.extensions;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.service.Service;

public class NullPipeFactory extends PipeFactory {

	@SuppressWarnings("unchecked")
	public ServiceInterceptorPipe createServiceInterceptorPipe(Service svc)
	{
		return new ServiceInterceptorPipe(svc, null);
	}
	
	public WebServiceInterceptorPipe createWebServiceInterceptorPipe(SOAPMessageContext smc)
	{
		return new WebServiceInterceptorPipe(smc, null);
	}
}
