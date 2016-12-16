package utils.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;
    protected final int size;

    public static <K, V> Map<K, V> newInstance(int size) {
        return new LRUCache<>(size);
    }

    protected LRUCache(int size) {
        this.size = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > size;
    }

}
