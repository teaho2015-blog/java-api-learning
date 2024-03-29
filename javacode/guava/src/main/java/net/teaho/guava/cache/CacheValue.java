package net.teaho.guava.cache;

import lombok.Data;

@Data
public class CacheValue<V> {
    private final V v;
    private final long ttl;

    public CacheValue(V v, long ttl) {
        this.v = v;
        this.ttl = ttl;
    }

    public V getV() {
        return this.v;
    }
}
