package step.framework.extensions;

public abstract class ExtensionListener {
	
	private Extension extension;
	
	void setExtension(Extension extension)
	{
		this.extension = extension;
	}
	
	protected Extension getExtension()
	{
		return extension;
	}
	
	protected abstract void extensionInitialized() throws ExtensionException;
	protected abstract void extensionDestroyed() throws ExtensionException;
	
}
