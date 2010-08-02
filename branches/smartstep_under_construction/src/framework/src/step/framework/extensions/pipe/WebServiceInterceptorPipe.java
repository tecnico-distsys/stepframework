package step.framework.extensions.pipe;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.extensions.InterceptorException;
import step.framework.extensions.WebServiceInterceptor;
import step.framework.ws.SOAPUtil;

public class WebServiceInterceptorPipe extends Pipe {
	
    /** Logging */
    private static Log log = LogFactory.getLog(WebServiceInterceptorPipe.class);
	
	private SOAPMessageContext smc;
	private List<WebServiceInterceptor> interceptors;
	
	private boolean inverted;
	
	public WebServiceInterceptorPipe(List<WebServiceInterceptor> interceptors)
	{
		this.smc = null;
		this.interceptors = (interceptors == null) ? new ArrayList<WebServiceInterceptor>() : interceptors;
		this.inverted = false;
	}
	
	public boolean executeOutbound(SOAPMessageContext smc, boolean isClientSide)
	{
		this.inverted = false;
		log.trace("Executing outbound " + ((isClientSide) ? "client" : "server") + " side pipe");
		try
		{
			init();
			this.smc = smc;
			
			boolean isOK = executeOutbound(isClientSide, 0);
			log.trace("Pipe result: " + ((isOK) ? "OK" : "ERROR"));
			
			return isOK;
		}
		catch(SOAPException e)
		{
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
		finally
		{
			this.smc = null;
			destroy();
		}
	}
	
	public boolean executeInbound(SOAPMessageContext smc, boolean isClientSide)
	{
		this.inverted = false;
		log.trace("Executing inbound " + ((isClientSide) ? "client" : "server") + " side pipe");
		try
		{
			init();
			this.smc = smc;
			
			boolean isOK = executeInbound(isClientSide, interceptors.size() - 1);
			log.trace("Pipe result: " + ((isOK) ? "OK" : "ERROR"));
			
			return isOK;
		}
		catch(SOAPException e)
		{
			log.trace(e);
			throw new IllegalStateException(e);
		}
		finally
		{
			this.smc = null;
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
	
	private boolean executeOutbound(boolean isClientSide, int startAt) throws SOAPException
	{
		for(int i=startAt; i<interceptors.size(); i++)
		{
			log.trace("Executing outbound " + interceptors.get(i).getExtension().getID());
			try
			{
				boolean abort = true;
				if(isClientSide)
					abort = !interceptors.get(i).interceptClientOutbound(smc);
				else
					abort = !interceptors.get(i).interceptServerOutbound(smc);
					
				if(abort)
				{
					log.trace("Received abort signal from " + interceptors.get(i).getExtension().getID());
					invert(true, isClientSide, i);
					return false;
				}
			}
			catch(InterceptorException e)
			{
				log.trace(e);
                SOAPUtil.replaceBodyWithNewFault(smc, e.getMessage());
				invert(true, isClientSide, i);
				return false;
			}
			catch(SOAPFaultException e)
			{
				log.trace(e);
                SOAPUtil.replaceBodyWithFault(smc, e.getFault());
				invert(true, isClientSide, i);
				return false;
			}
		}
		return true;
	}
	
	private boolean executeInbound(boolean isClientSide, int startAt) throws SOAPException
	{
		for(int i=startAt; i>=0; i--)
		{
			log.trace("Executing inbound " + interceptors.get(i).getExtension().getID());
			try
			{
				boolean abort = true;
				if(isClientSide)
					abort = !interceptors.get(i).interceptClientInbound(smc);
				else
					abort = !interceptors.get(i).interceptServerInbound(smc);

				if(abort)
				{
					log.trace("Received abort signal from " + interceptors.get(i).getExtension().getID());
					invert(false, isClientSide, i);
					return false;
				}
			}
			catch(InterceptorException e)
			{
				log.trace(e);
                SOAPUtil.replaceBodyWithNewFault(smc, e.getMessage());
				invert(false, isClientSide, i);
				return false;
			}
			catch(SOAPFaultException e)
			{
				log.trace(e);
                SOAPUtil.replaceBodyWithFault(smc, e.getFault());
				invert(false, isClientSide, i);
				return false;
			}
		}
		return true;
	}
	
	private void invert(boolean isOutbound, boolean isClientSide , int extension) throws SOAPException
	{
		if(canInvert(isOutbound, isClientSide))
		{
			inverted = true;
			
			log.trace("Inverting direction: " + ((isClientSide) ? "client" : "server") + " side, " + ((isOutbound) ? "outbound" : "inbound"));
			if(isOutbound)
				executeInbound(isClientSide, extension - 1);
			else
				executeOutbound(isClientSide, extension + 1);
		}
		else
		{			
			log.trace("Continuing after error: " + ((isClientSide) ? "client" : "server") + " side, " + ((isOutbound) ? "outbound" : "inbound"));
			if(isOutbound)
				executeOutbound(isClientSide, extension + 1);
			else
				executeInbound(isClientSide, extension - 1);
			
		}
	}
	
	private boolean canInvert(boolean isOutbound, boolean isClientSide)
	{
		return !inverted && ((isClientSide && isOutbound) || (!isClientSide && !isOutbound));
	}
}
