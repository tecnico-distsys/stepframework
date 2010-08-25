package step.framework.policy;

import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.neethi.All;
import org.apache.neethi.Assertion;
import org.apache.neethi.ExactlyOne;
import org.apache.neethi.Policy;
import org.apache.neethi.builders.xml.XmlPrimtiveAssertion;

import step.framework.extensions.pipe.policy.PolicyPipeFactory;

/**
 * Implementation of the WS-Policy intersection operation.
 * 
 * Two assertions are considered equivalent if they have the same namespace and local name.
 * The intersection of two equivalent assertions is the assertion from the primary policy.
 * 
 * Assertions marked with the SmartSTEP Local attribute will not be intersected, and only
 * the ones from the primary policy will be kept. 
 *  
 * @author João Leitão
 *
 */
public class PolicyIntersector {

	/**
	 * Intersect the two policies considering policy1 as primary and policy2 as secondary.
	 * 
	 * @param	policy1		The primary policy.
	 * @param	policy2		The secondary policy.
	 * 
	 * @return	The Policy resulting from the intersection of the two policies.
	 */
	@SuppressWarnings("unchecked")
	public static Policy intersect(Policy policy1, Policy policy2)
	{
		policy1 = (Policy) policy1.normalize(true);
		policy2 = (Policy) policy2.normalize(true);
		
		Policy result = new Policy();
		ExactlyOne exactlyOne = new ExactlyOne();
		
		Iterator<List<Assertion>> it = policy1.getAlternatives();
		
		while(it.hasNext())
		{
			List<Assertion> alt = it.next();
			
			if(hasCompatible(alt, policy2))
			{
				All all = new All();
				
				all.addPolicyComponents(alt);
				
				exactlyOne.addPolicyComponent(all);
			}
		}
		
		result.addPolicyComponent(exactlyOne);		
		return (Policy) result.normalize(true);
	}
	
	@SuppressWarnings("unchecked")
	private static boolean hasCompatible(List<Assertion> alt1, Policy policy)
	{
		Iterator<List<Assertion>> it = policy.getAlternatives();
		
		while(it.hasNext())
		{
			List<Assertion> alt2 = it.next();
			
			if(isCompatible(alt1, alt2))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean isCompatible(List<Assertion> alt1, List<Assertion> alt2)
	{
		if(alt1.size() == 0 && alt2.size() == 0)
			return true;
		
		for(int i=0; i<alt1.size(); i++)
		{
			if(!hasCompatibleAssertion(alt1.get(i), alt2))
				return false;
		}

		for(int i=0; i<alt2.size(); i++)
		{
			if(!hasCompatibleAssertion(alt2.get(i), alt1))
				return false;
		}		
		
		return true;
	}
	
	private static boolean hasCompatibleAssertion(Assertion assertion, List<Assertion> alt)
	{
		if(isLocal(assertion))
			return true;
		
		for(int i=0; i<alt.size(); i++)
		{
			if(isCompatible(assertion, alt.get(i)))
				return true;
		}
		
		return false;
	}
	
	private static boolean isCompatible(Assertion assertion1, Assertion assertion2)
	{
		return assertion1.getName().equals(assertion2.getName());
	}
	
	private static boolean isLocal(Assertion assertion)
	{
		OMElement element = ((XmlPrimtiveAssertion) assertion).getValue();
		String local = element.getAttributeValue(PolicyPipeFactory.QN_LOCAL_ASSERTION);
		
		return Boolean.parseBoolean(local);
	}
}
