package step.framework.wsconfig.policy;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;

import step.framework.config.Config;
import step.framework.config.ConfigException;
import step.framework.context.ApplicationContext;
import step.framework.wsconfig.ExtensionLiaison;
import step.framework.wsconfig.WSConfigurationException;
import step.framework.wsconfig.WSConfigurator;
import step.framework.wsconfig.wsdl.HTTPWSDLObtainer;
import step.framework.wsconfig.wsdl.LocalWSDLObtainer;

/*
 * Utility class for policy loading
 */
public class WSPolicyConfigurator implements WSConfigurator {

	public static final String NSSMARTSTEP = "http://stepframework.sourceforge.net/smartstep/policy";
	
	private static final QName QN_POLICY_HEADER = new QName(NSSMARTSTEP, "UsedPolicy");
	
	private static final String LOCAL_POLICY_PROPERTY_NAME = "wsconfig.policy.local";
	private static final String SERVER_POLICY_PROPERTY_NAME = "wsconfig.policy.server";
	
	private static final String CONTEXT_KEY_ALTERNATIVE = "wsconfig.context.alternative";
	private static final String CONTEXT_KEY_POLICY = "wsconfig.context.policy";

    /** Logging */
    private Log log;
	
	//********************************************************
	//constructors
	
	public WSPolicyConfigurator()
	{
    	this.log = LogFactory.getLog(WSPolicyConfigurator.class);
    	new STEPAssertionBuilder().register();
	}
	
	//********************************************************
	//configurator interface

