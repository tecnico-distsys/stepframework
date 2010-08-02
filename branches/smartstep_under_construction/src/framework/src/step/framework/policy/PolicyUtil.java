package step.framework.policy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import step.framework.wsdl.WSDLDocument;

public class PolicyUtil {
	
	public enum PolicyTag {
		Policy,
		PolicyReference
	}
	
	// Constants
	
	public static final String NS_POLICY12 = "http://schemas.xmlsoap.org/ws/2004/09/policy";
	public static final String NS_POLICY15 = "http://www.w3.org/ns/ws-policy";

	// Methods
	
	public static boolean isPolicy(Element elem) {
		return elem != null && elem.getNamespaceURI().equals(NS_POLICY12) && elem.getLocalName().equals("Policy");
	}
	
	public static boolean isPolicyReference(Element elem) {
		return elem != null && elem.getNamespaceURI().equals(NS_POLICY12) && elem.getLocalName().equals("PolicyReference");
	}
	
	public static NodeList getPolicyNodes(WSDLDocument doc, PolicyTag tag) {
		NodeList nodes = doc.getDocument().getElementsByTagNameNS(NS_POLICY12, tag.name());
		return nodes;
	}
	
	public static void printPolicy(Policy policy)
	{
		PolicyPrinter.printPolicy(policy);
	}
	
	public static void printAlternatives(Policy policy)
	{
		PolicyPrinter.printAlternatives(policy);
	}
	
	public static Policy intersect(Policy policy1, Policy policy2)
	{
		return PolicyIntersector.intersect(policy1, policy2);
	}
	
	public static Policy getPolicy(Node node) throws Exception
	{
		return PolicyEngine.getPolicy(toOM(node));
	}
	
	public static OMElement toOM(String string)
	{
		try
		{
			Source source = new StreamSource(new StringReader(string));
	
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    Result result = new StreamResult(baos);
		
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform(source, result);
		
		    ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
		    XMLStreamReader reader = StAXUtils.createXMLStreamReader(is);
		
		    StAXOMBuilder builder = new StAXOMBuilder(reader);
		    builder.setCache(true);
		    builder.releaseParserOnClose(true);
		
		    OMElement omElement = builder.getDocumentElement();
		    omElement.build();
		    builder.close();
		    
		    return omElement;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static OMElement toOM(Node node)
	{
		try
		{
			Source source = new DOMSource(node);
	
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    Result result = new StreamResult(baos);
		
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform(source, result);
		
		    ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
		    XMLStreamReader reader = StAXUtils.createXMLStreamReader(is);
		
		    StAXOMBuilder builder = new StAXOMBuilder(reader);
		    builder.setCache(true);
		    builder.releaseParserOnClose(true);
		
		    OMElement omElement = builder.getDocumentElement();
		    omElement.build();
		    builder.close();
		    
		    return omElement;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static String toString(Policy policy)
	{
		try
		{
			StringWriter stringWriter = new StringWriter();
			XMLOutputFactory fact = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = fact.createXMLStreamWriter(stringWriter);
			policy.serialize(writer);
			
			String string = stringWriter.toString();
			string = string.replace("&lt;", "<");
			string = string.replace("&gt;", ">");
			string = string.replace("&amp;", "&");
			string = string.replace("&#37;", "%");
			
			return string;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static Node toNode(Policy policy)
	{
		try
		{
			OMElement element = toOM(toString(policy));
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        element.serialize(baos);
	        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setNamespaceAware(true);
	        
	        return factory.newDocumentBuilder().parse(bais).getDocumentElement();
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
