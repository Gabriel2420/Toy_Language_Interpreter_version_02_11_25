package model.adt;

import model.values.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyDictionaryHeap implements MyIDictionaryHeap{
    private Map<Integer, IValue> heap;
    static int nextFreeAddress = 0;

    public MyDictionaryHeap() {
        this.heap = new HashMap<>();
        nextFreeAddress = 1;
    }

    @Override
    public int allocate(IValue value) {
        int address = nextFreeAddress;
        heap.put(address, value);
        nextFreeAddress++;
        return address;
    }

    @Override
    public void put(Integer address, IValue value) {
        this.heap.put(address,value);
    }

    @Override
    public IValue getValue(Integer address) {
        return this.heap.get(address);
    }

    @Override
    public boolean containsAddress(Integer address) {
        return heap.containsKey(address);
    }

    @Override
    public void removeValue(Integer address) {
        heap.remove(address);
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return heap;
    }

    @Override
    public void setContent(Map<Integer, IValue> newContent) {
        heap = newContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Heap:\n");
        for(Integer address : heap.keySet()) {
            sb.append(address).append(" --> ").append(heap.get(address)).append("\n");
        }
        return sb.toString();
    }
}
