package model.adt;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K,V> implements MyIDictionary<K,V>{
    private final Map<K,V> dictionary;

    public MyDictionary() {
        this.dictionary = new HashMap<>();
    }
    @Override
    public void put(K key, V value) {
        this.dictionary.put(key, value);
    }

    @Override
    public boolean isDefined(K key) {
        return dictionary.containsKey(key);
    }

    @Override
    public V getValue(K key) {
        return dictionary.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return dictionary.containsKey(key);
    }

    @Override
    public String toString() {
        return dictionary.toString();
    }

    @Override
    public void remove(K key, V value) {
        this.dictionary.remove(key,value);
    }

    @Override
    public Map<K, V> getContent() {
        return this.dictionary;
    }
}
