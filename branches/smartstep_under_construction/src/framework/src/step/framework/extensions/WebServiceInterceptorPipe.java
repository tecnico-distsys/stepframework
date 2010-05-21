package step.framework.extensions;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import step.framework.ws.SOAPUtil;

@SuppressWarnings("unchecked")
public class WebServiceInterceptorPipe {
	
	private SOAPMessageContext smc;
	private List<WebServiceInterceptor> interceptors;
	
	private boolean inverted;
	
	WebServiceInterceptorPipe(SOAPMessageContext smc, List<WebServiceInterceptor> interceptors)
	{
		this.smc = smc;
		this.interceptors = (interceptors == null) ? new ArrayList<WebServiceInterceptor>() : interceptors;
		this.inverted = false;
	}
	
	public boolean executeOutbound(boolean isClientSide)
	{
		this.inverted = false;
		System.out.println("[DEBUG] Executing outbound " + ((isClientSide) ? "client" : "server") + " side pipe");
		try
		{
			boolean isOK = executeOutbound(isClientSide, 0);
			System.out.println("[DEBUG] Pipe result: " + ((isOK) ? "OK" : "ERROR"));
			
			return isOK;
		}
		catch(SOAPException e)
		{
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
	
	public boolean executeInbound(boolean isClientSide)
	{
		this.inverted = false;
		System.out.println("[DEBUG] Executing inbound " + ((isClientSide) ? "client" : "server") + " side pipe");
		try
		{
			boolean isOK = executeInbound(isClientSide, interceptors.size() - 1);
			System.out.println("[DEBUG] Pipe result: " + ((isOK) ? "OK" : "ERROR"));
			
			return isOK;
		}
		catch(SOAPException e)
		{
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
	
	private boolean executeOutbound(boolean isClientSide, int startAt) throws SOAPException
	{
		for(int i=startAt; i<interceptors.size(); i++)
		{
			System.out.println("[DEBUG] Executing outbound " + interceptors.get(i).getExtension().getID());
			try
			{
				boolean abort = true;
				if(isClientSide)
					abort = !interceptors.get(i).interceptClientOutbound(smc);
				else
					abort = !interceptors.get(i).interceptServerOutbound(smc);
					
				if(abort)
				{
					System.out.println("[DEBUG] Received abort signal from " + interceptors.get(i).getExtension().getID());
					invert(true, isClientSide, i);
					return false;
				}
				return true;
			}
			catch(InterceptorException e)
			{
				e.printStackTrace();
                SOAPUtil.replaceBodyWithNewFault(smc, e.getMessage());
				invert(true, isClientSide, i);
				return false;
			}
			catch(SOAPFaultException e)
			{
				e.printStackTrace();
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
			System.out.println("[DEBUG] Executing inbound " + interceptors.get(i).getExtension().getID());
			try
			{
				boolean abort = true;
				if(isClientSide)
					abort = !interceptors.get(i).interceptClientInbound(smc);
				else
					abort = !interceptors.get(i).interceptServerInbound(smc);

				if(abort)
				{
					System.out.println("[DEBUG] Received abort signal from " + interceptors.get(i).getExtension().getID());
					invert(false, isClientSide, i);
					return false;
				}
				return true;
			}
			catch(InterceptorException e)
			{
				e.printStackTrace();
                SOAPUtil.replaceBodyWithNewFault(smc, e.getMessage());
				invert(false, isClientSide, i);
				return false;
			}
			catch(SOAPFaultException e)
			{
				e.printStackTrace();
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
			
			System.out.println("[DEBUG] Inverting direction: " + ((isClientSide) ? "client" : "server") + " side, " + ((isOutbound) ? "outbound" : "inbound"));
			if(isOutbound)
				executeInbound(isClientSide, extension - 1);
			else
				executeOutbound(isClientSide, extension + 1);
		}
		else
		{			
			System.out.println("[DEBUG] Continuing after error: " + ((isClientSide) ? "client" : "server") + " side, " + ((isOutbound) ? "outbound" : "inbound"));
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