	public void configClientOutbound(SOAPMessageContext smc) throws WSConfigurationException
	{
		System.out.println("\nCONFIG CLIENT OUTBOUND\n");
		
		log.debug("Configuring extensions for outbound client message");
		
		try
		{
			String filename = loadFilenameFromConfig();
			Policy localPolicy = loadPolicy(filename);
			Policy serverPolicy = getServerPolicy(smc);		
			Policy policy = PolicyUtil.intersect(localPolicy, serverPolicy);
			//TODO: start debug
			System.out.println("\nLOCAL_POLICY");
			PolicyUtil.printAlternatives(localPolicy);
			System.out.println("END_LOCAL_POLICY\n");
			System.out.println("\nSERVER_POLICY");
			PolicyUtil.printAlternatives(serverPolicy);
			System.out.println("END_SERVER_POLICY\n");
			System.out.println("\nUSED_POLICY");
			PolicyUtil.printAlternatives(policy);
			System.out.println("END_USED_POLICY\n");
			//TODO: end debug
			if(hasAlternatives(serverPolicy))
				addPolicyHeader(smc.getMessage(), policy);
			List<Assertion> alternative = getAlternative(policy);
			enableExtensions(alternative);
			ApplicationContext.getInstance().put(CONTEXT_KEY_ALTERNATIVE, alternative);
			ApplicationContext.getInstance().put(CONTEXT_KEY_POLICY, policy);
		}
		catch(WSConfigurationException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}

	public void configServerInbound(SOAPMessageContext smc) throws WSConfigurationException
	{
		System.out.println("\nCONFIG SERVER INBOUND\n");
		
		log.debug("Configuring extensions for inbound server message");
		
		try
		{
			Policy policy = getPolicyFromHeader(smc.getMessage());
			if(policy == null)
			{				
				policy = getPolicyFromWSDL(smc);
				if(policy == null)
					throw new WSConfigurationException("Error loading server local policy.");					
				if(hasAlternatives(policy))
					throw new WSConfigurationException("Unspecified policy alternative.");

				//TODO: start debug
				System.out.println("\nWSDL_POLICY");
				PolicyUtil.printPolicy(policy);
				System.out.println("END_WSDL_POLICY\n");
				//TODO: end debug
			}
			else
			{
				//TODO: start debug
				System.out.println("\nPOLICY_HEADER");
				PolicyUtil.printPolicy(policy);
				System.out.println("END_POLICY_HEADER\n");
				//TODO: end debug
			}
			List<Assertion> alternative = getAlternative(policy);
			enableExtensions(alternative);
			ApplicationContext.getInstance().put(CONTEXT_KEY_ALTERNATIVE, alternative);
			ApplicationContext.getInstance().put(CONTEXT_KEY_POLICY, policy);
		}
		catch(WSConfigurationException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void configServerOutbound(SOAPMessageContext smc) throws WSConfigurationException
	{
		System.out.println("\nCONFIG SERVER OUTBOUND\n");
		
		log.debug("Configuring extensions for outbound server message");
		
		try
		{
			//TODO: start debug
			Policy policy = (Policy) ApplicationContext.getInstance().get(CONTEXT_KEY_POLICY);
			System.out.println("\nSAVED_POLICY");
			PolicyUtil.printPolicy(policy);
			System.out.println("END_SAVED_POLICY\n");
			//TODO: end debug			
			List<Assertion> alternative = (List<Assertion>) ApplicationContext.getInstance().get(CONTEXT_KEY_ALTERNATIVE);
			enableExtensions(alternative);
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void configClientInbound(SOAPMessageContext smc) throws WSConfigurationException
	{
		System.out.println("\nCONFIG CLIENT INBOUND\n");
		
		log.debug("Configuring extensions for inbound client message");
		
		try
		{
			//TODO: start debug
			Policy policy = (Policy) ApplicationContext.getInstance().get(CONTEXT_KEY_POLICY);
			System.out.println("\nSAVED_POLICY");
			PolicyUtil.printPolicy(policy);
			System.out.println("END_SAVED_POLICY\n");
			//TODO: end debug
			List<Assertion> alternative = (List<Assertion>) ApplicationContext.getInstance().get(CONTEXT_KEY_ALTERNATIVE);
			enableExtensions(alternative);
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void reset()
	{
		try
		{
			List<Assertion> alternative = (List<Assertion>) ApplicationContext.getInstance().get(CONTEXT_KEY_ALTERNATIVE);
			disableExtensions(alternative);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//********************************************************
	//auxiliary methods
	
	private String loadFilenameFromConfig() throws ConfigException, WSConfigurationException
	{
		log.debug("Loading policy filename from config");
		
		Config.getInstance().load();
		String filename = Config.getInstance().getInitParameter(LOCAL_POLICY_PROPERTY_NAME);
		
		if(filename == null)
			throw new WSConfigurationException("Local policy property is undefined");
		
		return filename;
	}
	
	private Policy loadPolicy(String filename) throws WSConfigurationException, IOException
	{
		log.debug("Loading policy from file \"" + filename + "\"");
			
		if(!filename.startsWith("/"))
			filename = "/" + filename;
		URL url = WSPolicyConfigurator.class.getResource(filename);
		Policy policy = PolicyEngine.getPolicy(url.openStream());
		
		if(policy == null)
			throw new WSConfigurationException("File \"" + filename + "\" does not define a valid policy");
		
		return policy;
	}
	
	private Policy getServerPolicy(SOAPMessageContext smc) throws WSConfigurationException
	{
		try
		{
			String wsdlUrl = Config.getInstance().getInitParameter(SERVER_POLICY_PROPERTY_NAME);
			if(wsdlUrl == null)
				throw new WSConfigurationException("Server policy property is undefined");
			
			ServerPolicyObtainer policyObt = new ServerPolicyObtainer(new HTTPWSDLObtainer(wsdlUrl));
			
			return policyObt.getServicePolicy();
		}
		catch(WSConfigurationException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}
	
	private Policy getPolicyFromWSDL(SOAPMessageContext smc) throws WSConfigurationException
	{
		try
		{
			String wsdlUrl = Config.getInstance().getInitParameter(SERVER_POLICY_PROPERTY_NAME);
			if(wsdlUrl == null)
				throw new WSConfigurationException("Server policy property is undefined");
			
			ServerPolicyObtainer policyObt = new ServerPolicyObtainer(new LocalWSDLObtainer(wsdlUrl));
			
			return policyObt.getServicePolicy();
		}
		catch(WSConfigurationException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}
	
	private boolean hasAlternatives(Policy policy)
	{
		Iterator<?> it = policy.getAlternatives();
		
		//has at least one alternative?
		if(it.hasNext())
		{
			it.next();
			//has more than one?
			return it.hasNext();
		}
		else
		{
			//doesn't have any
			return false;
		}
	}
	
	private void addPolicyHeader(SOAPMessage message, Policy policy) throws WSConfigurationException
	{
		try
		{
			SOAPEnvelope soapEnvelope = message.getSOAPPart().getEnvelope();
			SOAPHeader soapHeader = soapEnvelope.getHeader();
			if(soapHeader == null)
				soapHeader = soapEnvelope.addHeader();
			
			SOAPHeaderElement element = soapHeader.addHeaderElement(QN_POLICY_HEADER);
			String strPolicy = PolicyUtil.toString(policy);
			element.setValue(strPolicy);
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Policy getPolicyFromHeader(SOAPMessage message) throws WSConfigurationException
	{
		try
		{
            SOAPHeader soapHeader = message.getSOAPPart().getEnvelope().getHeader();            
            if(soapHeader == null)
            	return null;
            
			Iterator<SOAPHeaderElement> it = (Iterator<SOAPHeaderElement>) soapHeader.examineAllHeaderElements();
			while(it.hasNext())
			{
				SOAPHeaderElement current = it.next();
				if(current.getElementQName().equals(QN_POLICY_HEADER))
				{
					soapHeader.removeChild(current);
					return PolicyUtil.getPolicy(current);
				}
			}
			
			return null;
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Assertion> getAlternative(Policy policy) throws WSConfigurationException
	{
		log.debug("Analyzing policy for a valid configuration alternative");
		
		Iterator<List<Assertion>> it = policy.getAlternatives();
		
		if(it.hasNext())
		{
			List<Assertion> assertions = it.next();
			
			if(log.isDebugEnabled())
			{
				if(assertions.size() == 0)
				{
					log.debug("Chosen alternative has no assertions");
				}
				else
				{
					log.debug("List of assertions on chosen alternative:");
					for(int i=0; i<assertions.size(); i++)
					{
						log.debug("\t" + assertions.get(i).getName().getLocalPart());
					}
					log.debug("End of list");
				}
			}
			
			return assertions;
		}
		
		throw new WSConfigurationException("The defined policy has no valid alternatives");
	}
	
	private void enableExtensions(List<Assertion> alternative) throws WSConfigurationException
	{
		log.debug("Configuring necessary extensions to meet policy requirements");
		
		for(int i=0; i<alternative.size(); i++)
		{
			Assertion assertion = alternative.get(i);
			String id = null;
			
			if(assertion instanceof STEPAssertion)
				id=((STEPAssertion) assertion).getExtension();
			else
				id=assertion.getName().getLocalPart();
			
			log.debug("Enabling extension: " + id);
			ExtensionLiaison.enable(id);
		}
		
		log.debug("Extensions configured successfully");
	}
	
	private void disableExtensions(List<Assertion> alternative) throws WSConfigurationException
	{
		log.debug("Disabling used extensions");
		
		for(int i=0; i<alternative.size(); i++)
		{
			Assertion assertion = alternative.get(i);
			String id = null;
			
			if(assertion instanceof STEPAssertion)
				id=((STEPAssertion) assertion).getExtension();
			else
				id=assertion.getName().getLocalPart();
			
			log.debug("Disabling extension: " + id);
			ExtensionLiaison.disable(id);
		}
		
		log.debug("Extensions disabled successfully");
	}
}
