package step.framework.extensions;

import step.framework.extensions.pipe.Pipe;


public abstract class Interceptor {

	private Extension extension;
	private Pipe pipe;
	
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
	
	public void setPipe(Pipe pipe)
	{
		this.pipe = pipe;
	}
	
	public Pipe getPipe()
	{
		return pipe;
	}
}
