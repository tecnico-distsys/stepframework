package step.framework.wsconfig;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.neethi.Assertion;
import org.apache.neethi.AssertionBuilderFactory;
import org.apache.neethi.builders.AssertionBuilder;

public class STEPAssertionBuilder implements AssertionBuilder {

	public Assertion build(OMElement arg0, AssertionBuilderFactory arg1) throws IllegalArgumentException
	{
		return new STEPAssertion(arg0);
	}

	public QName[] getKnownElements()
	{
		QName[] names = new QName[2];
		
		names[0] = new QName(WSPolicyConfigurator.NSSMARTSTEP, "Cipher");
		names[1] = new QName(WSPolicyConfigurator.NSSMARTSTEP, "Identification");
		
		return names;
	}

}
