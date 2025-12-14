package model.adt;

import java.util.Map;

public interface MyIDictionary<K,V> {
    public void put(K key, V value);
    public boolean isDefined(K key);
    public V getValue(K key);
    public void remove(K key, V value);
    public boolean containsKey(K key);
    public Map<K, V> getContent();
}
