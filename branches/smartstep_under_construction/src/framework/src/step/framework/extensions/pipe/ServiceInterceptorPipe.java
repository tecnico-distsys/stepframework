package step.framework.extensions.pipe;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.domain.DomainException;
import step.framework.exception.ServiceException;
import step.framework.extensions.ExtensionException;
import step.framework.extensions.ServiceInterceptor;
import step.framework.service.Service;

@SuppressWarnings("unchecked")
public class ServiceInterceptorPipe extends Pipe {
	
    /** Logging */
    private static Log log = LogFactory.getLog(ServiceInterceptorPipe.class);
	
	private List<ServiceInterceptor> interceptors;
	
	public ServiceInterceptorPipe(List<ServiceInterceptor> interceptors)
	{
		this.interceptors = (interceptors == null) ? new ArrayList<ServiceInterceptor>() : interceptors;
	}
	
	public void executeBefore(Service svc) throws DomainException, ServiceException
	{
		log.trace("Executing before service pipe");
		try
		{
			init();
			
			for(int i=0; i<interceptors.size(); i++)
			{
				log.trace("Executing before " + interceptors.get(i).getExtension().getID());
				interceptors.get(i).interceptBefore(svc);
			}
			
			log.trace("Pipe result: OK");
		}
		catch(ExtensionException e)
		{
			log.trace("Pipe result: ERROR");
			throw new ServiceException(e);
		}
		finally
		{
			destroy();
		}
	}
	
	public void executeAfter(Service svc) throws DomainException, ServiceException
	{
		log.trace("Executing after service pipe");
		try
		{
			init();
			
			for(int i=interceptors.size() - 1; i >= 0; i--)
			{
				log.trace("Executing after " + interceptors.get(i).getExtension().getID());
				interceptors.get(i).interceptAfter(svc);
			}
			
			log.trace("Pipe result: OK");
		}
		catch(ExtensionException e)
		{
			log.trace("Pipe result: ERROR");
			throw new ServiceException(e);
		}
		finally
		{
			destroy();
		}
	}
	
	private void init()
	{
		for(int i=0; i<interceptors.size(); i++)
			interceptors.get(i).setPipe(this);
	}
	
	private void destroy()
	{
		for(int i=0; i<interceptors.size(); i++)
			interceptors.get(i).setPipe(null);
	}
}
