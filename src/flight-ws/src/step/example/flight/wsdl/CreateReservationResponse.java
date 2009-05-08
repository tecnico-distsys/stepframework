
package step.example.flight.wsdl;

import java.io.Serializable;
import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import step.example.flight.view.ReservationVoucher;
import step.example.flight.wsdl.CreateReservationResponse;


/**
 * <p>Java class for CreateReservationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateReservationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="voucher" type="{urn:step:example:flight:view}ReservationVoucher"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "CreateReservationResponse", propOrder = {
    "voucher"
})
public class CreateReservationResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "urn:step:example:flight:wsdl")
    protected ReservationVoucher voucher;

    /**
     * Gets the value of the voucher property.
     * 
     * @return
     *     possible object is
     *     {@link ReservationVoucher }
     *     
     */
    public ReservationVoucher getVoucher() {
        return voucher;
    }

    /**
     * Sets the value of the voucher property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReservationVoucher }
     *     
     */
    public void setVoucher(ReservationVoucher value) {
        this.voucher = value;
    }

}
