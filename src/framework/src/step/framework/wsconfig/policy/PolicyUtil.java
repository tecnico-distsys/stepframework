package step.framework.wsconfig.policy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.neethi.Policy;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import step.framework.wsconfig.wsdl.WSDLDocument;

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
		//TODO
		return false;
	}
	
	public static boolean isPolicyReference(Element elem) {
		//TODO
		return false;
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
		return Intersector.intersect(policy1, policy2);
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
			e.printStackTrace();
			return null;
		}
	}
}
