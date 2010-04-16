package step.framework.wsconfig;

import step.framework.extensions.Extension;
import step.framework.extensions.ExtensionEngine;

public class ExtensionLiaison {

	public static void enable(String id) throws WSConfigurationException
	{
		try
		{
			Extension extension = ExtensionEngine.getInstance().getExtension(id);
			
			if(extension == null)
				throw new WSConfigurationException("No configured extension with id \"" + id + "\"");
			
			extension.enable();
		}
		catch(WSConfigurationException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}
	
	public static void disable(String id) throws WSConfigurationException
	{
		try
		{
			Extension extension = ExtensionEngine.getInstance().getExtension(id);
			
			if(extension == null)
				throw new WSConfigurationException("No configured extension with id \"" + id + "\"");
			
			extension.disable();
		}
		catch(WSConfigurationException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new WSConfigurationException(e);
		}
	}
}
