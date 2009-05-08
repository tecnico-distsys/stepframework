
package step.example.flight.wsdl;

import java.io.Serializable;
import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import step.example.flight.wsdl.CreateReservation;
import step.example.flight.wsdl.Passenger;


/**
 * <p>Java class for CreateReservation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateReservation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="departure" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="arrival" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="passenger" type="{urn:step:example:flight:wsdl}Passenger"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "CreateReservation", propOrder = {
    "departure",
    "arrival",
    "passenger"
})
public class CreateReservation
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "urn:step:example:flight:wsdl")
    protected String departure;
    @XmlElement(namespace = "urn:step:example:flight:wsdl")
    protected String arrival;
    @XmlElement(namespace = "urn:step:example:flight:wsdl")
    protected Passenger passenger;

    /**
     * Gets the value of the departure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeparture() {
        return departure;
    }

    /**
     * Sets the value of the departure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeparture(String value) {
        this.departure = value;
    }

    /**
     * Gets the value of the arrival property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrival() {
        return arrival;
    }

    /**
     * Sets the value of the arrival property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrival(String value) {
        this.arrival = value;
    }

    /**
     * Gets the value of the passenger property.
     * 
     * @return
     *     possible object is
     *     {@link Passenger }
     *     
     */
    public Passenger getPassenger() {
        return passenger;
    }

    /**
     * Sets the value of the passenger property.
     * 
     * @param value
     *     allowed object is
     *     {@link Passenger }
     *     
     */
    public void setPassenger(Passenger value) {
        this.passenger = value;
    }

}
