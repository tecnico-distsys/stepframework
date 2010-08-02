package step.extension.errors;

import javax.xml.namespace.QName;

import step.framework.extensions.ExtensionInstaller;
import step.framework.extensions.ExtensionListener;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;

public class ErrorsExtensionInstaller extends ExtensionInstaller {

	@Override
	public String getExtensionID()
	{
		return "errors";
	}
	
	public QName[] getWSPNamespaces()
	{
		QName[] nss = new QName[1];
		nss[0] = new QName("http://stepframework.sourceforge.net/smartstep/policy", "Errors");
		
		return nss;
	}

	@Override
	protected Class<? extends ExtensionListener> getExtensionListenerClass()
	{
		return ErrorsExtensionListener.class;
	}

	@Override
	public Class<? extends ServiceInterceptor> getServiceInterceptorClass()
	{
		return ErrorsServiceInterceptor.class;
	}

	@Override
	public Class<? extends WebServiceInterceptor> getWebServiceInterceptorClass()
	{
		return ErrorsWebServiceInterceptor.class;
	}
}
