
package step.example.flight.view;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import step.example.flight.view.ObjectFactory;
import step.example.flight.view.ReservationVoucher;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the step.example.flight.view package. 
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

    private final static QName _ReservationVoucher_QNAME = new QName("urn:step:example:flight:view", "reservationVoucher");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: step.example.flight.view
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReservationVoucher }
     * 
     */
    public ReservationVoucher createReservationVoucher() {
        return new ReservationVoucher();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReservationVoucher }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:step:example:flight:view", name = "reservationVoucher")
    public JAXBElement<ReservationVoucher> createReservationVoucher(ReservationVoucher value) {
        return new JAXBElement<ReservationVoucher>(_ReservationVoucher_QNAME, ReservationVoucher.class, null, value);
    }

}
