package step.framework.jarloader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JarLoader {
	
	public static Map<File, JarLoader> instances;
	
	public static void load(String path) throws JarException
	{
		if(instances == null)
			instances = new HashMap<File, JarLoader>();
		
		File folder = new File(path);
		JarLoader instance = instances.get(folder);
		if(instance == null)
			instance = new JarLoader(folder);
		
		instance.update();
	}
	
	//*********************************************************************+
	//jar loader implementation
	
	private File folder;
	private Map<File, Jar> jars;
	
	private JarLoader(File folder) throws JarException
	{
		this.folder = folder;
		if(!folder.exists() || folder.isDirectory())
			throw new JarException("The path \"" + folder.getAbsolutePath() + "\" does not specify an existing folder.");
		
		this.jars = new HashMap<File, Jar>();
	}
	
	@SuppressWarnings("finally")
	private final void update()
	{
		//TODO: delete current loaded jars, if possible
		
		File[] files = folder.listFiles();
		if(files == null)
			return;
		
		for(int i=0; i<files.length; i++)
		{
			Jar jar = jars.get(files[i]);
			if(jar == null)
			{
				try
				{
					jar = new Jar(files[i]);
					jar.install();
					jars.put(files[i], jar);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					continue;
				}
			}
			else if(jar.lastModified() < files[i].lastModified())
			{
				//TODO: update the contents of the jar, if possible, otherwise ignore
				continue;
			}				
		}
	}
}
