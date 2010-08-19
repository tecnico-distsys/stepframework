package step.framework.ws;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *  SOAP message handling utility static methods.
 */
public class SOAPUtil{

    //
    //  SOAP 1.1 constants
    //

    public static final String SOAP_1_1_ENVELOPE_NS = "http://schemas.xmlsoap.org/soap/envelope/";

    public static final QName SOAP_1_1_FAULT_CODE_CLIENT_QNAME = new QName(SOAP_1_1_ENVELOPE_NS, "Client");
    public static final QName SOAP_1_1_FAULT_CODE_SERVER_QNAME = new QName(SOAP_1_1_ENVELOPE_NS, "Server");


    //
    //  SOAP 1.2 constants
    //
    public static final String SOAP_1_2_ENVELOPE_NS = "http://www.w3.org/2003/05/soap-envelope";

    public static final QName SOAP_1_2_FAULT_CODE_CLIENT_QNAME = new QName(SOAP_1_2_ENVELOPE_NS, "Sender");
    public static final QName SOAP_1_2_FAULT_CODE_SERVER_QNAME = new QName(SOAP_1_2_ENVELOPE_NS, "Receiver");


    //
    //  STEP message context properties
    //

    public static final String FORCED_BACK_TO_CLIENT_PROPERTY = "step.framework.ws.messageForcedBackToClient";



    //
    //  SOAP message context utilities
    //

    /**
     *  This method checks if the SOAP message in context
     *  is being processed on the server-side (true) or on the client-side (false).
     */
    public static boolean isServerSideMessage(SOAPMessageContext smc) {
        Object servletContextProperty = smc.get(MessageContext.SERVLET_CONTEXT);
        return (servletContextProperty != null);
    }

    /**
     *  This method checks if the SOAP message in context
     *  is going out of Web Service (outbound - true) or in (inbound - false).
     */
    public static boolean isOutboundMessage(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        return outboundProperty.booleanValue();
    }

    /**
     *  This method changes the SOAP message context outbound property.
     */
    public static void setOutboundMessage(SOAPMessageContext smc, boolean outboundPropertyValue) {
        smc.put(MessageContext.MESSAGE_OUTBOUND_PROPERTY, new Boolean(outboundPropertyValue));
    }

    /**
     *  This method checks if the SOAP message in context
     *  is being forced back to the client.
     *
     *  If the property does not exist,
     *  then message is also considered as not being forced back to client.
     */
    public static boolean isForcedBackToClient(SOAPMessageContext smc) {
        Boolean forcedProperty = (Boolean) smc.get(FORCED_BACK_TO_CLIENT_PROPERTY);
        if(forcedProperty == null)
            return false;
        else
            return forcedProperty.booleanValue();
    }

    /**
     *  This method changes the SOAP message context property
     *  for message being forced back to the client.
     */
    public static void setForcedBackToClient(SOAPMessageContext smc, boolean forcedPropertyValue) {
        smc.put(FORCED_BACK_TO_CLIENT_PROPERTY, new Boolean(forcedPropertyValue));
    }


    /**
     *  This method checks if the SOAP message in context
     *  contains a SOAP fault (true) or not (false).
     */
    public static boolean isFaultMessage(SOAPMessageContext smc) throws SOAPException {
        // check if message is a fault
        SOAPMessage msg = smc.getMessage();
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPBody sb = se.getBody();
        return sb.hasFault();
    }

    /**
     *  This method analyses the SOAP message context and suggests
     *  the most appropriate Qualified Name for a SOAP fault that would be
     *  generated in it.<br />
     *  <br />
     *  It is aware of SOAP versions 1.1 and 1.2.
     */
    public static QName suggestFaultCode(SOAPMessageContext smc)
    throws SOAPException {

        // obtain necessary message situation flags
        boolean isServerSide = isServerSideMessage(smc);

        // access SOAP envelope
        SOAPMessage soapMessage = smc.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

        // get SOAP envelope URI
        String soapEnvelopeURI = soapEnvelope.getNamespaceURI();

        // determine SOAP fault code
        QName code = null;
        if(isServerSide) {
            // server-side
            if(soapEnvelopeURI.equals(SOAP_1_2_ENVELOPE_NS)) {
                code = SOAP_1_2_FAULT_CODE_SERVER_QNAME;
            } else {
                code = SOAP_1_1_FAULT_CODE_SERVER_QNAME;
            }
        } else {
            // client-side
            if(soapEnvelopeURI.equals(SOAP_1_2_ENVELOPE_NS)) {
                code = SOAP_1_2_FAULT_CODE_CLIENT_QNAME;
            } else {
                code = SOAP_1_1_FAULT_CODE_CLIENT_QNAME;
            }
        }
        return code;
    }

