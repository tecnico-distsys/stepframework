
package step.example.flight.wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import step.example.flight.view.ReservationVoucher;
import step.example.flight.wsdl.FlightFault_Exception;
import step.example.flight.wsdl.FlightPortType;
import step.example.flight.wsdl.Passenger;
import step.example.flight.wsdl.ServiceError_Exception;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0-b26-ea3
 * Generated source version: 2.0
 * 
 */
@WebService(name = "FlightPortType", targetNamespace = "urn:step:example:flight:wsdl", wsdlLocation = "file:///C:/dev/java/simple-step/flight-ws/WebContent/WEB-INF/wsdl/flight.wsdl")
public interface FlightPortType {


    /**
     * 
     * @param arrival
     * @param departure
     * @param passenger
     * @return
     *     returns step.example.flight.view.ReservationVoucher
     * @throws FlightFault_Exception
     * @throws ServiceError_Exception
     */
    @WebMethod
    @WebResult(name = "voucher", targetNamespace = "urn:step:example:flight:wsdl")
    @RequestWrapper(localName = "createReservation", targetNamespace = "urn:step:example:flight:wsdl", className = "step.example.flight.wsdl.CreateReservation")
    @ResponseWrapper(localName = "createReservationResponse", targetNamespace = "urn:step:example:flight:wsdl", className = "step.example.flight.wsdl.CreateReservationResponse")
    public ReservationVoucher createReservation(
        @WebParam(name = "departure", targetNamespace = "urn:step:example:flight:wsdl")
        String departure,
        @WebParam(name = "arrival", targetNamespace = "urn:step:example:flight:wsdl")
        String arrival,
        @WebParam(name = "passenger", targetNamespace = "urn:step:example:flight:wsdl")
        Passenger passenger)
        throws FlightFault_Exception, ServiceError_Exception
    ;

}