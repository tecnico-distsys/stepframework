package step.extension.cipher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import sun.misc.BASE64Encoder;

public class DesEncrypter {
	
    private Cipher ecipher;
    private Cipher dcipher;

    DesEncrypter(SecretKey key)
    {
        try
        {
            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
    }

    public String encrypt(SOAPMessage soapMessage)
    {
        try
        {
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	soapMessage.writeTo(bos);
        	
        	byte[] bytes = bos.toByteArray();

            // Encrypt
            byte[] enc = ecipher.doFinal(bytes);

            // Encode bytes to base64 to get a string
            return new BASE64Encoder().encode(enc);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	return null;
        }
    }

    public SOAPMessage decrypt(String str)
    {
        try
        {
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            String string = new String(utf8, "UTF8");
            
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage soapMessage = mf.createMessage(null, new ByteArrayInputStream(string.getBytes()));
            
            return soapMessage;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	return null;
        }
    }
}
