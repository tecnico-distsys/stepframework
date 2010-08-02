package step.framework.extensions.pipe;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.service.Service;

public class NullPipeFactory extends PipeFactory {

	@SuppressWarnings("unchecked")
	public ServiceInterceptorPipe createServiceInterceptorPipe(Service svc)
	{
		return new ServiceInterceptorPipe(null);
	}
	
	public WebServiceInterceptorPipe createWebServiceInterceptorPipe(SOAPMessageContext smc)
	{
		return new WebServiceInterceptorPipe(null);
	}
}
