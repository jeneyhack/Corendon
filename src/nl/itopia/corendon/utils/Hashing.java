package nl.itopia.corendon.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 *
 * @author wieskueter.com
 */
public class Hashing {

    public static String generateSaltString() {
        byte[] salt = generateSalt();
        return Base64.getEncoder().encodeToString(salt);
    }

    public static byte[] generateSalt(){

        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);

        return salt;
    }

    public static String sha256(String value) {
        
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
           throw new RuntimeException(ex);
        }
    }
}
