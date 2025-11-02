package model.adt;

public interface MyIDictionary<K,V> {
    public void put(K key, V value);
    public boolean isDefined(K key);
    public V getValue(K key);
}
