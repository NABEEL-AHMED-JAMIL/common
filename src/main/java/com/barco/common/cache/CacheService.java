package com.barco.common.cache;

/**
 * Simple cache service contract for storing and removing objects in a shared cache.
 * Implementations may use Caffeine or any other caching provider.
 */
public interface CacheService {

    /**
     * Put a value into the cache for the given key. Overwrites any existing value.
     * @param key cache key (not null)
     * @param value value to store (maybe null)
     */
    void put(String key, Object value);

    /**
     * Get a value from the cache.
     * @param key cache key
     * @return stored value or null if absent
     */
    Object get(String key);

    /**
     * Get a value and attempt to cast to the requested type.
     * @param key cache key
     * @param clazz desired class
     * @param <T> type
     * @return value cast to T or null when absent
     * @throws ClassCastException when value cannot be cast to clazz
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * Remove an entry from the cache.
     * @param key cache key
     */
    void evict(String key);

    /**
     * Clear all entries from the cache.
     */
    void clear();

    /**
     * Check whether the cache contains a value for the given key.
     * @param key cache key
     * @return true if present
     */
    boolean contains(String key);
}

