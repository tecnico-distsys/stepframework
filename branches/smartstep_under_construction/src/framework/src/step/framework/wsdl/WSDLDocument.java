package step.framework.wsdl;

import org.w3c.dom.Document;

/**
 * Representation of a WSDL document.
 * 
 * It is a wrapper for a Document object.
 * 
 * @author João Leitão
 *
 */
public class WSDLDocument {
	
	/**WSDL 1.1 namespace*/
	public static final String NS_WSDL11 = "http://schemas.xmlsoap.org/wsdl/";
	/**WSDL 2.0 namespace*/
	public static final String NS_WSDL20 = "http://www.w3.org/ns/wsdl";

	/**WSDL 1.1 binding tag name*/
	public static final String TAG_BINDING = "binding";
	/**WSDL 1.1 service tag name*/
	public static final String TAG_SERVICE = "service";
	
	private Document doc;
	
	/**
	 * Creates a new WSDLDocument object to wrap the given Document object.
	 * 
	 * @param doc
	 */
	public WSDLDocument(Document doc)
	{
		this.doc = doc;
	}
	
	/**
	 * Access the wrapped Document object.
	 * 
	 * @return	The wrapped Document object.
	 */
	public Document getDocument()
	{
		return doc;
	}
}
