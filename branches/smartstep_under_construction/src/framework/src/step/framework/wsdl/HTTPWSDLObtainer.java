package step.framework.wsdl;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * Loads a WSDLDocument using an HTTP-GET operation.
 * 
 * @author João Leitão
 *
 */
public class HTTPWSDLObtainer implements WSDLObtainer {

	private String txt_url;

	/**
	 * Create a new HTTPWSDLObtainer for the WSDLDocument at the specified url parameter.
	 * 
	 * @param	url	The URL of the WSDLDocument to obtain.
	 */
	public HTTPWSDLObtainer(String url)
	{
		this.txt_url = url;
	}

	public WSDLDocument getWSDL() throws WSDLException
	{
		try
		{
			URL url = new URL(txt_url);
	
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true);
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(url.openStream());
	
			return new WSDLDocument(doc);
		}
		catch(Exception e)
		{
			throw new WSDLException(e);
		}
	}
}
