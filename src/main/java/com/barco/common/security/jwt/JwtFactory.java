package com.barco.common.security.jwt;

import com.barco.common.utility.BarcoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtFactory: helper to generate and validate JWTs using RSA keys.
 * Supports private keys stored either as raw Base64 PKCS#8 bytes or as an AES-encrypted blob
 * (IV + ciphertext, Base64-encoded). AES key is derived via SHA-256(aesSecret).
 * @author Nabeel Ahmed
 */
@Component
public class JwtFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFactory.class);

    // Inject JwtKeyGenerator to delegate decryption/parsing of private keys
    private final JwtKeyGenerator keyGenerator;

    // Constructor injection (Spring will wire the JwtKeyGenerator component)
    public JwtFactory(JwtKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    /**
     * Load a PrivateKey from either:
     * - a Base64-encoded PKCS#8 key
     * - a Base64-encoded AES-encrypted blob (IV + ciphertext) which will be decrypted
     *   using AES key derived as SHA-256(aesSecret).
     * If aesSecret is null/empty, the method will treat keyBlob as raw Base64 key.
     */
    public PrivateKey loadPrivateKey(String privateKeyBase64, String aesSecret) throws Exception {
        if (BarcoUtil.isNull(privateKeyBase64)) {
            throw new IllegalArgumentException("keyBlobBase64 is null");
        }
        return this.keyGenerator.decryptToPrivateKey(privateKeyBase64, aesSecret);
    }

    /**
     * Load a PublicKey from Base64-encoded X.509 bytes
     * @param publicKeyBase64 Base64-encoded X.509 bytes of the public key
     * @return PublicKey object
     */
    public PublicKey loadPublicKey(String publicKeyBase64) throws Exception {
        if (BarcoUtil.isNull(publicKeyBase64)) {
            throw new IllegalArgumentException("publicKeyBase64 is null");
        }
        byte[] decoded = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * Load a PublicKey directly from raw X.509 encoded bytes
     * @param publicKeyBytes raw X.509 encoded public key bytes
     * @return PublicKey object
     */
    public PublicKey loadPublicKey(byte[] publicKeyBytes) throws Exception {
        if (BarcoUtil.isNull(publicKeyBytes)) {
            throw new IllegalArgumentException("publicKeyBytes is null");
        }
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * Load a PrivateKey from either a Base64 string or raw bytes. This overload accepts raw bytes
     * which may be either raw PKCS#8 DER or IV||ciphertext bytes (when encrypted). It Base64-encodes
     * the input and delegates to the existing String-based method.
     */
    public PrivateKey loadPrivateKey(byte[] privateKeyBytes, String aesSecret) throws Exception {
        if (BarcoUtil.isNull(privateKeyBytes)) {
            throw new IllegalArgumentException("privateKeyBytes is null");
        }
        String base64 = Base64.getEncoder().encodeToString(privateKeyBytes);
        return loadPrivateKey(base64, aesSecret);
    }

    /**
     * Generate a JWT signed with RSA private key. Expiration is relative ms from now.
     * @param privateKeyBase64 private key stored in DB (Base64 PKCS#8 or AES-encrypted blob)
     * @param keyId key ID to set in JWT header (used for lookup during verification)
     * @param aesSecret optional AES secret (if key blob is encrypted)
     * @param subject subject to set in token (e.g., username or token id)
     * @param expiresInMs milliseconds from now when token expires
     */
    public String generateToken(String privateKeyBase64,
        String aesSecret,
        String keyId,
        String subject,
        long expiresInMs) throws Exception {
        if (BarcoUtil.isNull(subject)) {
            throw new IllegalArgumentException("subject is null");
        }
        PrivateKey privateKey = this.loadPrivateKey(privateKeyBase64, aesSecret);
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiresInMs);
        return Jwts.builder()
           .setHeaderParam("typ", "JWT")
           .setHeaderParam("alg", "RS256")
           .setHeaderParam("kid", keyId)
           .setSubject(subject)
           .setIssuedAt(now)
           .setExpiration(exp)
           .signWith(SignatureAlgorithm.RS256, privateKey)
           .compact();
    }

    /**
     * Generate a JWT signed with RSA private key from a raw byte[] private key blob.
     * The privateKeyBytes can be either raw PKCS#8 DER bytes or IV||ciphertext (the same byte[] produced by JwtKeyGenerator.getPrivateKeyEncryptedBytes()).
     */
    public String generateToken(byte[] privateKeyBytes,
        String aesSecret,
        String keyId,
        String subject,
        long expiresInMs) throws Exception {
        String base64 = Base64.getEncoder().encodeToString(privateKeyBytes);
        return this.generateToken(base64, aesSecret, keyId, subject, expiresInMs);
    }

    /**
     * Generate a refresh token. Caller can provide a custom expiration in milliseconds.
     * Backward-compatible convenience method without expires uses one year.
     * @param privateKeyBase64 private key stored in DB (Base64 PKCS#8 or AES-encrypted blob)
     * @param aesSecret optional AES secret (if key blob is encrypted)
     * @param keyId key ID to set in JWT header (used for lookup during verification)
     * @param subject subject to set in token (e.g., username or token id)
     * @param expiresInMs milliseconds from now when token expires
     */
    public String generateRefreshToken(String privateKeyBase64,
        String aesSecret,
        String keyId,
        String subject,
        long expiresInMs) throws Exception {
        return this.generateToken(privateKeyBase64, aesSecret, keyId, subject, expiresInMs);
    }

    /**
     * Overload of generateRefreshToken that accepts raw byte[] private key blob.
     * The privateKeyBytes can be either raw PKCS#8 DER bytes or IV||ciphertext (the same byte[] produced by JwtKeyGenerator.getPrivateKeyEncryptedBytes()).
     * @param privateKeyBytes raw byte[] of the private key blob (either raw PKCS#8 DER or AES-encrypted IV||ciphertext)
     * @param aesSecret optional AES secret (if key blob is encrypted)
     * @param keyId key ID to set in JWT header (used for lookup during verification)
     * @param subject subject to set in token (e.g., username or token id)
     * @param expiresInMs milliseconds from now when token expires
     */
    public String generateRefreshToken(byte[] privateKeyBytes,
        String aesSecret,
        String keyId,
        String subject,
        long expiresInMs) throws Exception {
        String base64 = Base64.getEncoder().encodeToString(privateKeyBytes);
        return this.generateRefreshToken(base64, aesSecret, keyId, subject, expiresInMs);
    }

    /**
     * Verify an RSA-signed JWT using the provided Base64-encoded X.509 public key.
     * This method validates signature and expiration. Returns true if token is valid.
     * @param token JWT token string
     * @param publicKeyBase64 Base64-encoded X.509 public key
     */
    public boolean verifyToken(String token, String publicKeyBase64) {
        try {
            if (BarcoUtil.isNull(token) || BarcoUtil.isNull(publicKeyBase64)) {
                LOGGER.warn("verifyToken called with null token or publicKey");
                return false;
            }
            PublicKey publicKey = this.loadPublicKey(publicKeyBase64);
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.warn("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            LOGGER.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.error("Error while verifying JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Overload of verifyToken that accepts raw byte[] public key.
     * This is useful when the caller already has the public key bytes (e.g., from a database) and wants to avoid the overhead of Base64 encoding/decoding.
     * The publicKeyBytes should be the raw X.509 encoded bytes of the public key.
     * @param token JWT token string
     * @param publicKeyBytes raw X.509 encoded public key bytes
     * @return true if token is valid (signature and expiration), false otherwise
     */
    public boolean verifyToken(String token, byte[] publicKeyBytes) {
        try {
            if (BarcoUtil.isNull(token) ||  BarcoUtil.isNull(publicKeyBytes)) {
                LOGGER.warn("verifyToken called with null token or publicKeyBytes");
                return false;
            }
            PublicKey publicKey = this.loadPublicKey(publicKeyBytes);
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.warn("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            LOGGER.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.error("Error while verifying JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verify token signature using the given public key and return the subject (sub) claim.
     * Throws Exception when public key cannot be loaded or token parsing fails.
     * @param token JWT token string
     * @param publicKeyBase64 Base64-encoded X.509 public key
     * @return subject (sub) claim or null if not present
     * @throws Exception on key parsing or unexpected errors
     */
    public String getSubjectWithPublicKey(String token, String publicKeyBase64) throws Exception {
        if (BarcoUtil.isNull(token) || BarcoUtil.isNull(publicKeyBase64)) {
            throw new IllegalArgumentException("token or publicKeyBase64 is null");
        }
        PublicKey publicKey = this.loadPublicKey(publicKeyBase64);
        Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * Overload of getSubjectWithPublicKey that accepts raw byte[] public key.
     * This is useful when the caller already has the public key bytes (e.g., from a database) and wants to avoid the overhead of Base64 encoding/decoding.
     * The publicKeyBytes should be the raw X.509 encoded bytes of the public key.
     * @param token JWT token string
     * @param publicKeyBytes raw X.509 encoded public key bytes
     * @return subject (sub) claim or null if not present
     * @throws Exception on key parsing or unexpected errors
     */
    public String getSubjectWithPublicKey(String token, byte[] publicKeyBytes) throws Exception {
        if (BarcoUtil.isNull(token) || BarcoUtil.isNull(publicKeyBytes)) {
            throw new IllegalArgumentException("token or publicKeyBytes is null");
        }
        PublicKey publicKey = this.loadPublicKey(publicKeyBytes);
        Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * Parse a subject JSON string into a JwtSubject object.
     * Returns null when subject is null or parsing fails.
     */
    public JwtSubject parseSubject(String subject) {
        if (BarcoUtil.isNull(subject)) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(subject, JwtSubject.class);
        } catch (Exception ex) {
            LOGGER.warn("Failed to parse JWT subject JSON: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Extract the JWT header as a Map (without validating signature). Returns a Map of header entries.
     * @param jwt the JWT token string
     * @return Map of header entries. If the header cannot be parsed as JSON, returns a Map with a single entry "raw" containing the header string.
     * Note: this method does not validate the token signature or expiration. It only decodes the header part.
     */
    public Map<String, Object> extractHeader(String jwt) {
        if (BarcoUtil.isNull(jwt)) {
            throw new IllegalArgumentException("JWT is null");
        }
        String[] parts = jwt.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT format: expected at least 2 parts separated by dots");
        }
        String headerB64 = parts[0];
        byte[] headerBytes = Base64.getUrlDecoder().decode(headerB64);
        String json = new String(headerBytes, StandardCharsets.UTF_8);
        try {
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> map = mapper.readValue(json, Map.class);
            return !BarcoUtil.isNull(map) ? map : new HashMap<>();
        } catch (Exception ex) {
            LOGGER.warn("Failed to parse JWT header JSON, returning raw string: {}", ex.getMessage());
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("raw", json);
            return fallback;
        }
    }

}
