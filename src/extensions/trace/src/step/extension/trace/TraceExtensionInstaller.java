package step.extension.trace;

import javax.xml.namespace.QName;

import step.framework.extensions.ExtensionInstaller;
import step.framework.extensions.ExtensionListener;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;

public class TraceExtensionInstaller extends ExtensionInstaller {

	@Override
	public String getExtensionID()
	{
		return "trace";
	}
	
	public QName[] getWSPNamespaces()
	{
		QName[] nss = new QName[1];
		nss[0] = new QName("http://stepframework.sourceforge.net/smartstep/policy", "Trace");
		
		return nss;
	}

	@Override
	protected Class<? extends ExtensionListener> getExtensionListenerClass()
	{
		return TraceExtensionListener.class;
	}

	@Override
	public Class<? extends ServiceInterceptor> getServiceInterceptorClass()
	{
		return TraceServiceInterceptor.class;
	}

	@Override
	public Class<? extends WebServiceInterceptor> getWebServiceInterceptorClass()
	{
		return TraceWebServiceInterceptor.class;
	}
}
