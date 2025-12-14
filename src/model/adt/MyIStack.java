package model.adt;

import java.util.List;

public interface MyIStack<T> {
    public void push(T element);
    public T pop();
    public boolean isEmpty();
    public List<T> getContent();
}
