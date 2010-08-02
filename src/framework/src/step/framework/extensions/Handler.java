package step.framework.extensions;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import step.framework.extensions.pipe.PipeFactory;
import step.framework.extensions.pipe.WebServiceInterceptorPipe;
import step.framework.ws.SOAPUtil;


/**
 *  JAX-WS SOAP Handler that intercepts web service message flow
 *  and calls web service interceptor manager to invoke interceptors.
 */
public class Handler implements SOAPHandler<SOAPMessageContext> {
	
	private static final String PIPE_CONTEXT_KEY = "extensions.pipe";
    
    //***********************************************************************
    // Members

    /** Logging */
    private Log log = LogFactory.getLog(Handler.class);
    
    //***********************************************************************
    // Constructor
    
    public Handler() {
    }

    //***********************************************************************
    // SOAPHandler interface
    
    public Set<QName> getHeaders()
    {
        log.trace("getHeaders()");
        
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc)
    {
        log.trace("handleMessage()");
        
    	return executePipe(smc);
    }
    
    public boolean handleFault(SOAPMessageContext smc)
    {
        log.trace("handleFault()");

    	return executePipe(smc);
    }

    public void close(MessageContext messageContext)
    {
        log.trace("close()");
    }

    //***********************************************************************
    // Auxiliary methods
    
    private boolean executePipe(SOAPMessageContext smc)
    {
    	try
    	{
			boolean isClientSide = !SOAPUtil.isServerSideMessage(smc);
			boolean isOutbound = SOAPUtil.isOutboundMessage(smc);
	
	        WebServiceInterceptorPipe pipe = null;
	        
	        if(isOutbound)
	        {
	        	if(isClientSide)
	        	{
	        		log.trace("Creating a new pipe to handle request");
	        		pipe = PipeFactory.getWebServiceInterceptorPipe(smc);
	        		smc.put(PIPE_CONTEXT_KEY, pipe);
	        	}
	        	else
	            {
	        		log.trace("Loading used pipe");
	        		pipe = (WebServiceInterceptorPipe) smc.get(PIPE_CONTEXT_KEY);
	            }
	        	
	        	if(pipe == null)
	        	{
	        		log.trace("No pipe to execute");
	        		return false;
	        	}
	        	else
	        	{
	        		log.trace("Executing pipe");
	        		return pipe.executeOutbound(smc, isClientSide);
	        	}
	        }
	        else
	        {
	        	if(!isClientSide)
	        	{
	        		log.trace("Creating a new pipe to handle request");
	        		pipe = PipeFactory.getWebServiceInterceptorPipe(smc);
	        		smc.put(PIPE_CONTEXT_KEY, pipe);
	        	}
	        	else
	            {
	        		log.trace("Loading used pipe");
	        		pipe = (WebServiceInterceptorPipe) smc.get(PIPE_CONTEXT_KEY);
	            }
	        	
	        	if(pipe == null)
	        	{
	        		log.trace("No pipe to execute");
	        		return false;
	        	}
	        	else
	        	{
	        		log.trace("Executing pipe");
	        		return pipe.executeInbound(smc, isClientSide);
	        	}
	        }
    	}
    	catch(ExtensionException e)
    	{
    		log.trace("Caught exception while handling message");
    		try
    		{
        		log.trace("Creating a fault message");
    			SOAPUtil.replaceBodyWithNewFault(smc, e.getMessage());
    			
    			return false;
			}
			catch(SOAPException e1)
			{
				log.trace(e1);
				throw new IllegalStateException(e1);
			}
    	}
    }
}
