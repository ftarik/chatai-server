package fr.fgroup.chatai.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
public class CryptUtil {

  private CryptUtil() {
  }

  private static final String ALGO = "AES"; // Default uses ECB PKCS5Padding

  public static String decrypt(String strToDecrypt, String secret) {
    try {
      Key key = generateKey(secret);
      Cipher cipher = Cipher.getInstance(ALGO);
      cipher.init(Cipher.DECRYPT_MODE, key);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Exception e) {
      log.error("Error while decrypting: ", e);
    }
    return null;
  }

  private static Key generateKey(String secret) {
    byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
    return new SecretKeySpec(decoded, ALGO);
  }

  public static String encodeKey(String str) {
    byte[] encoded = Base64.getEncoder().encode(str.getBytes());
    return new String(encoded);
  }
}
