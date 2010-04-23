package step.extension.id;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.WebServiceInterceptorException;
import step.framework.extensions.WebServiceInterceptorParameter;

public class IDWebServiceInterceptor implements WebServiceInterceptor {

	private static final String NS_IDENTIFICATION = "http://stepframework.sourceforge.net/extensions/identification";
	
	private static final QName QN_HEADER_ELEM = new QName(NS_IDENTIFICATION, "Identification", "sid");
	private static final QName QN_ID_ATTR = new QName("id");
	private static final QName QN_SECRET_ATTR = new QName("secret");
	
	private static final String PROPERTY_ID = "client.id";
	private static final String PROPERTY_SECRET = "client.secret";
	private static final String PROPERTY_AUTHORIZED = "server.authorized";

    public boolean interceptMessage(WebServiceInterceptorParameter param) throws SOAPFaultException, WebServiceInterceptorException {

        try
        {        	
            boolean isServerSide = param.isServerSide();
            boolean isOutbound = param.isOutboundSOAPMessage();

            if(isServerSide && !isOutbound)
            	checkID(param);
            
            if(!isServerSide && isOutbound)
            	addID(param);
            
            return true;
        }
        catch(RuntimeException e)
        {
            throw e;
        }
    }

    private void addID(WebServiceInterceptorParameter param)
    {
    	try
    	{
    		Properties extConfig = param.getExtension().getConfig();
    		String id = extConfig.getProperty(PROPERTY_ID);
    		String secret = extConfig.getProperty(PROPERTY_SECRET);
    		
    		if(id == null || secret == null)
    			throw new RuntimeException("Identification undefined! Please define \"" + PROPERTY_ID + "\" and \"" + PROPERTY_SECRET + "\" properties in \"extension-id.properties\".");
    		
            SOAPMessageContext smc = param.getSOAPMessageContext();
            SOAPEnvelope soapEnvelope = smc.getMessage().getSOAPPart().getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            if(soapHeader == null)
            	soapHeader = soapEnvelope.addHeader();
            
            addElement(soapHeader, id, secret);
    	}
    	catch(RuntimeException e)
    	{
    		throw e;
    	}
    	catch(Exception e)
    	{
    		throw new RuntimeException(e);
    	}
    }

	@SuppressWarnings("unchecked")
	private void checkID(WebServiceInterceptorParameter param) throws RuntimeException
    {
    	try
    	{
    		Properties extConfig = param.getExtension().getConfig();
    		String authorized = extConfig.getProperty(PROPERTY_AUTHORIZED);
    		
    		if(authorized == null)
    			throw new RuntimeException("Authorized clients property is undefined! Please define \"" + PROPERTY_AUTHORIZED + "\" property in \"extension-id.properties\".");
    		
            SOAPMessageContext smc = param.getSOAPMessageContext();
            SOAPHeader soapHeader = smc.getMessage().getSOAPPart().getEnvelope().getHeader();
            
            if(soapHeader == null)
            	throw new RuntimeException("No identification was provided.");
            
			Iterator<SOAPHeaderElement> it = (Iterator<SOAPHeaderElement>) soapHeader.examineAllHeaderElements();
			while(it.hasNext())
			{
				SOAPHeaderElement current = it.next();
				//found id header
				if(current.getElementQName().equals(QN_HEADER_ELEM))
				{
					checkValid(current, convertToMap(authorized));
					return;
				}
			}
			throw new RuntimeException("No identification was provided.");
    	}
    	catch(RuntimeException e)
    	{
    		throw e;
    	}
    	catch(Exception e)
    	{
    		throw new RuntimeException(e);
    	}
    }
    
    private void addElement(SOAPHeader soapHeader, String id, String secret)
    {
    	try
    	{
	    	SOAPHeaderElement headerElement = soapHeader.addHeaderElement(QN_HEADER_ELEM);
	    	headerElement.addAttribute(QN_ID_ATTR, id);
	    	headerElement.addAttribute(QN_SECRET_ATTR, secret);
    	}
    	catch(Exception e)
    	{
    		throw new RuntimeException(e);
    	}
    }
    
    private void checkValid(SOAPHeaderElement element, Map<String, String> authorized)
    {
    	try
    	{    		
    		String id = element.getAttributeValue(QN_ID_ATTR);
    		String secret = element.getAttributeValue(QN_SECRET_ATTR);
    		
    		//if required attributes are undefined then fault 
    		if(id == null || secret == null)
    			throw new RuntimeException("Identification attributes are undefined.");

    		//if all ids are authorized, then accept
    		if(authorized.containsKey("*"))
    			return;
    		    		
    		String storedSecret = authorized.get(id);
    		//if the id is not authorized, then fault
    		if(storedSecret == null)
    			throw new RuntimeException("Unauthorized client.");
    		//if the secrets do not match, then fault
    		if(!storedSecret.equals(secret))
    			throw new RuntimeException("Authorization has an invalid secret.");
    	}
    	catch(RuntimeException e)
    	{
    		throw e;
    	}
    	catch(Exception e)
    	{
    		throw new RuntimeException(e);     		
    	}
    }
    
    private Map<String, String> convertToMap(String authorized)
    {
    	try
    	{
	    	Map<String, String> map = new HashMap<String, String>();
	    	
	    	//separate ids by ;
	    	String[] elements = authorized.split(";");
	    	for(int i=0;i<elements.length; i++)
	    	{
	    		//if * is defined, then accept all ids
	    		if(elements[i].trim().equals("*"))
	    		{
	    			map.put("*", "");
	    			return map;
	    		}
	    		
	    		//if element is empty, ignore
	    		if(elements[i].trim().length() != 0)
	    		{
	    			System.out.println("ELEMENT: " + elements[i]);
	    			//separate id and secret by :
	    			String[] element = elements[i].split(":");
		    		map.put(element[0].trim(), element[1].trim());
		    		
	    		}
	    	}
	    	
	    	return map;
    	}
    	catch(Exception e)
    	{
    		throw new RuntimeException(e);
    	}
    }
}
