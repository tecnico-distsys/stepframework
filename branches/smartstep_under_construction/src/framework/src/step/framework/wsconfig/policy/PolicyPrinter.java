package step.framework.wsconfig.policy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.neethi.All;
import org.apache.neethi.Assertion;
import org.apache.neethi.AssertionBuilderFactory;
import org.apache.neethi.Constants;
import org.apache.neethi.ExactlyOne;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyComponent;
import org.apache.neethi.PolicyEngine;
import org.apache.neethi.PolicyReference;
import org.apache.neethi.builders.xml.XMLPrimitiveAssertionBuilder;
import org.apache.neethi.builders.xml.XmlPrimtiveAssertion;

public class PolicyPrinter {
	
	private static boolean full = true;	
	
	public static void printPolicy(Policy policy)
	{
		full = true;
		print(policy, 0);
	}
	
	public static void printAlternatives(Policy policy)
	{
		full = false;
		printAlternatives(policy, 0);
	}
	
	@SuppressWarnings("unchecked")
	private static void printAlternatives(Policy policy, int depth)
	{
		Iterator<List<Assertion>> it = policy.getAlternatives();
		
		int alt = 0;

		tab(depth);
		System.out.println("ALTERNATIVES");
		while(it.hasNext())
		{
			tab(depth + 1);
			System.out.println("ALT " + alt++);
			print(it.next(), depth + 1);
		}
		tab(depth);
		System.out.println("END");
	}
	
	private static void print(PolicyComponent comp, int depth)
	{
		switch(comp.getType())
		{
			case Constants.TYPE_ALL:
			{
				print((All) comp, depth);
				return;
			}
			case Constants.TYPE_ASSERTION:
			{
				print((Assertion) comp, depth);
				return;
			}
			case Constants.TYPE_EXACTLYONE:
			{
				print((ExactlyOne) comp, depth);
				return;
			}
			case Constants.TYPE_POLICY:
			{
				print((Policy) comp, depth);
				return;
			}
			case Constants.TYPE_POLICY_REF:
			{
				print((PolicyReference) comp, depth);
				return;
			}
			default:
			{
				return;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void print(All all, int depth)
	{
		tab(depth);
		System.out.println("ALL");
		
		print((List<PolicyComponent>) all.getPolicyComponents(), depth);		
	}
	
	@SuppressWarnings("unchecked")
	private static void print(Assertion assertion, int depth)
	{
		tab(depth);
		System.out.println("ASSERTION: " + assertion.getName().getLocalPart() + (assertion.isOptional() ? " (optional)" : ""));
		
		OMElement element = null;
		if(assertion instanceof STEPAssertion)
			element = ((STEPAssertion) assertion).getValue();
		else
			element = ((XmlPrimtiveAssertion) assertion).getValue();
		
		Iterator<OMNode> it = element.getChildren();
		while(it.hasNext())
		{
			try
			{
				OMNode node = it.next();
				
				if(node instanceof OMElement)
				{
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					node.serialize(stream);
					
					byte[] buf = stream.toByteArray();
					Policy policy = PolicyEngine.getPolicy(new ByteArrayInputStream(buf));
					
					if(!policy.isEmpty())
					{
						if(full)
							print(policy, depth + 1);
						else
							printAlternatives(policy, depth + 1);
						
						return;
					}
					
					Assertion nested = getAssertion((OMElement) node);
					
					if(nested != null)
					{
						print(nested, depth + 1);
						return;
					}					
				}				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void print(ExactlyOne one, int depth)
	{
		tab(depth);
		System.out.println("EXACTLYONE");
		
		print((List<PolicyComponent>) one.getPolicyComponents(), depth);
	}
	
	@SuppressWarnings("unchecked")
	private static void print(Policy policy, int depth)
	{
		tab(depth);
		System.out.println("POLICY");
		
		print((List<PolicyComponent>) policy.getPolicyComponents(), depth);
	}
	
	private static void print(PolicyReference reference, int depth)
	{
		tab(depth);
		System.out.println("REFERENCE TO: \"" + reference.getURI() + "\"");
		
		print(reference.getRemoteReferencedPolicy(reference.getURI()), depth + 1);
	}
	
	private static void tab(int depth)
	{
		for(int i=0; i<depth; i++)
		{
			System.out.print("\t");
		}
	}
	
	private static void print(List<? extends PolicyComponent> comps, int depth)
	{
		for(int i=0; i<comps.size(); i++)
		{
			print(comps.get(i), depth + 1);
		}
	}
	
	private static Assertion getAssertion(OMElement elem)
	{
		try
		{
			XMLPrimitiveAssertionBuilder pab = new XMLPrimitiveAssertionBuilder();
			
			return pab.build((OMElement) elem, new AssertionBuilderFactory());
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
