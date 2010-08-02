package step.framework.extensions;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;


public class ExtensionRepository {

	private static String REL_PATH = "extensions";
	public static String LOADER_PATH = initDefaultPath();
	
	private static ExtensionRepository instance;
	
	public static ExtensionRepository getInstance()
	{
		if(instance == null)
			instance = new ExtensionRepository();
		
		return instance;
	}
	
	private static String initDefaultPath()
	{
		String step_home = System.getenv("STEP_HOME");
		
		if(step_home == null)
		{
			System.out.println("STEP_HOME property is undefined");
			return REL_PATH;
		}
		
		if(!step_home.endsWith("/") && !step_home.endsWith("\\"))
		{
			step_home += File.separator;
		}

		String extensions = step_home + REL_PATH; 
		System.out.println("Using folder \"" + extensions + "\" for extension JarLoader.");
		
		return extensions;
	}
	
	//*******************************************************************
	//extension repository implementation
	
	private Map<String, Extension> extensions;
	private Map<QName, List<Extension>> wspMapping;
	
	private ExtensionRepository()
	{
		this.extensions = new HashMap<String, Extension>();
		this.wspMapping = new HashMap<QName, List<Extension>>();
	}
	
	public List<Extension> getExtensions()
	{
		return new LinkedList<Extension>(extensions.values());
	}
	
	public List<Extension> getExtensions(QName wspNamespace)
	{
		List<Extension> exts = wspMapping.get(wspNamespace);
		if(exts == null)
			exts = new LinkedList<Extension>();
		
		return exts;
	}
	
	public Extension getExtension(String id)
	{
		return extensions.get(id);
	}
	
	public void install(Extension ext) throws ExtensionException
	{
		if(!extensions.containsKey(ext.getID()))
			extensions.put(ext.getID(), ext);
		
		QName[] wspNss = ext.getWSPNamespaces();
		
		for(int i=0; i < wspNss.length; i++)
		{
			if(wspNss[i] != null)
				addWSPMapping(wspNss[i], ext);
		}
	}
	
	private void addWSPMapping(QName ns, Extension ext)
	{
		List<Extension> exts = wspMapping.get(ns);
		if(exts == null)
		{
			exts = new LinkedList<Extension>();
			wspMapping.put(ns, exts);
		}
		
		exts.add(ext);
	}
}
