package step.extension.cipher;

import java.io.*;
import java.security.*;
import java.util.*;

import javax.crypto.*;
import javax.xml.soap.*;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.codec.binary.Base64;

import step.framework.extensions.*;

/**
 *  Web Service interceptor that ciphers outbound messages
 *  and deciphers inbound messages.
 */
public class CipherWebServiceInterceptor extends WebServiceInterceptorBase {

    /*
     *  Interception
     */

    @Override
	public boolean interceptMessage(WebServiceInterceptorParameter param)
        throws SOAPFaultException, WebServiceInterceptorException {

        boolean isOutbound = param.isOutboundSOAPMessage();
		try {
			if(isOutbound)
				cipherMessage(param.getSOAPMessageContext());
			else
				decipherMessage(param.getSOAPMessageContext());
			return true;

		} catch(Exception e) {
			throw new WebServiceInterceptorException(
                "Extension failed to " + (isOutbound ? "cipher" : "decipher") + " message", e);
		}
	}

    
    /*
     *  Security implementation
     */

     /** Cipher SOAP message in context */
     private void cipherMessage(SOAPMessageContext smc) throws Exception {

        SOAPMessage msg = smc.getMessage();
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPBody sb = se.getBody();
        SOAPHeader sh = se.getHeader();
        if (sh == null) {
            sh = se.addHeader();
        }

        // cipher message with symmetric key
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        msg.writeTo(byteOut);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, KeyManager.getSecretKey());
        byte[] cipheredMessage = cipher.doFinal(byteOut.toByteArray());

        // encode in base64
        Base64 b64 = new Base64();
        String encodedMessage = b64.encodeToString(cipheredMessage);

        // remove clear text
        sb.detachNode();
        sh.detachNode();

        // reinitialize SOAP components
        sb = se.addBody();
        sh = se.addHeader();

        // store message
        SOAPBodyElement element = sb.addBodyElement(se.createName("CipherBody"));
        element.addTextNode(encodedMessage);
    }

    /** Decipher SOAP message in context */
    private void decipherMessage(SOAPMessageContext smc) throws Exception {

        SOAPMessage msg = smc.getMessage();
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPBody sb = se.getBody();
        SOAPHeader sh = se.getHeader();

        // fetch message
        Iterator it = sb.getChildElements(se.createName("CipherBody"));
        String encodedMessage = ((SOAPElement) it.next()).getValue();

        // decode from base64
        Base64 b64 = new Base64();
        byte[] decodedMessage = b64.decode(encodedMessage);

        // decipher message
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, KeyManager.getSecretKey());
        byte[] decipheredMessage = cipher.doFinal(decodedMessage);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(decipheredMessage);

        // restore deciphered envelope
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(msg.getMimeHeaders(), byteIn);
        sp.setContent(message.getSOAPPart().getContent());
    }

}