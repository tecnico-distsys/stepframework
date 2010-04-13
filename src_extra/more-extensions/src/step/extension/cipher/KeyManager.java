package step.extension.cipher;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * This class is responsible for managing the keys.
 * In this simplistic implementation it just holds a fixed secret key.
 */
class KeyManager {

	// key data array
	private static byte[] encodedDESKey = { (byte) 0x13, (byte) 0x22, (byte) 0x02, (byte) 0x06, 
                                            (byte) 0xF1, (byte) 0xA8, (byte) 0x07, (byte) 0xF1 };

	public static SecretKey getSecretKey() throws Exception {
        DESKeySpec keySpec = new DESKeySpec(encodedDESKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(keySpec);
	}

}
