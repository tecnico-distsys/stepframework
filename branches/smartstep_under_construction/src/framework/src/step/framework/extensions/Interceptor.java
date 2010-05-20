package step.framework.extensions;

public abstract class Interceptor {

	private Extension extension;
	
	protected Interceptor() {
	}
	
	void setExtension(Extension extension)
	{
		this.extension = extension;
	}
	
	public Extension getExtension()
	{
		return extension;
	}
}
