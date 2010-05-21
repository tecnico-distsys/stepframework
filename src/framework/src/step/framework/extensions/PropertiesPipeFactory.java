package step.framework.extensions;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.service.Service;

public class PropertiesPipeFactory extends PipeFactory {

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
		return new ServiceInterceptorPipe(svc, interceptors);
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
		return new WebServiceInterceptorPipe(smc, interceptors);
	}
}
