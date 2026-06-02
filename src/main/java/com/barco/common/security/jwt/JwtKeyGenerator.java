package com.barco.common.security.jwt;

import com.barco.common.utility.BarcoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Utility to generate RSA key pairs and produce an AES-encrypted private key blob
 * compatible with JwtFactory.decryptPrivateKeyEncrypted.
 * @author Nabeel Ahmed
 */
@Component
public class JwtKeyGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtKeyGenerator.class);

    public static class JwtKeyPair {

        private final String keyId;
        private final byte[] publicKeyBytes;              // raw X.509 encoded public key bytes
        private final byte[] privateKeyEncryptedBytes;   // either raw PKCS#8 DER bytes or IV||ciphertext bytes

        public JwtKeyPair(String keyId, byte[] publicKeyBytes, byte[] privateKeyEncryptedBytes) {
            this.keyId = keyId;
            this.publicKeyBytes = publicKeyBytes;
            this.privateKeyEncryptedBytes = privateKeyEncryptedBytes;
        }

        public String getKeyId() {
            return keyId;
        }

        // New byte-array accessors (what you asked for)
        public byte[] getPublicKeyBytes() {
            return publicKeyBytes;
        }

        public byte[] getPrivateKeyEncryptedBytes() {
            return privateKeyEncryptedBytes;
        }

        // Backwards-compatible Base64 accessors
        public String getPublicKeyBase64() {
            return BarcoUtil.isNull(publicKeyBytes) ? null : Base64.getEncoder().encodeToString(publicKeyBytes);
        }

        public String getPrivateKeyEncryptedBase64() {
            return BarcoUtil.isNull(privateKeyEncryptedBytes) ? null : Base64.getEncoder().encodeToString(privateKeyEncryptedBytes);
        }

    }

    /**
     * Generate an RSA key pair and encrypt the private key using the provided AES secret.
     * If aesSecret is null or empty, the private key will not be encrypted: the returned
     * privateKeyEncryptedBytes will contain the raw PKCS#8 DER private key bytes.
     */
    private JwtKeyPair generate(int rsaKeySize, String aesSecret) throws Exception {
        LOGGER.debug("Starting RSA key pair generation with size {} bits", rsaKeySize);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(rsaKeySize);
        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // raw bytes
        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();
        LOGGER.debug("Generated public key (bytes length={})", publicKeyBytes.length);
        // Encrypt the raw private key bytes (return bytes, either raw DER or IV||ciphertext)
        byte[] encryptedPrivateBytes = this.encryptPrivateKeyBytesToBytes(privateKeyBytes, aesSecret);
        // Generate a key ID using the pattern: ETL:URI:RSI-<uuid>
        String keyId = String.format("ETL:URI:RSI-%s", UUID.randomUUID());
        LOGGER.info("Generated RSA key pair with keyId={}", keyId);
        if (BarcoUtil.isNull(aesSecret)) {
            LOGGER.info("Private key returned unencrypted (no AES secret provided)");
        } else {
            LOGGER.debug("Private key encrypted using AES; encrypted bytes length={}", encryptedPrivateBytes.length);
        }
        return new JwtKeyPair(keyId, publicKeyBytes, encryptedPrivateBytes);
    }

    /**
     * Helper to encrypt the provided private key bytes using AES/CBC/PKCS5Padding.
     * Returns Base64 string for compatibility with earlier callers.
     */
    private String encryptPrivateKeyBytes(byte[] privateKeyBytes, String aesSecret) throws Exception {
        if (BarcoUtil.isNull(aesSecret)) {
            LOGGER.debug("No AES secret provided; skipping encryption of private key");
            return Base64.getEncoder().encodeToString(privateKeyBytes);
        }
        byte[] combined = this.encryptPrivateKeyBytesToBytes(privateKeyBytes, aesSecret);
        return Base64.getEncoder().encodeToString(combined);
    }

    /**
     * Helper to encrypt the provided private key bytes and return raw bytes.
     * If aesSecret is null/empty, returns the raw privateKeyBytes (no encryption).
     */
    private byte[] encryptPrivateKeyBytesToBytes(byte[] privateKeyBytes, String aesSecret) throws Exception {
        if (BarcoUtil.isNull(aesSecret)) {
            LOGGER.debug("No AES secret provided; returning raw private key bytes");
            return privateKeyBytes;
        }
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(aesSecret.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        SecureRandom rnd = SecureRandom.getInstanceStrong();
        rnd.nextBytes(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
        byte[] cipherBytes = cipher.doFinal(privateKeyBytes);
        byte[] combined = new byte[iv.length + cipherBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherBytes, 0, combined, iv.length, cipherBytes.length);
        LOGGER.trace("AES encryption complete; ivLen={}, cipherLen={}", iv.length, cipherBytes.length);
        return combined;
    }

    /**
     * Decrypt a stored private key blob (Base64) and return raw PKCS#8 DER bytes.
     * Accepts both encrypted blobs (Base64(IV||ciphertext)) when aesSecret is provided
     * and raw Base64-encoded PKCS#8 bytes when aesSecret is null/empty.
     */
    public byte[] decryptPrivateKeyBytes(String privateKeyEncryptedBase64, String aesSecret) throws Exception {
        if (BarcoUtil.isNull(privateKeyEncryptedBase64)) {
            throw new IllegalArgumentException("privateKeyEncryptedBase64 is null");
        }
        byte[] decoded = Base64.getDecoder().decode(privateKeyEncryptedBase64);
        if (BarcoUtil.isNull(aesSecret)) {
            LOGGER.debug("No AES secret provided; returning decoded private key bytes");
            return decoded;
        }
        if (decoded.length <= 16) {
            throw new IllegalArgumentException("Encrypted private key blob is too short to contain IV + ciphertext");
        }
        byte[] iv = new byte[16];
        System.arraycopy(decoded, 0, iv, 0, 16);
        byte[] ciphertext = new byte[decoded.length - 16];
        System.arraycopy(decoded, 16, ciphertext, 0, ciphertext.length);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(aesSecret.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
        byte[] plain = cipher.doFinal(ciphertext);
        return plain;
    }

    /**
     * Convenience: decrypt the stored Base64 blob and parse it into an RSAPrivateKey.
     */
    public RSAPrivateKey decryptToPrivateKey(String privateKeyEncryptedBase64, String aesSecret) throws Exception {
        byte[] der = this.decryptPrivateKeyBytes(privateKeyEncryptedBase64, aesSecret);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    /**
     * Convenience method to generate a key pair with default RSA key size (2048 bits).
     */
    public JwtKeyPair generate(String aesSecret) throws Exception {
        return this.generate(2048, aesSecret);
    }

}
