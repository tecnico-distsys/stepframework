package step.framework.extensions.pipe.policy;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.All;
import org.apache.neethi.Assertion;
import org.apache.neethi.ExactlyOne;
import org.apache.neethi.Policy;
import org.apache.neethi.builders.xml.XmlPrimtiveAssertion;
import org.hibernate.cfg.NotYetImplementedException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.xml.ws.developer.JAXWSProperties;

import step.framework.extensions.Extension;
import step.framework.extensions.ExtensionException;
import step.framework.extensions.ExtensionRepository;
import step.framework.extensions.ServiceInterceptor;
import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.pipe.PipeFactory;
import step.framework.extensions.pipe.ServiceInterceptorPipe;
import step.framework.extensions.pipe.WebServiceInterceptorPipe;
import step.framework.extensions.pipe.policy.config.ServiceSection;
import step.framework.extensions.pipe.policy.config.SmartSTEPConfig;
import step.framework.extensions.pipe.policy.config.WSClientSection;
import step.framework.extensions.pipe.policy.config.WSDLDefinition;
import step.framework.extensions.pipe.policy.config.WSSection;
import step.framework.policy.PolicyUtil;
import step.framework.policy.ServerPolicyObtainer;
import step.framework.service.Service;
import step.framework.ws.SOAPUtil;
import step.framework.wsdl.HTTPWSDLObtainer;
import step.framework.wsdl.WSDLObtainer;

public class PolicyPipeFactory extends PipeFactory {
	
	//********************************************************
	// Constants
	
	public static final String PIPE_POLICY_CONSTANT = "pipe.policy";

	public static final String NSSMARTSTEP = "http://stepframework.sourceforge.net/smartstep/policy";
	public static final QName QN_POLICY_HEADER = new QName(NSSMARTSTEP, "PolicyHeader");
	public static final QName QN_LOCAL_ASSERTION = new QName(NSSMARTSTEP, "Local");
	
	private static final String JAXB_CONTEXT = "step.framework.extensions.pipe.policy.config";
	private static final String SMARTSTEP_FILE = "/smartstep.xml";
	
	//********************************************************
	// Logging

    /** Logging */
    private static Log log = LogFactory.getLog(PolicyPipeFactory.class);
	
	//********************************************************
	// Pipe creators

	@SuppressWarnings("unchecked")
	public ServiceInterceptorPipe createServiceInterceptorPipe(Service svc) throws ExtensionException
	{
		try
		{
			log.trace("Creating ServiceInterceptorPipe");

			log.trace("Loading configuration file");
			SmartSTEPConfig config = loadConfig();
			
			log.trace("Loading configuration for service: " + svc.getClass().getName());
			ServiceSection section = loadServiceSection(config, svc);
			
			if(section == null)
			{
				log.trace("No configuration found for this service");
				log.trace("Skipping service interception");
				return new ServiceInterceptorPipe(null);
			}

			log.trace("Loading policy from configuration");
			Policy policy = PolicyUtil.getPolicy((Element) section.getAny());
						
			if(policy == null)
			{
				log.trace("Service policy is null");
				throw new ExtensionException("Failed to load policy");
			}

			log.trace("Normalizing policy");
			policy = (Policy) policy.normalize(true);
			log.trace("Choosing alternative from policy");
			List<Assertion> alternative = getAlternative(policy);
			log.trace("Loading extensions");
			List<Extension> exts = getExtensions(alternative);
			log.trace("Loading interceptors");
			List<ServiceInterceptor> interceptors = getServiceInterceptorList(exts);

			Policy pipePolicy = new Policy();
			for(int i=0; i<alternative.size(); i++)
				pipePolicy.addAssertion(alternative.get(i));

			log.trace("Returning pipe");
			ServiceInterceptorPipe pipe = new ServiceInterceptorPipe(interceptors);
			pipe.setConstant(PIPE_POLICY_CONSTANT, pipePolicy);
			
			return pipe;
		}
		catch(ExtensionException e)
		{
			log.trace("An error occured when trying to create the pipe", e);
			log.error(e);
			throw e;
		}
		catch(Exception e)
		{
			log.trace("An error occured when trying to create the pipe", e);
			log.error(e);
			throw new ExtensionException(e);
		}
		finally
		{
			log.trace("Exiting ServiceInterceptorPipe creator");
		}
	}
	
