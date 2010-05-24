package step.framework.jarloader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JarLoader {
	
	public static Map<File, JarLoader> instances;
	
	public static void load(String path) throws JarException
	{
		try
		{
			if(instances == null)
				instances = new HashMap<File, JarLoader>();
			
			File folder = new File(path);
			JarLoader instance = instances.get(folder);
			if(instance == null)
			{
				instance = new JarLoader(folder);
				instances.put(folder, instance);
			}
			System.out.println("[DEBUG] Updating JarLoader on folder \"" + folder.getCanonicalPath() + "\"");
			instance.update();
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
	
	//*********************************************************************+
	//jar loader implementation
	
	private File folder;
	private Map<File, Jar> jars;
	
	private JarLoader(File folder) throws JarException, IOException
	{
		this.folder = folder;
		if(!folder.exists() || !folder.isDirectory())
			throw new JarException("The path \"" + folder.getCanonicalPath() + "\" does not specify an existing folder.");
		
		this.jars = new HashMap<File, Jar>();
	}
	
	@SuppressWarnings("finally")
	private final void update()
	{
		File[] files = folder.listFiles();
		if(files == null)
			return;
		
		for(int i=0; i<files.length; i++)
		{
			if(!files[i].getName().endsWith(".jar") && !files[i].getName().endsWith(".JAR"))
				continue;
			
			Jar jar = jars.get(files[i]);
			if(jar == null)
			{
				try
				{
					System.out.println("[DEBUG] Found new Jar: " + files[i].getName());
					jar = new Jar(files[i]);
					System.out.println("[DEBUG] Installing Jar: " + files[i].getName());
					jar.install();
					System.out.println("[DEBUG] Registering Jar: " + files[i].getName());
					jars.put(files[i], jar);
					System.out.println("[DEBUG] Jar \"" + files[i].getName() + "\" successfully installed.");
				}
				catch(Exception e)
				{
					System.out.println("[DEBUG] An error occured while loading Jar \"" + files[i].getName() + "\". Details:");
					e.printStackTrace();
				}
			}
		}
	}
}
