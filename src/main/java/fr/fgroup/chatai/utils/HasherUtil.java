package fr.fgroup.chatai.utils;


import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

/**
 * HasherUtil - Utility class for cryptographic hashing operations.
 * 
 * This utility provides methods for:
 * - Generating SHA-256 hashes for user identification
 * - Converting byte arrays to hex strings
 * 
 * Used primarily for generating unique, non-reversible API keys from user IDs.
 * The hashing includes a timestamp to ensure uniqueness across multiple calls.
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2023-03-05
 */
@Slf4j
public class HasherUtil {

  /**
   * Private constructor to prevent instantiation of utility class.
   */
  private HasherUtil() {
  }

  /**
   * Generates a SHA-256 hash from the given input string.
   * 
   * @param input the string to hash
   * @return byte array containing the SHA-256 hash
   * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
   */
  private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    return md.digest(input.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Converts a byte array to its hexadecimal string representation.
   * 
   * Pads the result with leading zeros to ensure a fixed 64-character length
   * (standard for SHA-256 hashes).
   * 
   * @param hash the byte array to convert
   * @return hexadecimal string representation
   */
  private static String toHexString(byte[] hash) {
    BigInteger number = new BigInteger(1, hash);
    StringBuilder hexString = new StringBuilder(number.toString(16));

    // Pad with leading zeros to ensure 64-character length
    while (hexString.length() < 64) {
      hexString.insert(0, '0');
    }

    return hexString.toString();
  }

  /**
   * Generates a unique SHA-256 hashed key for a user.
   * 
   * The key is generated from:
   * - The user's ID
   * - A static salt ("ChatAI")
   * - Current timestamp (ensures uniqueness)
   * 
   * This creates a non-reversible, unique key suitable for API authentication.
   * 
   * @param id the user's ID
   * @return SHA-256 hashed key as a hexadecimal string
   * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
   */
  public static String getHashedKey(Long id) throws NoSuchAlgorithmException {
    log.debug("Generate SHA-256 code for id : {}", id);
    String s = id + "_ChatAI_" + new Timestamp(System.currentTimeMillis());
    return toHexString(getSHA(s));
  }
}
