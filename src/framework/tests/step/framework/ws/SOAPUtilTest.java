package step.framework.ws;

import java.io.*;
import java.util.*;

import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.junit.*;
import static org.junit.Assert.*;


public class SOAPUtilTest {

    static final private String BR = System.getProperty("line.separator");
    private PrintStream out = System.out;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private static SOAPMessage byteArrayToSOAPMessage(byte[] msg) throws Exception {
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(msg);
        StreamSource source = new StreamSource(byteInStream);
        SOAPMessage newMsg = null;

        MessageFactory mf = MessageFactory.newInstance();
        newMsg = mf.createMessage();
        SOAPPart soapPart = newMsg.getSOAPPart();
        soapPart.setContent(source);

        return newMsg;
    }

    private static final String SOAP_OK = 
        "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" + BR + 
        "  <S:Header/>" + BR + 
        "  <S:Body>" + BR + 
        "    <ns2:searchFlights xmlns=\"urn:org:tripplanner:flight:view\" " + BR + 
        "xmlns:ns2=\"urn:org:tripplanner:flight:wsdl\">" + BR + 
        "      <ns2:input>" + BR + 
        "        <depart>Lisbon</depart>" + BR + 
        "        <arrive>New York</arrive>" + BR + 
        "      </ns2:input>" + BR + 
        "    </ns2:searchFlights>" + BR + 
        "  </S:Body>" + BR + 
        "</S:Envelope>";

    @Test
    public void testGetBody() throws Exception {
        SOAPMessage soapMessage = byteArrayToSOAPMessage(SOAP_OK.getBytes());
        SOAPBody soapBody = SOAPUtil.getBody(soapMessage);

        assertNotNull(soapBody);
        assertEquals("Body", soapBody.getElementQName().getLocalPart());
    }
    
    @Test
    public void testGetFaultBodyPayload() throws Exception {
        SOAPMessage soapMessage = byteArrayToSOAPMessage(SOAP_FAULT.getBytes());
        SOAPElement soapElement = SOAPUtil.getBodyPayload(soapMessage);

        // even tough Fault is there, it is not considered a body payload
        assertNull(soapElement);
    }

    @Test
    public void testGetBodyPayload() throws Exception {
        SOAPMessage soapMessage = byteArrayToSOAPMessage(SOAP_OK.getBytes());
        SOAPElement soapElement = SOAPUtil.getBodyPayload(soapMessage);

        assertNotNull(soapElement);
        assertEquals("searchFlights", soapElement.getElementQName().getLocalPart());
    }


    private static final String SOAP_FAULT = 
        "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" + BR + 
        "  <S:Body>" + BR + 
        "    <ns2:Fault xmlns:ns2=\"http://schemas.xmlsoap.org/soap/envelope/\" " + BR + 
        "xmlns:ns3=\"http://www.w3.org/2003/05/soap-envelope\">" + BR + 
        "      <faultcode>ns2:Server</faultcode>" + BR + 
        "      <faultstring>Flight number 123 not found!</faultstring>" + BR + 
        "      <detail>" + BR + 
        "        <ns2:flightFault xmlns=\"urn:org:tripplanner:flight:view\" " + BR + 
        "xmlns:ns2=\"urn:org:tripplanner:flight:wsdl\">" + BR + 
        "          <ns2:faultType>org.tripplanner.flight.exception.NoFlightAvailableForReservationException</ns2:faultType>" + BR + 
        "        </ns2:flightFault>" + BR + 
        "      </detail>" + BR + 
        "    </ns2:Fault>" + BR + 
        "  </S:Body>" + BR + 
        "</S:Envelope>" + BR;

    @Test
    public void testGetFault() throws Exception {
        SOAPMessage soapMessage = byteArrayToSOAPMessage(SOAP_FAULT.getBytes());
        SOAPFault soapFault = SOAPUtil.getFault(soapMessage);

        assertNotNull(soapFault);
        assertEquals("Fault", soapFault.getElementQName().getLocalPart());
    }

    @Test
    public void testGetFaultPayload() throws Exception {
        SOAPMessage soapMessage = byteArrayToSOAPMessage(SOAP_FAULT.getBytes());
        SOAPElement soapElement = SOAPUtil.getFaultPayload(soapMessage);

        assertNotNull(soapElement);
        assertEquals("flightFault", soapElement.getElementQName().getLocalPart());
    }

    @Test
    public void testGetNonExistentFaultPayload() throws Exception {
        SOAPMessage soapMessage = byteArrayToSOAPMessage(SOAP_OK.getBytes());
        SOAPElement soapElement = SOAPUtil.getFaultPayload(soapMessage);

        assertNull(soapElement);
    }


}
