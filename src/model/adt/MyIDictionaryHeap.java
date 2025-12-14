package model.adt;

import model.values.IValue;

import java.util.Map;

public interface MyIDictionaryHeap {
    int allocate(IValue value);
    void put(Integer address, IValue value);
    IValue getValue(Integer address);
    boolean containsAddress(Integer address);
    void removeValue(Integer address);
    Map<Integer, IValue> getContent();
    void setContent(Map<Integer, IValue> newContent);
}
