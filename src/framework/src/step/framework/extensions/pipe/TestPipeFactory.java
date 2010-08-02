package step.framework.extensions.pipe;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.extensions.Extension;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;
import step.framework.service.Service;

public class TestPipeFactory extends PipeFactory {

	@SuppressWarnings("unchecked")
	public ServiceInterceptorPipe createServiceInterceptorPipe(Service svc)
	{
		List<Extension> extensionList = getExtensionRepository().getExtensions();
		List<ServiceInterceptor> interceptors = new ArrayList<ServiceInterceptor>();
		for(int i=0; i<extensionList.size(); i++)
		{
			Extension ext = extensionList.get(i);
			if(ext.interceptsServices())
				interceptors.add(ext.getServiceInterceptor());
		}
		
		return new ServiceInterceptorPipe(interceptors);		
	}
	
	public WebServiceInterceptorPipe createWebServiceInterceptorPipe(SOAPMessageContext smc)
	{
		List<Extension> extensionList = getExtensionRepository().getExtensions();
		List<WebServiceInterceptor> interceptors = new ArrayList<WebServiceInterceptor>();
		for(int i=0; i<extensionList.size(); i++)
		{
			Extension ext = extensionList.get(i);
			if(ext.interceptsWebServices())
				interceptors.add(ext.getWebServiceInterceptor());
		}
		return new WebServiceInterceptorPipe(interceptors);
	}
}
