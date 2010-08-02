package step.framework.wsdl;

import org.w3c.dom.Document;

public class WSDLDocument {
	
	public static final String NS_WSDL11 = "http://schemas.xmlsoap.org/wsdl/";
	public static final String NS_WSDL20 = "http://www.w3.org/ns/wsdl";

	public static final String TAG_BINDING = "binding";
	public static final String TAG_SERVICE = "service";
	
	private Document doc;
	
	public WSDLDocument(Document doc)
	{
		this.doc = doc;
	}
	
	public Document getDocument() {
		return doc;
	}
}
