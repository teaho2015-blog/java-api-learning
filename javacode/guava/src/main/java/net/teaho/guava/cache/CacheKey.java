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
    private String field;


    @Override
    public int hashCode() {
        return Objects.hash(key, field);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey that = (CacheKey)o;
        return Objects.equals(key, that.key) && Objects.equals(field, that.field);
    }
}