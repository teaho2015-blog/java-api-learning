package net.teaho.guava.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CacheKey {
    private String key;
    private String hashField;


    @Override
    public int hashCode() {
        return Objects.hash(key, hashField);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey that = (CacheKey)o;
        return Objects.equals(key, that.key) && Objects.equals(hashField, that.hashField);
    }
}