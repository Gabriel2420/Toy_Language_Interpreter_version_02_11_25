package model.adt;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MyStack<T> implements MyIStack<T>{
   private final Stack<T> tail;

   public MyStack()
   {

       this.tail = new Stack<>();
   }

    @Override
    public void push(T element) {
        this.tail.push(element);
    }

    @Override
    public T pop() {
        return this.tail.pop();
    }

    @Override
    public boolean isEmpty() {
        return this.tail.isEmpty();
    }

    @Override
    public List<T> getContent() {
        return new ArrayList<>(this.tail);
    }

    @Override
    public String toString() {
        Stack<T> copy = new Stack<>();
        copy.addAll(this.tail);  // clone the original stack
        Stack<T> reversed = new Stack<>();

        while (!copy.isEmpty()) {
            reversed.push(copy.pop());  // reverse the stack
        }

        StringBuilder sb = new StringBuilder("[ ");
        for (T elem : reversed) {
            sb.append(elem).append(", ");
        }

        if (!reversed.isEmpty()) {
            sb.setLength(sb.length() - 2); // removes last comma and space
        }
        sb.append(" ]");

        return sb.toString();
    }

}
