
package step.example.flight.wsdl;

import javax.xml.ws.WebFault;
import step.example.flight.wsdl.ServiceError;
import step.example.flight.wsdl.ServiceError_Exception;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0-b26-ea3
 * Generated source version: 2.0
 * 
 */
@WebFault(name = "serviceError", targetNamespace = "urn:step:example:flight:wsdl")
public class ServiceError_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ServiceError faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public ServiceError_Exception(String message, ServiceError faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param message
     * @param cause
     */
    public ServiceError_Exception(String message, ServiceError faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: step.example.flight.wsdl.ServiceError
     */
    public ServiceError getFaultInfo() {
        return faultInfo;
    }

}
