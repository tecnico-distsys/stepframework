package step.framework.wsdl;

/**
 * Interface for WSDL obtainers.
 * 
 * @author João Leitão
 *
 */
public interface WSDLObtainer {
	
	/**
	 * Loads the WSDLDocument object.
	 * 
	 * @return	The WSDLDocument loaded.
	 * @throws	WSDLException	When an error occurs during the loading process.
	 */
	public WSDLDocument getWSDL() throws WSDLException;

}
