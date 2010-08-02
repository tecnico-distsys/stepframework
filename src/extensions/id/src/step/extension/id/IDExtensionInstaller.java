package step.extension.id;

import javax.xml.namespace.QName;

import step.framework.extensions.ExtensionInstaller;
import step.framework.extensions.ExtensionListener;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;

public class IDExtensionInstaller extends ExtensionInstaller {

	@Override
	public String getExtensionID()
	{
		return "id";
	}
	
	public QName[] getWSPNamespaces()
	{
		QName[] nss = new QName[1];
		nss[0] = new QName("http://stepframework.sourceforge.net/smartstep/policy", "Identification");
		
		return nss;
	}

	@Override
	protected Class<? extends ExtensionListener> getExtensionListenerClass()
	{
		return null;
	}

	@Override
	public Class<? extends ServiceInterceptor> getServiceInterceptorClass()
	{
		return null;
	}

	@Override
	public Class<? extends WebServiceInterceptor> getWebServiceInterceptorClass()
	{
		return IDWebServiceInterceptor.class;
	}
}
