package step.framework.extensions;

import step.framework.jarloader.JarInstaller;

public abstract class ExtensionInstaller extends JarInstaller {
	
	protected abstract String getExtensionID();
	protected abstract Class<? extends ServiceInterceptor> getServiceInterceptorClass();
	protected abstract Class<? extends WebServiceInterceptor> getWebServiceInterceptorClass();
	
	public	final void install() throws ExtensionException
	{
		try
		{
			Extension ext = new Extension(getExtensionID());
			
			Class<? extends ServiceInterceptor> svcIntClass = getServiceInterceptorClass();
			if(svcIntClass != null)
			{
				ServiceInterceptor svcInt =svcIntClass.getConstructor().newInstance();
				svcInt.setExtension(ext);
				ext.setServiceInterceptor(svcInt);
			}
			
			Class<? extends WebServiceInterceptor> wsIntClass = getWebServiceInterceptorClass();
			if(wsIntClass != null)
			{
				WebServiceInterceptor wsInt = wsIntClass.getConstructor().newInstance() ;
				wsInt.setExtension(ext);
				ext.setWebServiceInterceptor(wsInt);
			}

			ExtensionRepository.getInstance().install(ext);
		}
		catch(Exception e)
		{
			throw new ExtensionException(e);
		}
	}
}
