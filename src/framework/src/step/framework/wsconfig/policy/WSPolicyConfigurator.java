package step.framework.wsconfig.policy;

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
import org.apache.neethi.builders.AssertionBuilder;

import step.framework.config.Config;
import step.framework.config.ConfigException;
import step.framework.wsconfig.ExtensionLiaison;
import step.framework.wsconfig.WSConfigurationException;
import step.framework.wsconfig.WSConfigurator;
import step.framework.wsconfig.wsdl.HTTPWSDLObtainer;

/*
 * Utility class for policy loading
 */
public class WSPolicyConfigurator implements WSConfigurator {

	public static final String NSSMARTSTEP = "http://stepframework.sourceforge.net/smartstep/policy";
	
	private static final QName QN_POLICY_HEADER = new QName(NSSMARTSTEP, "UsedPolicy");
	
	private static final String LOCAL_POLICY_PROPERTY_NAME = "wsconfig.policy.local";
	private static final String SERVER_POLICY_PROPERTY_NAME = "wsconfig.policy.server";

    /** Logging */
    private Log log;
    
    private AssertionBuilder assertionBuilder;
	
	//********************************************************
	//constructors
	
	public WSPolicyConfigurator()
	{
    	this.log = LogFactory.getLog(WSPolicyConfigurator.class);
    	this.assertionBuilder = new STEPAssertionBuilder();
    	
    	QName[] names = assertionBuilder.getKnownElements();
    	for(int i=0; i<names.length; i++)
    	{
    		PolicyEngine.registerBuilder(names[i], assertionBuilder);
    	}
	}
	
	//********************************************************
	//configurator interface

	public boolean config(SOAPMessageContext smc) throws WSConfigurationException
	{
		log.debug("Starting web service policy configuration");
		
		try
		{
			String filename = loadFilenameFromConfig();
			Policy localPolicy = loadPolicy(filename);
			Policy serverPolicy = getServerPolicy(smc);	
			Policy policy = PolicyUtil.intersect(localPolicy, serverPolicy);
			if(hasAlternatives(serverPolicy))
				addPolicyHeader(smc.getMessage(), policy);
			List<Assertion> alternative = getAlternative(policy);
			enableExtensions(alternative);
			
			return true;
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

	public void reset() throws WSConfigurationException
	{
		log.debug("Reseting policy configurations");
		
		//TODO: do nothing for now
	}
	
	//********************************************************
	//auxiliary methods
	
	/**
	 * Loads the configuration property defining the policy file to use
	 * 
	 * @return String representing the name of the policy file defined
	 * @throws ConfigException When an exception occurs trying to load the property
	 * @throws WSConfigurationException When the property is undefined
	 */
	private String loadFilenameFromConfig() throws ConfigException, WSConfigurationException
	{
		log.debug("Loading policy filename from config");
		
		Config.getInstance().load();
		String filename = Config.getInstance().getInitParameter(LOCAL_POLICY_PROPERTY_NAME);
		
		if(filename == null)
			throw new WSConfigurationException("Local policy property is undefined");
		
		return filename;
	}
	
	/**
	 * Loads a policy from the file given by filename
	 * 
	 * @param	filename	The name of the file to load as policy
	 * @return 				Policy defined by the specified filename
	 * @throws WSConfigurationException When the file given by filename doesn't exist
	 * 		   or doesn't specify a valid policy
	 */
	private Policy loadPolicy(String filename) throws WSConfigurationException
	{
		log.debug("Loading policy from file \"" + filename + "\"");
		if(!filename.startsWith("/"))
			filename = "/" + filename;
		Policy policy = PolicyEngine.getPolicy(WSPolicyConfigurator.class.getResourceAsStream(filename));
		
		if(policy == null)
			throw new WSConfigurationException("File \"" + filename + "\" does not define a valid policy");
		
		return policy;
	}
	
	/**
	 * Loads a policy from the file given by filename
	 * 
	 * @param	smc			SOAP message context
	 * @return 				Server policy defined for the invoked service
	 * @throws WSConfigurationException When the policy can't be retrieved
	 */
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
			element.setTextContent(strPolicy);
			System.out.println("POLICY_HEADER\n" + strPolicy + "END_POLICY_HEADER");
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}
	
	/**
	 * Interprets the policy in order to find a valid alternative
	 * 
	 * @param	policy 	The loaded policy to be interpreted
	 * @return			A list of assertions/components that must be configured 
	 * @throws WSConfigurationException When the policy has no valid alternatives
	 */
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
	
	/**
	 * Tries to enable the required extensions to meet the requirements specified by alternative
	 * 
	 * @param	alternative	The list of conditions/assertions that must be met
	 * @return 				<b>true</b> or <b>false</b> according to the success of the configuration process
	 * @throws WSConfigurationException When it wasn't possible to enable an extension
	 */
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
}
