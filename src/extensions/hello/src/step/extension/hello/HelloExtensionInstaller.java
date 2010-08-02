package step.extension.hello;

import javax.xml.namespace.QName;

import step.framework.extensions.ExtensionInstaller;
import step.framework.extensions.ExtensionListener;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;

public class HelloExtensionInstaller extends ExtensionInstaller {

	@Override
	public String getExtensionID()
	{
		return "hello";
	}
	
	public QName[] getWSPNamespaces()
	{
		QName[] nss = new QName[1];
		nss[0] = new QName("http://stepframework.sourceforge.net/smartstep/policy", "Hello");
		
		return nss;
	}

	@Override
	protected Class<? extends ExtensionListener> getExtensionListenerClass()
	{
		return HelloExtensionListener.class;
	}

	@Override
	public Class<? extends ServiceInterceptor> getServiceInterceptorClass()
	{
		return HelloServiceInterceptor.class;
	}

	@Override
	public Class<? extends WebServiceInterceptor> getWebServiceInterceptorClass()
	{
		return HelloWebServiceInterceptor.class;
	}
}
