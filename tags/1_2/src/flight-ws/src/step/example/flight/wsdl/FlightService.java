
package step.example.flight.wsdl;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import step.example.flight.wsdl.FlightPortType;
import step.example.flight.wsdl.FlightService;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0-b26-ea3
 * Generated source version: 2.0
 *
 */
@WebServiceClient(name = "FlightService", targetNamespace = "urn:step:example:flight:wsdl", wsdlLocation = "file:///C:/dev/java/stepframework/src/flight-ws/WebContent/WEB-INF/wsdl/flight.wsdl")
public class FlightService
    extends Service
{

    private final static URL WSDL_LOCATION;
    private final static QName FLIGHTSERVICE = new QName("urn:step:example:flight:wsdl", "FlightService");
    private final static QName FLIGHTPORT = new QName("urn:step:example:flight:wsdl", "FlightPort");

    static {
        URL url = null;
        try {
            url = new URL("file:/C:/dev/java/stepframework/src/flight-ws/WebContent/WEB-INF/wsdl/flight.wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public FlightService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FlightService() {
        super(WSDL_LOCATION, FLIGHTSERVICE);
    }

    /**
     *
     * @return
     *     returns FlightPortType
     */
    @WebEndpoint(name = "FlightPort")
    public FlightPortType getFlightPort() {
        return (FlightPortType)super.getPort(FLIGHTPORT, FlightPortType.class);
    }

}