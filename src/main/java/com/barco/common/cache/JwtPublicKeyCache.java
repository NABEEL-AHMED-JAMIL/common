package com.barco.common.cache;

import com.barco.common.utility.BarcoUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * Caffeine-backed cache service. Provides simple put/get/evict operations.
 * Default settings: maximumSize = 100, expireAfterAccess = 60 minutes.
 * @author Nabeel Ahmed
 */
@Component("jwtPublicKeyCache")
public class JwtPublicKeyCache implements CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtPublicKeyCache.class);

    private Cache<String, byte[]> cache;

    @PostConstruct
    public void init() {
        this.cache = Caffeine.newBuilder().maximumSize(100).build();
        LOGGER.info("JwtPublicKeyCache initialized with maxSize=100 and expireAfterAccess=60m");
    }

    /**
     * Put a public key into the cache.
     * <p>
     * If key is null the operation is a no-op. If value is null the entry is invalidated
     * (evicted) from the cache. This method expects the value to be a byte[] containing
     * the public key bytes; passing any other type will be ignored and logged as a warning.
     * </p>
     * @param key cache key (typically the keyId/kid)
     * @param value public key bytes (byte[]) or null to evict
     */
    @Override
    public void put(String key, Object value) {
        if (BarcoUtil.isNull(key)) {
            LOGGER.debug("JwtPublicKeyCache.put called with null key - ignoring");
            return;
        }
        if (BarcoUtil.isNull(value)) {
            // invalidate when null value provided
            this.cache.invalidate(key);
            LOGGER.debug("JwtPublicKeyCache.invalidate key={}", key);
            return;
        }
        if (!(value instanceof byte[])) {
            LOGGER.warn("JwtPublicKeyCache.put received unsupported value type={} " +
                "for key={}. Expected byte[]; ignoring.", value.getClass().getName(), key);
            return;
        }
        byte[] bytes = (byte[]) value;
        this.cache.put(key, bytes);
        LOGGER.debug("JwtPublicKeyCache.put key={} len={}", key, bytes.length);
    }

    /**
     * Retrieve the cached public key bytes for the given key.
     * @param key cache key
     * @return byte[] public key or null when absent
     */
    @Override
    public Object get(String key) {
        if (BarcoUtil.isNull(key)) {
            LOGGER.debug("JwtPublicKeyCache.get called with null key");
            return null;
        }
        byte[] val = this.cache.getIfPresent(key);
        if (val == null) {
            LOGGER.debug("JwtPublicKeyCache.get miss for key={}", key);
        } else {
            LOGGER.debug("JwtPublicKeyCache.get hit for key={} len={}", key, val.length);
        }
        return val;
    }

    /**
     * Retrieve and cast the cached value to the requested type.
     * This will throw ClassCastException if the cached value cannot be cast to the provided class.
     * @param key cache key
     * @param clazz desired class to cast to
     * @param <T> type
     * @return cached value cast to T or null if absent
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = get(key);
        if (BarcoUtil.isNull(value)) {
            return null;
        }
        return clazz.cast(value);
    }

    /**
     * Evict a value from the cache for the provided key.
     * @param key cache key to invalidate
     */
    @Override
    public void evict(String key) {
        if (BarcoUtil.isNull(key)) {
            LOGGER.debug("JwtPublicKeyCache.evict called with null key - ignoring");
            return;
        }
        this.cache.invalidate(key);
        LOGGER.debug("JwtPublicKeyCache.evict key={}", key);
    }

    /**
     * Clear the entire cache.
     */
    @Override
    public void clear() {
        this.cache.invalidateAll();
        LOGGER.info("JwtPublicKeyCache cleared all entries");
    }

    /**
     * Check whether a value for the given key exists in the cache.
     * @param key cache key
     * @return true if present
     */
    @Override
    public boolean contains(String key) {
        boolean present = get(key) != null;
        LOGGER.debug("JwtPublicKeyCache.contains key={} present={}", key, present);
        return present;
    }
}
