package ru.gnidenko.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class KeyConfig {

    @Value("${key.public}")
    private String publicKeyPath;

    @Bean
    public PublicKey parsePublicKey() {
        String keyContent = readKeyFromFile(publicKeyPath);
        keyContent = keyContent.replace("-----BEGIN PUBLIC KEY-----", "");
        keyContent = keyContent.replace("-----END PUBLIC KEY-----", "");
        keyContent = keyContent.replaceAll("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(keyContent);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        try {
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private String readKeyFromFile(String pathFile){
        ClassPathResource resource = new ClassPathResource(pathFile);
        try (InputStream inputStream = resource.getInputStream()) {

            return new String(inputStream.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
