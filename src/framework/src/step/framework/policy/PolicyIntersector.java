package step.framework.policy;

import java.util.Iterator;
import java.util.List;

import org.apache.neethi.All;
import org.apache.neethi.Assertion;
import org.apache.neethi.ExactlyOne;
import org.apache.neethi.Policy;

public class PolicyIntersector {

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
		for(int i=0; i<alt.size(); i++)
		{
			if(isCompatible(assertion, alt.get(i)))
				return true;
		}
		
		return false;
	}
	
	private static boolean isCompatible(Assertion assertion1, Assertion assertion2)
	{
		//TODO: consider nested policies
		return assertion1.getName().equals(assertion2.getName());
	}
}
