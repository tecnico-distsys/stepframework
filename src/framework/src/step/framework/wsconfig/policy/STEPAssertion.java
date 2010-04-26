package step.framework.wsconfig.policy;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.neethi.builders.xml.XmlPrimtiveAssertion;

public class STEPAssertion extends XmlPrimtiveAssertion {
	
	private static final QName QNEXTENSION = new QName("extension");
	
	public STEPAssertion(OMElement element)
	{
		super(element);
	}
	
	public String getExtension()
	{
		return getValue().getAttributeValue(QNEXTENSION);
	}
}
