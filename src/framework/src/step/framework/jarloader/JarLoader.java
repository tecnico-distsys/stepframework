package step.framework.jarloader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class JarLoader extends TimerTask {
	
	private static final int TIMER_DELAY = 0;
	private static final int TIMER_PERIOD = 5000;
	
	//*********************************************************************+
	//singleton
	
	public static Map<File, JarLoader> instances;
	
	public static void loadOneTime(String path) throws JarException
	{
		try
		{
			File folder = new File(path);
			JarLoader loader = new JarLoader(folder);
			loader.run();
			System.out.println("[DEBUG] JarLoader loaded folder \"" + folder.getCanonicalPath() + "\"");
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
				instance.start();
				System.out.println("[DEBUG] Started JarLoader on folder \"" + folder.getCanonicalPath() + "\"");
			}
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

	public static void kill() throws JarException
	{
		try
		{
			Iterator<JarLoader> it = instances.values().iterator();
			while(it.hasNext())
			    it.next().stop();
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
	private Timer timer;
	
	private JarLoader(File folder) throws JarException, IOException
	{
		this.folder = folder;
		if(!folder.exists() || !folder.isDirectory())
			throw new JarException("The path \"" + folder.getCanonicalPath() + "\" does not specify an existing folder.");
		
		this.jars = new HashMap<File, Jar>();
	}
	
	public void start()
	{
		timer = new Timer(true);
		timer.schedule(this, TIMER_DELAY, TIMER_PERIOD);
	}

	public void stop()
	{
		if(timer != null)
		    timer.cancel();
	}
	
	public void run()
	{
		File[] files = folder.listFiles();
		if(files == null)
			return;
		
		for(int i=0; i<files.length; i++)
		{
			if(!files[i].getName().toLowerCase().endsWith(".jar"))
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
