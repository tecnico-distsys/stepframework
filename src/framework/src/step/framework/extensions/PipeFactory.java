package step.framework.extensions;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import step.framework.service.Service;

public class PipeFactory {
	
	private static PipeFactory instance;
	
	public static PipeFactory getInstance()
	{
		if(instance == null)
			instance = new PipeFactory(ExtensionRepository.getInstance());
		
		return instance;
	}
	
	//**********************************************************
	//pipe factory implementation
	
	private ExtensionRepository extRepository;
	
	private PipeFactory(ExtensionRepository extRepository)
	{
		this.extRepository = extRepository;
	}

	@SuppressWarnings("unchecked")
	public ServiceInterceptorPipe getServiceInterceptorPipe(Service svc)
	{
		List<Extension> extensionList = extRepository.getExtensions();
		List<ServiceInterceptor> interceptors = new ArrayList<ServiceInterceptor>();
		for(int i=0; i<extensionList.size(); i++)
		{
			Extension ext = extensionList.get(i);
			if(ext.interceptsServices())
				interceptors.add(ext.getServiceInterceptor());
		}
		return new ServiceInterceptorPipe(svc, interceptors);
	}
	
	public WebServiceInterceptorPipe getWebServiceInterceptorPipe(SOAPMessageContext smc)
	{
		List<Extension> extensionList = extRepository.getExtensions();
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
