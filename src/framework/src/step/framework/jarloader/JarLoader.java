package step.framework.jarloader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JarLoader extends TimerTask {
	
	private static final int TIMER_DELAY = 0;
	private static final int TIMER_PERIOD = 5000;
	
	//*********************************************************************
	//logging

    private static Log log = LogFactory.getLog(JarLoader.class);
	
	//*********************************************************************
	//members
    
	private static Map<File, JarLoader> instances = new HashMap<File, JarLoader>();
	
	//*********************************************************************
	//api
		
	public static void load(String path, boolean periodic) throws JarException
	{
		if(periodic)
			loadPeriodically(path);
		else
			loadOneTime(path);
	}

	public static void kill(String path)
	{
		try
		{
			File folder = new File(path);
			JarLoader instance = instances.get(folder);
			if(instance != null)
			{
				instance.stop();
				instances.remove(instance);
			}
		}
		catch(Exception e)
		{
			log.error("An error occured when trying to stop JarLoader instance.", e);
		}
	}

	public static void killAll()
	{
		try
		{
			Iterator<JarLoader> it = instances.values().iterator();
			while(it.hasNext())
			    it.next().stop();			
		}
		catch(Exception e)
		{
			log.error("An error occured when trying to stop running JarLoader instances.", e);
		}
		finally
		{
			instances.clear();
		}
	}

	//*********************************************************************
	//loading methods
	
	private static void loadOneTime(String path) throws JarException
	{
		try
		{
			File folder = new File(path);
			JarLoader loader = new JarLoader(folder);
			loader.run();
			log.debug("JarLoader loaded folder \"" + folder.getCanonicalPath() + "\"");
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
	
	private static void loadPeriodically(String path) throws JarException
	{
		try
		{
			File folder = new File(path);
			JarLoader instance = instances.get(folder);
			if(instance == null)
			{
				instance = new JarLoader(folder);
				instances.put(folder, instance);
				instance.start();
				log.debug("Started periodic JarLoader on folder \"" + folder.getCanonicalPath() + "\"");
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
	
	//*********************************************************************
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
	
	private void start()
	{
		timer = new Timer(true);
		timer.schedule(this, TIMER_DELAY, TIMER_PERIOD);
	}

	private void stop()
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
					log.debug("Found new Jar: " + files[i].getName());
					jar = new Jar(files[i]);
					log.debug("Installing Jar: " + files[i].getName());
					jar.install();
					log.debug("Registering Jar: " + files[i].getName());
					jars.put(files[i], jar);
					log.debug("Jar \"" + files[i].getName() + "\" successfully installed.");
				}
				catch(Exception e)
				{
					log.error("An error occured while loading Jar \"" + files[i].getName(), e);
				}
			}
		}
	}
}
