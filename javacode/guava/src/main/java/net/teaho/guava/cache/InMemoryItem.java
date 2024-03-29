package net.teaho.guava.cache;

import lombok.Data;

@Data
public class InMemoryItem {
    public String id;
    public Object itemBytes;

    public InMemoryItem(String id, Object itemBytes) {
        this.id = id;
        this.itemBytes = itemBytes;
    }

}
