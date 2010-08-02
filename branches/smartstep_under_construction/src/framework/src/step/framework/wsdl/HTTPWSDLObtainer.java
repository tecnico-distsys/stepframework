package step.framework.wsdl;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HTTPWSDLObtainer implements WSDLObtainer {
	
	public static boolean print = false;

	private String txt_url;

	public HTTPWSDLObtainer(String url) {
		this.txt_url = url;
	}

	public WSDLDocument getWSDL() throws Exception {		
		URL url = new URL(txt_url);

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(url.openStream());
		
		if(print)
			print(doc, 0);

		return new WSDLDocument(doc);
	}
	
	/**
	 * DEBUG ONLY
	 */
	private void print(Node node, int depth)
	{		
		NodeList children = node.getChildNodes();
		
		String space = "";
		for(int i=0; i<depth; i++)
		{
			space += "\t";
		}
		
		for(int i=0; i<children.getLength(); i++)
		{
			Node current = children.item(i);
			
			if(current.getNodeType() != Node.TEXT_NODE && current.getNodeType() != Node.COMMENT_NODE)
			{
				System.out.println(space + current.getNodeName() + " -> " + current.getNamespaceURI());
				print(current, depth + 1);
			}
		}
	}
}