    /**
     *  This method creates a new SOAP fault using the given fault message
     *  and replaces the SOAP message's current body with it
     */
    public static void replaceBodyWithNewFault(SOAPMessageContext smc, String faultMessage)
    throws SOAPException {
        // access SOAP message components
        SOAPMessage soapMessage = smc.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

        // reset body
        SOAPBody soapBody = soapEnvelope.getBody();
        if(soapBody == null) {
            soapEnvelope.addBody();
        } else {
            soapBody.removeContents();
        }

        // create fault
        SOAPFault soapFault = soapBody.addFault();

        // set fault code
        QName soapFaultCode = suggestFaultCode(smc);
        soapFault.setFaultCode(soapFaultCode);

        // set fault string
        soapFault.setFaultString(faultMessage);
    }

    /**
     *  This method replaces the SOAP message's current body with
     *  a new SOAP Fault with the same contents as the given fault,
     *  including details
     */
    public static void replaceBodyWithFault(SOAPMessageContext smc, SOAPFault soapFault)
    throws SOAPException {
        // access SOAP message components
        SOAPMessage soapMessage = smc.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

        // reset body
        SOAPBody soapBody = soapEnvelope.getBody();
        if(soapBody == null) {
            soapEnvelope.addBody();
        } else {
            soapBody.removeContents();
        }

        // create fault
        SOAPFault newSOAPFault = soapBody.addFault();

        // copy fault code
        QName faultCode = soapFault.getFaultCodeAsQName();
        if(faultCode != null) {
            newSOAPFault.setFaultCode(faultCode);
        }

        // copy fault string
        String faultString = soapFault.getFaultString();
        if(faultString != null) {
            newSOAPFault.setFaultString(faultString);
        }

        // copy fault detail
        Detail faultDetail = soapFault.getDetail();
        if(faultDetail != null) {
            Detail newDetail = newSOAPFault.addDetail();
            org.w3c.dom.Document newDoc = newDetail.getOwnerDocument();

            // import all detail entries (copy)
            Iterator<?> detailEntriesIterator = faultDetail.getDetailEntries();
            while(detailEntriesIterator.hasNext()) {
                DetailEntry detailEntry = (DetailEntry) detailEntriesIterator.next();
                org.w3c.dom.Node newNode = newDoc.importNode(detailEntry, true /*deep*/);
                newDetail.appendChild(newNode);
            }
        }

        // copy fault actor
        String faultActor = soapFault.getFaultActor();
        if(faultActor != null) {
            newSOAPFault.setFaultActor(faultActor);
        }

    }


    /**
     *  This method returns the SOAP body contained inside the SOAP message
     *  or returns null if there is none.
     */
    public static SOAPBody getBody(SOAPMessage soapMessage) throws SOAPException {
        // access SOAP body
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        return soapEnvelope.getBody();
    }


    /**
     *  This method returns the first child element of the SOAP body tag
     *  or null if there is none or the body contains a Fault.
     */
    public static SOAPElement getBodyPayload(SOAPMessage soapMessage) throws SOAPException {
        SOAPBody soapBody = getBody(soapMessage);
        SOAPElement soapElement = (SOAPElement) XMLUtil.getFirstChildElement(soapBody);
        if(soapElement != null && "Fault".equals(soapElement.getElementQName().getLocalPart()))
            return null;
        return soapElement;
    }


    /**
     *  This method returns the SOAP fault contained inside the SOAP message
     *  or returns null if there is none.
     */
    public static SOAPFault getFault(SOAPMessage soapMessage) throws SOAPException {
        // return SOAP fault or null if it does not exist
        return getBody(soapMessage).getFault();
    }

    /**
     *  This method returns the first child element of the SOAP fault detail tag
     *  or returns null if there is none.
     */
    public static SOAPElement getFaultPayload(SOAPMessage soapMessage) throws SOAPException {
        SOAPFault soapFault = getFault(soapMessage);
        if(soapFault != null) {
            Detail detail = soapFault.getDetail();
            if(detail != null) {
                SOAPElement soapElement = (SOAPElement) XMLUtil.getFirstChildElement(detail);
                return soapElement;
            }
        }
        return null;
    }

}
