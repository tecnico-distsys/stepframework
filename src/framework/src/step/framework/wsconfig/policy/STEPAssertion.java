package step.framework.wsconfig.policy;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMElement;
import org.apache.neethi.Assertion;
import org.apache.neethi.Constants;
import org.apache.neethi.PolicyComponent;

public class STEPAssertion implements Assertion {
	
	private static final QName QNOPTIONAL = new QName("optional");
	private static final QName QNEXTENSION = new QName("extension");
	
	private OMElement element;
	
	public STEPAssertion(OMElement element)
	{
		this.element = element;
	}

	public QName getName()
	{
		return element.getQName();
	}

	public boolean isOptional()
	{
		String optional = element.getAttributeValue(QNOPTIONAL);
		if(optional == null)
			return false;
		
		return optional.equals("true");
	}

	public PolicyComponent normalize()
	{
		//TODO: normalize inner policies
		return this;
	}

	public void serialize(XMLStreamWriter arg0) throws XMLStreamException
	{
		throw new UnsupportedOperationException();
	}

	public boolean equal(PolicyComponent arg0)
	{
		return this.equals(arg0);
	}

	public short getType()
	{
		return Constants.TYPE_ASSERTION;
	}
	
	public String getExtension()
	{
		return element.getAttributeValue(QNEXTENSION);
	}
}
