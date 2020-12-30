package org.mcnative.licensing;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/**
 * The Token Generator is a simple standalone tool for generating an rsa pair for the McNative licensing service
 */
public class TokenGenerator {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        System.out.println("-----BEGIN PRIVATE KEY-----");
        System.out.println(Base64.getMimeEncoder().encodeToString(kp.getPrivate().getEncoded()));
        System.out.println("-----END PRIVATE KEY-----");
        System.out.println("-----BEGIN PUBLIC KEY-----");
        System.out.println(Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
        System.out.println("-----END PUBLIC KEY-----");
    }
}
