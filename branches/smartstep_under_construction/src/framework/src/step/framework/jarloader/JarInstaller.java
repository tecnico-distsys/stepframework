package step.framework.jarloader;

import java.util.Properties;

public abstract class JarInstaller {
	
	private Properties properties;
	
	protected JarInstaller()
	{
		this.properties = new Properties();
	}
	
	final void setProperties(Properties properties)
	{
		this.properties = (properties == null) ? new Properties() : properties;
	}
	
	protected Properties getProperties()
	{
		return properties;
	}
	
	protected abstract void install() throws JarException;
	
}
