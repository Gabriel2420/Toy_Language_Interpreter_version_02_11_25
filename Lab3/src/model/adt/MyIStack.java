package model.adt;

public interface MyIStack<T> {
    public void push(T element);
    public T pop();
    public boolean isEmpty();
}
