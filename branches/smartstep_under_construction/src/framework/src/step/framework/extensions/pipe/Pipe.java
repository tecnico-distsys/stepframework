package step.framework.extensions.pipe;

import java.util.HashMap;
import java.util.Map;

public abstract class Pipe {
	
	private Map<String, Object> constants;
	private Map<String, Object> variables;
	
	protected Pipe()
	{
		this.constants = new HashMap<String, Object>();
		this.variables = new HashMap<String, Object>();
	}
	
	public Object getVariable(String name)
	{
		return variables.get(name);
	}
	
	public void setVariable(String name, Object value)
	{
		variables.put(name, value);
	}
	
	public Object getConstant(String name)
	{
		return constants.get(name);
	}
	
	public void setConstant(String name, Object value)
	{
		if(constants.containsKey(name))
			throw new IllegalStateException("Can't override an existant constant.");
		
		constants.put(name, value);
	}
}
