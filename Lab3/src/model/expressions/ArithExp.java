package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

public class ArithExp implements IExp{
    private final IExp exp1;
    private final IExp exp2;
    private final int operation;
    public ArithExp(IExp exp1,IExp exp2, int operation) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.operation = operation;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dictionary) throws MyException {
        IValue value1 = exp1.eval(dictionary);
        if (!value1.getType().equals(new IntType()))
            throw new MyException("First operand is not an integer!");

        IValue value2 = exp2.eval(dictionary);
        if (!value2.getType().equals(new IntType()))
            throw new MyException("Second operand is not an integer!");

        IntValue i1 = (IntValue) value1;
        IntValue i2 = (IntValue) value2;
        int n1 = i1.getValue();
        int n2 = i2.getValue();

        return switch (operation) {
            case 1 -> new IntValue(n1 + n2);
            case 2 -> new IntValue(n1 - n2);
            case 3 -> new IntValue(n1 * n2);
            case 4 -> {
                if (n2 == 0)
                    throw new MyException("Division by zero is not allowed!");
                yield new IntValue(n1 / n2);
            }
            default -> throw new MyException("Unknown operator: " + operation);
        };
    }


    @Override
    public String toString() {
        return switch (operation) {
            case 1 -> exp1.toString() + "+" + exp2.toString();
            case 2 -> exp1.toString() + "-" + exp2.toString();
            case 3 -> exp1.toString() + "*" + exp2.toString();
            case 4 -> exp1.toString() + "/" + exp2.toString();
            default -> throw new RuntimeException(new MyException("Operation not allowed: " + operation));
        };
    }

    @Override
    public IExp deepCopy() {
        return new ArithExp(this.exp1.deepCopy(),this.exp2.deepCopy(),operation);
    }
}
