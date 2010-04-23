package step.extension.cipher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Node;

import step.framework.extensions.WebServiceInterceptor;
import step.framework.extensions.WebServiceInterceptorException;
import step.framework.extensions.WebServiceInterceptorParameter;

public class CipherWebServiceInterceptor implements WebServiceInterceptor {
	
	private static final String KEY = "IA4+cwImMl4=";

	public boolean interceptMessage(WebServiceInterceptorParameter param) throws SOAPFaultException, WebServiceInterceptorException
	{
		try
		{
			boolean isOutbound = param.isOutboundSOAPMessage();
			
			if(isOutbound)
				encrypt(param.getSOAPMessageContext());
			else
				decrypt(param.getSOAPMessageContext());
			
			return true;
		}
		catch(RuntimeException e)
		{
			throw new WebServiceInterceptorException(e.toString());
		}
		catch(Exception e)
		{
			throw new WebServiceInterceptorException(e.toString());
		}
	}
	
	private void encrypt(SOAPMessageContext smc) throws Exception
	{
		SOAPMessage soapMessage = smc.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPBody soapBody = soapEnvelope.getBody();

        DESKeySpec keySpec = new DESKeySpec(KEY.getBytes());
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
        DesEncrypter des = new DesEncrypter(key);
        String enc = des.encrypt(soapMessage);
        
        soapEnvelope.removeChild(soapBody);
        soapEnvelope.addBody().addChildElement("message").setTextContent(enc);
	}
	
	private void decrypt(SOAPMessageContext smc) throws Exception
	{
		SOAPMessage soapMessage = smc.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPBody encSoapBody = soapEnvelope.getBody();

        DESKeySpec keySpec = new DESKeySpec(KEY.getBytes());
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
        DesEncrypter des = new DesEncrypter(key);
        Node message = encSoapBody.getElementsByTagName("message").item(0);
        if(message == null)
        	return;
        SOAPMessage dec = des.decrypt(message.getTextContent());
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        dec.writeTo(bos);
        
        soapPart.setContent(new StreamSource(new ByteArrayInputStream(bos.toByteArray())));
	}
}