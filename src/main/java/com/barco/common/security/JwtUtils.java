package com.barco.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * @author Nabeel Ahmed
 */
@Component
public class JwtUtils {

    private Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Method to convert Base64-encoded string to PrivateKey
    private static PrivateKey getPrivateKeyFromString(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // Method to convert Base64-encoded string to PublicKey
    private static PublicKey getPublicKeyFromString(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * Method use to generate the token from the username detail
     * @param username
     * @return String
     * */
    public static long oneYearInMs = 365 * 24 * 60 * 60 * 1000L;
    public String generateToken(String privateKey, String tokenId) throws Exception {
        return Jwts.builder().setSubject(tokenId).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + oneYearInMs))
            .signWith(SignatureAlgorithm.RS256, getPrivateKeyFromString(privateKey)).compact();
    }


    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + this.jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, this.jwtSecret).compact();
    }

    /**
     * Reset password link expire in 10 mint
     * @param payload
     * @return String
     * */
    public String generateTokenFromUsernameResetPassword(String payload) {
        return Jwts.builder().setSubject(payload).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + 600000))
            .signWith(SignatureAlgorithm.HS512, this.jwtSecret).compact();
    }

    /**
     * Method use to fetch the username from the jwt tokn
     * @param token
     * @return String
     * */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Method use to validate the jwt
     * @param authToken
     * @return boolean
     * */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        // Generate KeyPair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // You can adjust key size as needed
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // Get Public and Private Key objects
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        // Print Base64 encoded keys
        System.out.println("Public Key (Base64): " + java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("Private Key (Base64): " + java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }

}