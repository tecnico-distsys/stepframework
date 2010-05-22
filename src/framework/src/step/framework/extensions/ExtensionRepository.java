package step.framework.extensions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ExtensionRepository {
	
	public static final String DEFAULT_PATH = "/extensions";
	
	private static ExtensionRepository instance;
	
	public static ExtensionRepository getInstance()
	{
		if(instance == null)
			instance = new ExtensionRepository();
		
		return instance;
	}
	
	//*******************************************************************
	//extension repository implementation
	
	private Map<String, Extension> extensions;
	
	private ExtensionRepository()
	{
		this.extensions = new HashMap<String, Extension>();
	}
	
	public List<Extension> getExtensions()
	{
		return new LinkedList<Extension>(extensions.values());
	}
	
	public Extension getExtension(String id)
	{
		return extensions.get(id);
	}
	
	public void install(Extension ext) throws ExtensionException
	{
		if(!extensions.containsKey(ext.getID()))
			extensions.put(ext.getID(), ext);
	}
}
