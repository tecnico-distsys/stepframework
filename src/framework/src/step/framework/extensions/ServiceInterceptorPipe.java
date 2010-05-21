package step.framework.extensions;

import java.util.ArrayList;
import java.util.List;

import step.framework.domain.DomainException;
import step.framework.exception.ServiceException;
import step.framework.service.Service;

@SuppressWarnings("unchecked")
public class ServiceInterceptorPipe {
	
	private Service<?> svc;
	private List<ServiceInterceptor> interceptors;
	
	ServiceInterceptorPipe(Service<?> svc, List<ServiceInterceptor> interceptors)
	{
		this.svc = svc;
		this.interceptors = (interceptors == null) ? new ArrayList<ServiceInterceptor>() : interceptors;
	}
	
	public void executeBefore() throws DomainException, ServiceException
	{
		try
		{
			for(int i=0; i<interceptors.size(); i++)
				interceptors.get(i).interceptBefore(svc);
		}
		catch(InterceptorException e)
		{
			throw new ServiceException(e);
		}
	}
	
	public void executeAfter() throws DomainException, ServiceException
	{
		try
		{
			for(int i=interceptors.size() - 1; i >= 0; i--)
				interceptors.get(i).interceptAfter(svc);
		}
		catch(InterceptorException e)
		{
			throw new ServiceException(e);
		}
	}
}
