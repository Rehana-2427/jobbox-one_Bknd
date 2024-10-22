package com.jobbox.Project_Jobbox.jwttoken;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecretKeyGeneratorExample {

	public static String generateSecretKey() {
        try {
            // Create a KeyGenerator for the AES algorithm
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            
            // Initialize the KeyGenerator with a key size (128, 192, or 256 bits)
            keyGenerator.init(256);  // 256-bit key size for strong encryption
            
            // Generate the secret key
            SecretKey secretKey = keyGenerator.generateKey();
            
            // Encode the secret key to a Base64 string and return
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;  // Return null if key generation fails
    }
}
