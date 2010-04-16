package step.framework.wsconfig;

public interface WSConfigurator {
	
	/**
	 * Configures the extension engine.
	 * <p>
	 * Returns true or false according to configuration success.
	 * 
	 * @return								<b>true</b> or <b>false</b> according to configuration success
	 * @throws	WSConfigurationException	If an unexpected, irrecoverable exception occured	
	 */
	public boolean config() throws WSConfigurationException;
	
	/**
	 * Resets the configured changes made to the extension engine.
	 * 
	 * @throws	WSConfigurationException	If an unexpected, irrecoverable exception occured	
	 */
	public void reset() throws WSConfigurationException;

}
