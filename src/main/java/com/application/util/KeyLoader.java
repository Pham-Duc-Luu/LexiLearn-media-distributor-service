package com.application.util;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyLoader {


    public static RSAPublicKey loadPublicKey(String publicKeyPath) throws Exception {
        ClassPathResource resource = new ClassPathResource(publicKeyPath);
        try (InputStream inputStream = resource.getInputStream()) {
            String publicKeyPEM = new String(inputStream.readAllBytes())
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
        }
    }
}
