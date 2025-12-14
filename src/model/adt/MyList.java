package model.adt;

import java.util.ArrayList;
import java.util.List;

public class MyList<V> implements MyIList<V>{
    private final List<V> elements;

    public MyList() {
        this.elements = new ArrayList<>();
    }

    @Override
    public void add(V value) {
        elements.add(value);
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public void remove(V value) {
        this.elements.remove(value);
    }

    @Override
    public List<V> getContent() {
        return this.elements;
    }
}
