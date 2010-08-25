package step.framework.extensions;

import javax.xml.namespace.QName;

import step.framework.jarloader.JarException;
import step.framework.jarloader.JarInstaller;

public abstract class ExtensionInstaller extends JarInstaller {
	
	protected abstract String getExtensionID();
	protected abstract QName[] getWSPNamespaces();
	protected abstract Class<? extends ExtensionListener> getExtensionListenerClass();
	protected abstract Class<? extends ServiceInterceptor> getServiceInterceptorClass();
	protected abstract Class<? extends WebServiceInterceptor> getWebServiceInterceptorClass();
	
	public final void install() throws JarException
	{
		try
		{
			String id = getExtensionID();
			
			if(id == null)
				throw new ExtensionException("Extension ID can't be null.");
			
			id = id.trim();
			if(id.length() == 0)
				throw new ExtensionException("Extension ID can't be empty.");
			
			Extension ext = new Extension(getExtensionID(), getWSPNamespaces(), getProperties());
			
			Class<? extends ExtensionListener> extListClass = getExtensionListenerClass();
			if(extListClass != null)
			{
				ExtensionListener extList = extListClass.getConstructor().newInstance();
				extList.setExtension(ext);
				ext.setExtensionListener(extList);
			}
			
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
			ext.init();
		}
		catch(Exception e)
		{
			throw new JarException(e);
		}
	}
}
