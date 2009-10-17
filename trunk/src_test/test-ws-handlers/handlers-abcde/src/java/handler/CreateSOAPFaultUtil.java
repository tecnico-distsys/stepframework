package handler;


import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;


public class CreateSOAPFaultUtil {


    public static SOAPFault createFault(String message) {
        try {
            // create SOAP message
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage soapMessage = mf.createMessage();

            // access SOAP body
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();

            // create SOAP Fault
            SOAPFault soapFault = soapBody.addFault();
            soapFault.setFaultString(message);

            return soapFault;

        } catch(Exception e) {
            System.out.println(e);
            return null;
        }

    }


}