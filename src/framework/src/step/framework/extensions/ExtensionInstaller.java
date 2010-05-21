package step.framework.extensions;


public abstract class ExtensionInstaller {
	
	static final Extension initExtension(ExtensionInstaller installer) throws ExtensionException
	{
		try
		{
			Extension ext = new Extension(installer.getExtensionID());
			
			Class<? extends ServiceInterceptor> svcIntClass = installer.getServiceInterceptorClass();
			ServiceInterceptor svcInt =svcIntClass.getConstructor().newInstance();
			svcInt.setExtension(ext);
			ext.setServiceInterceptor(svcInt);
			
			Class<? extends WebServiceInterceptor> wsIntClass = installer.getWebServiceInterceptorClass();
			WebServiceInterceptor wsInt = wsIntClass.getConstructor().newInstance() ;
			wsInt.setExtension(ext);
			ext.setWebServiceInterceptor(wsInt);
			
			return ext;
		}
		catch(Exception e)
		{
			throw new ExtensionException(e);
		}
	}
	
	//****************************************************
	//ExtensionInstaller methods
	
	public ExtensionInstaller() { }
	
	public abstract String getExtensionID();
	public abstract Class<? extends ServiceInterceptor> getServiceInterceptorClass();
	public abstract Class<? extends WebServiceInterceptor> getWebServiceInterceptorClass();
	
}