	public WebServiceInterceptorPipe createWebServiceInterceptorPipe(SOAPMessageContext smc) throws ExtensionException
	{
		try
		{
			log.trace("Creating WebServiceInterceptorPipe");

			log.trace("Loading configuration file");
			SmartSTEPConfig config = loadConfig();
			
			Policy policy = null;
			boolean addPolicyHeader = false;
			if(!SOAPUtil.isServerSideMessage(smc))
			{
				log.trace("Client side message (assuming outbound)");

				log.trace("Loading configuration for this endpoint");
				WSClientSection section = loadWSClientSection(config, smc); 
				
				if(section == null)
				{
					log.trace("No configuration found for this endpoint");
					log.trace("Skipping web service interception");
					return new WebServiceInterceptorPipe(null);
				}
				
				log.trace("Loading policy from configuration");
				Policy localPolicy = PolicyUtil.getPolicy((Element) section.getAny());
				log.trace("Loading policy from WSDL");
				Policy serverPolicy = getPolicyFromWSDL(section.getWsdl());
				
				log.trace("Calculating effective policy");
				policy = PolicyUtil.intersect(localPolicy, serverPolicy);
				
				if(!policy.getAlternatives().hasNext())
				{
					log.trace("Effective policy is empty");
					throw new ExtensionException("The policies are incompatible");
				}
				
				if(hasAlternatives(serverPolicy))
				{
					addPolicyHeader = true;
				}
			}
			else
			{
				log.trace("Server side message (assuming inbound)");

				log.trace("Loading configuration for this endpoint");
				WSSection section = loadWSSection(config, smc); 
				
				if(section == null)
				{
					log.trace("No configuration found for this endpoint");
					log.trace("Skipping web service interception");
					return new WebServiceInterceptorPipe(null);
				}
				
				log.trace("Loading policy from WSDL");
				Policy localPolicy = getPolicyFromWSDL(section.getWsdl());
				if(hasAlternatives(localPolicy))
				{
					log.trace("Local policy has multiple alternatives");
					
					log.trace("Loading policy from SOAPHeader");
					Policy headerPolicy = getPolicyFromSOAPHeader(smc);

					log.trace("Checking validity of SOAPHeader policy");
					policy = PolicyUtil.intersect(localPolicy, headerPolicy);

					if(!policy.getAlternatives().hasNext())
					{
						log.trace("SOAPHeader policy is incompatible with server policy");
						throw new ExtensionException("Policy specified by the client is incompatible with server policy");
					}
				}
				else
				{
					log.trace("Local policy has less than two alternatives");

					log.trace("Using local policy as effective policy");
					policy = (Policy) localPolicy.normalize(true);
				}
			}

			log.trace("Choosing alternative from effective policy");
			List<Assertion> alternative = getAlternative(policy);
			log.trace("Loading extensions");
			List<Extension> exts = getExtensions(alternative);
			log.trace("Loading interceptors");
			List<WebServiceInterceptor> interceptors = getWebServiceInterceptorList(exts);

			Policy pipePolicy = new Policy();
			for(int i=0; i<alternative.size(); i++)
				pipePolicy.addAssertion(alternative.get(i));
			
			if(addPolicyHeader)
			{
				log.trace("Adding policy header");
				Policy headerPolicy = removeLocalAssertions(pipePolicy);			
				addPolicyHeader(smc, headerPolicy);
			}			

			log.trace("Returning pipe");
			WebServiceInterceptorPipe pipe = new WebServiceInterceptorPipe(interceptors);
			pipe.setConstant(PIPE_POLICY_CONSTANT, pipePolicy);
			
			return pipe;
		}
		catch(ExtensionException e)
		{
			log.trace("An error occured when trying to create the pipe", e);
			log.error(e);
			throw e;
		}
		catch(Exception e)
		{
			log.trace("An error occured when trying to create the pipe", e);
			log.error(e);
			throw new ExtensionException(e);
		}
		finally
		{
			log.trace("Exiting WebServiceInterceptorPipe creator");
		}
	}
	
	//********************************************************
	// Aux - config
	
	@SuppressWarnings("unchecked")
	private SmartSTEPConfig loadConfig() throws Exception
	{
		JAXBContext jaxb = JAXBContext.newInstance(JAXB_CONTEXT);

		URL url = PolicyPipeFactory.class.getResource(SMARTSTEP_FILE);
		Unmarshaller unmarsh = jaxb.createUnmarshaller();
		JAXBElement<SmartSTEPConfig> elem = (JAXBElement<SmartSTEPConfig>) unmarsh.unmarshal(url);
		SmartSTEPConfig config = elem.getValue();
		
		return config;
	}
	
	@SuppressWarnings("unchecked")
	private ServiceSection loadServiceSection(SmartSTEPConfig config, Service svc) throws Exception
	{
		List<ServiceSection> sections = config.getService();
		ServiceSection section = null;
		Class<Service> sectionClass = null;
		
		for(int i=0; i<sections.size(); i++)
		{
			String className = sections.get(i).getClazz();
			Class<Service> clazz = (Class<Service>) Class.forName(className);
			
			if(clazz.isAssignableFrom(svc.getClass()))
			{
				if(section == null || clazz.isAssignableFrom(sectionClass))
				{
					section = sections.get(i);
					sectionClass = clazz;
				}
			}
		}
		
		return section;
	}
	
	private WSClientSection loadWSClientSection(SmartSTEPConfig config, SOAPMessageContext smc)
	{
		List<WSClientSection> sections = config.getWsclient();
		
		for(int i=0; i<sections.size(); i++)
		{
			String url = sections.get(i).getEndpoint();
			if(url.equals((String) smc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY)))
			{
				return sections.get(i);
			}
		}
		
