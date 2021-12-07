package me.darkboy.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class PasswordUtils {

   private static final Random RANDOM = new SecureRandom();

   public static String getSalt(int length) {
      StringBuilder returnValue = new StringBuilder(length);

      for (int i = 0; i < length; ++i) {
         returnValue.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(RANDOM.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
      }

      return new String(returnValue);
   }

   public static byte[] hash(char[] password, byte[] salt) {
      PBEKeySpec spec = new PBEKeySpec(password, salt, 10000, 256);
      Arrays.fill(password, '\u0000');

      byte[] secret;
      try {
         SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
         secret = skf.generateSecret(spec).getEncoded();
      } catch (InvalidKeySpecException | NoSuchAlgorithmException var8) {
         throw new AssertionError("Error while hashing a password: " + var8.getMessage(), var8);
      } finally {
         spec.clearPassword();
      }

      return secret;
   }

   public static String generateSecurePassword(String password, String salt) {
      byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
      return Base64.getEncoder().encodeToString(securePassword);
   }
}