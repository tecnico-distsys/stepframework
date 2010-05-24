package step.framework.jarloader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

class Jar {
	
	private static final String CONFIGFILE = "installer.properties";
	private static final String INSTALLERPROPERTY = "installer.class";
	
	private File file;
	private JarInstaller installer;
	private URLClassLoader classLoader;
	
	Jar(File file) throws JarException
	{
		this.file = file;
		initClassLoader();
		initInstaller();
	}
	
	long lastModified()
	{
		return file.lastModified();
	}
	
	void install() throws Exception
	{
		installer.install();
	}
	
	private void initClassLoader() throws JarException
	{
		try
		{
			System.out.println("[DEBUG] Creating a new ClassLoader for Jar: " + file.getName());
			String urlStr = "jar:" + file.toURI() + "!/";
			URL url = new URL(urlStr);

			classLoader = URLClassLoader.newInstance(new URL[]{url}, this.getClass().getClassLoader());
		}
		catch(Exception e)
		{
			throw new JarException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initInstaller() throws JarException
	{		
		try
		{
			InputStream is = classLoader.getResourceAsStream(CONFIGFILE);
			if(is == null)
				throw new JarException("The Jar file \"" + file.getName() + "\" does not contain a valid configuration file: " + CONFIGFILE);

			System.out.println("[DEBUG] Loading properties from \"" + CONFIGFILE + "\".");
			Properties properties = new Properties();
			properties.load(is);
			is.close();

			String installerProperty = (String) properties.remove(INSTALLERPROPERTY);
			if(installerProperty == null)
				throw new JarException("The Jar file \"" + file.getName() + "\" does not define the \"" + INSTALLERPROPERTY + "\" property");

			System.out.println("[DEBUG] Loading installer \"" + installerProperty + "\".");
			Class<JarInstaller> installerClass = (Class<JarInstaller>) classLoader.loadClass(installerProperty);
			installer = installerClass.newInstance();
			installer.setProperties(properties);
		}
		catch(JarException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new JarException(e);
		}
	}
}