		return null;
	}
	
	private WSSection loadWSSection(SmartSTEPConfig config, SOAPMessageContext smc)
	{
		List<WSSection> sections = config.getWs();
		
		for(int i=0; i<sections.size(); i++)
		{
			String url = sections.get(i).getEndpoint();
			if(url.equals((String) smc.get(JAXWSProperties.HTTP_REQUEST_URL)))
			{
				return sections.get(i);
			}
		}
		
		return null;
	}
	
	//********************************************************
	// Aux - policy obtainers
	
	private Policy getPolicyFromWSDL(WSDLDefinition wsdlDef) throws Exception
	{
		String wsdlUrl = wsdlDef.getLocation();		
		WSDLObtainer wsdlObt = null;
		
		switch(wsdlDef.getMethod())
		{
			case HTTP:
			{
				wsdlObt = new HTTPWSDLObtainer(wsdlUrl);
				break;
			}
			case WSMEX:
			{
				throw new NotYetImplementedException("WS-MetadataExchange has not been implemented yet.");
			}
			default:
			{
				wsdlObt = new HTTPWSDLObtainer(wsdlUrl);
				break;
			}
		}
		
		ServerPolicyObtainer policyObt = new ServerPolicyObtainer(wsdlObt);
		
		Policy policy = policyObt.getServicePolicy();

		if(policy == null)
			throw new ExtensionException("Remote WSDL has no valid policy");
		
		return policy;
	}
	
	@SuppressWarnings("unchecked")
	private Policy getPolicyFromSOAPHeader(SOAPMessageContext smc) throws Exception
	{
		SOAPMessage message = smc.getMessage();
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
				
				Policy policy = PolicyUtil.getPolicy(current);
				
				if(policy == null)
					throw new ExtensionException("SOAPHeader has no valid policy");
				
				return policy;
			}
		}
		
		throw new ExtensionException("SOAPHeader has no valid policy");
	}
	
	//********************************************************
	// Aux - utils
	
	@SuppressWarnings("unchecked")
	private Policy removeLocalAssertions(Policy policy)
	{
		Policy clean = new Policy();
		ExactlyOne one = new ExactlyOne();
		
		Iterator<List<Assertion>> it = policy.getAlternatives();
		while(it.hasNext())
		{
			All all = new All();
			List<Assertion> assertions = it.next();
			
			for(int i=0; i<assertions.size(); i++)
			{
				Assertion current = assertions.get(i);
				OMElement element = ((XmlPrimtiveAssertion) current).getValue();
				String local = element.getAttributeValue(QN_LOCAL_ASSERTION);
				
				if(!Boolean.parseBoolean(local))
				{
					all.addPolicyComponent(current);
				}
			}
			
			one.addPolicyComponent(all);
		}
		
		clean.addPolicyComponent(one);		
		return clean;
	}
		
	private void addPolicyHeader(SOAPMessageContext smc, Policy policy) throws ExtensionException
	{
		try
		{			
			SOAPMessage message = smc.getMessage();
			SOAPEnvelope soapEnvelope = message.getSOAPPart().getEnvelope();
			SOAPHeader soapHeader = soapEnvelope.getHeader();
			if(soapHeader == null)
				soapHeader = soapEnvelope.addHeader();

			SOAPHeaderElement element = soapHeader.addHeaderElement(PolicyPipeFactory.QN_POLICY_HEADER);
			
			Node node = PolicyUtil.toNode(policy);
			node = element.getOwnerDocument().importNode(node, true);
			
			element.appendChild(node);
		}
		catch(Exception e)
		{
			throw new ExtensionException(e);
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
	
	@SuppressWarnings("unchecked")
	private List<Assertion> getAlternative(Policy policy) throws ExtensionException
	{
		log.trace("Analyzing policy for a valid configuration alternative");
		Iterator<List<Assertion>> it = policy.getAlternatives();
		
		while(it.hasNext())
		{
			List<Assertion> assertions = it.next();
			
			int i;
			for(i=0; i<assertions.size(); i++)
			{
				if(ExtensionRepository.getInstance().getExtension(assertions.get(i).getName()) == null)
				{
					log.trace("An alternative requires an unsupported feature");
					log.trace("Assessing next alternative");
					break;
				}
			}
			
			if(i == assertions.size())
			{
				log.trace("Found a valid alternative");
				return assertions;
			}
		}
		
		throw new ExtensionException("The policy has no valid alternatives");
	}
	
	private List<Extension> getExtensions(List<Assertion> alternative) throws ExtensionException
	{
		List<Extension> exts = new LinkedList<Extension>();
		
		log.trace("Mapping of assertions:");
		for(int i=0; i<alternative.size(); i++)
		{
			Assertion assertion = alternative.get(i);
			Extension extension = getExtensionRepository().getExtension(assertion.getName()); 
			exts.add(extension);
			log.trace("\t" + assertion.getName().getLocalPart() + " handled by extension " + extension.getID());
		}
		log.trace("End of mapping");
		
		return exts;
	}
}
