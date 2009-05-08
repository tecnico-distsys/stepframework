
package step.example.flight.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import step.example.flight.wsdl.CreateReservation;
import step.example.flight.wsdl.CreateReservationResponse;
import step.example.flight.wsdl.FlightFault;
import step.example.flight.wsdl.ObjectFactory;
import step.example.flight.wsdl.Passenger;
import step.example.flight.wsdl.ServiceError;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the step.example.flight.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateReservation_QNAME = new QName("urn:step:example:flight:wsdl", "createReservation");
    private final static QName _FlightFault_QNAME = new QName("urn:step:example:flight:wsdl", "flightFault");
    private final static QName _Passenger_QNAME = new QName("urn:step:example:flight:wsdl", "passenger");
    private final static QName _CreateReservationResponse_QNAME = new QName("urn:step:example:flight:wsdl", "createReservationResponse");
    private final static QName _ServiceError_QNAME = new QName("urn:step:example:flight:wsdl", "serviceError");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: step.example.flight.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Passenger }
     * 
     */
    public Passenger createPassenger() {
        return new Passenger();
    }

    /**
     * Create an instance of {@link CreateReservationResponse }
     * 
     */
    public CreateReservationResponse createCreateReservationResponse() {
        return new CreateReservationResponse();
    }

    /**
     * Create an instance of {@link CreateReservation }
     * 
     */
    public CreateReservation createCreateReservation() {
        return new CreateReservation();
    }

    /**
     * Create an instance of {@link ServiceError }
     * 
     */
    public ServiceError createServiceError() {
        return new ServiceError();
    }

    /**
     * Create an instance of {@link FlightFault }
     * 
     */
    public FlightFault createFlightFault() {
        return new FlightFault();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:step:example:flight:wsdl", name = "createReservation")
    public JAXBElement<CreateReservation> createCreateReservation(CreateReservation value) {
        return new JAXBElement<CreateReservation>(_CreateReservation_QNAME, CreateReservation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FlightFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:step:example:flight:wsdl", name = "flightFault")
    public JAXBElement<FlightFault> createFlightFault(FlightFault value) {
        return new JAXBElement<FlightFault>(_FlightFault_QNAME, FlightFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Passenger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:step:example:flight:wsdl", name = "passenger")
    public JAXBElement<Passenger> createPassenger(Passenger value) {
        return new JAXBElement<Passenger>(_Passenger_QNAME, Passenger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:step:example:flight:wsdl", name = "createReservationResponse")
    public JAXBElement<CreateReservationResponse> createCreateReservationResponse(CreateReservationResponse value) {
        return new JAXBElement<CreateReservationResponse>(_CreateReservationResponse_QNAME, CreateReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:step:example:flight:wsdl", name = "serviceError")
    public JAXBElement<ServiceError> createServiceError(ServiceError value) {
        return new JAXBElement<ServiceError>(_ServiceError_QNAME, ServiceError.class, null, value);
    }

}
