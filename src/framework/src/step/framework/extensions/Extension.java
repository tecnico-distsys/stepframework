package step.framework.extensions;

//TODO: properties/config/etc
public final class Extension {
	
	private String id;
	private ServiceInterceptor serviceInterceptor;
	private WebServiceInterceptor webServiceInterceptor;
	
	public Extension(String id)
	{
		this.id = id;
		this.serviceInterceptor = null;
		this.webServiceInterceptor = null;
	}
	
	public String getID()
	{
		return id;
	}
	
	void setServiceInterceptor(ServiceInterceptor serviceInterceptor)
	{
		this.serviceInterceptor = serviceInterceptor;
	}
	
	ServiceInterceptor getServiceInterceptor()
	{
		return serviceInterceptor;
	}
	
	boolean interceptsServices()
	{
		return serviceInterceptor != null;
	}
	
	void setWebServiceInterceptor(WebServiceInterceptor webServiceInterceptor)
	{
		this.webServiceInterceptor = webServiceInterceptor;
	}
	
	boolean interceptsWebServices()
	{
		return webServiceInterceptor != null;
	}
	
	WebServiceInterceptor getWebServiceInterceptor()
	{
		return webServiceInterceptor;
	}
}
