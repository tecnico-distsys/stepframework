package step.extension.newext;

import step.framework.extensions.ExtensionInstaller;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;

public class NewExtensionInstaller extends ExtensionInstaller {

	@Override
	public String getExtensionID()
	{
		return "newext";
	}

	@Override
	public Class<? extends ServiceInterceptor> getServiceInterceptorClass()
	{
		return NewServiceInterceptor.class;
	}

	@Override
	public Class<? extends WebServiceInterceptor> getWebServiceInterceptorClass()
	{
		return NewWebServiceInterceptor.class;
	}

}
