package handler;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

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

import org.w3c.dom.Node;

public class XMLUtil {

    public static final SOAPFault createFault(String message) {
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
    
    public static final void prettyPrint(Node xml, OutputStream out)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        //tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(2));
        tf.transform(new DOMSource(xml), new StreamResult(out));
    }


    public static final void prettyPrint(String xml, OutputStream out)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new StreamSource(new StringReader(xml)), new StreamResult(out));
    }

}
