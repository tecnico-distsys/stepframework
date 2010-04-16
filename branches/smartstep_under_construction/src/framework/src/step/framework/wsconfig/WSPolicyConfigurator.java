package step.framework.wsconfig;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;

import step.framework.config.Config;
import step.framework.config.ConfigException;

/*
 * Utility class for policy loading
 */
public class WSPolicyConfigurator implements WSConfigurator {
	
	private static final String FILENAME_PROPERTY_NAME = "wsconfig.policy.filename";

    /** Logging */
    private Log log;
	
	//********************************************************
	//constructors
	
	public WSPolicyConfigurator()
	{
    	this.log = LogFactory.getLog(WSPolicyConfigurator.class);
	}
	
	//********************************************************
	//configurator interface

	public boolean config() throws WSConfigurationException
	{
		log.debug("Starting web service policy configuration");
		
		try
		{
			String filename = loadFilenameFromConfig();
			Policy policy = loadPolicy(filename);
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
		String filename = Config.getInstance().getInitParameter(FILENAME_PROPERTY_NAME);
		
		if(filename == null)
			throw new WSConfigurationException("Policy filename property definition is undefined");
		
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
		
		Policy policy = PolicyEngine.getPolicy(WSPolicyConfigurator.class.getResourceAsStream(filename));
		
		if(policy == null)
			throw new WSConfigurationException("File \"" + filename + "\" does not define a valid policy");
		
		return policy;
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
			String id = alternative.get(i).getName().getLocalPart();
			log.debug("Enabling extension: " + id);
			ExtensionLiaison.enable(id);
		}
		
		log.debug("Extensions configured successfully");
	}
}
