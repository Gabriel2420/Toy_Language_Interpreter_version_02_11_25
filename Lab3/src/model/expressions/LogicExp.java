package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class LogicExp implements IExp {
    private final IExp exp1;
    private final IExp exp2;
    private final int operation;

    public LogicExp(IExp exp1, IExp exp2, int operation) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return switch (operation) {
            case 1 -> exp1.toString() + "&&" + exp2.toString();
            case 2 -> exp1.toString() + "||" + exp2.toString();
            default -> throw new RuntimeException(new MyException("Operation not allowed: " + operation));
        };
    }

    @Override
    public IExp deepCopy() {
        return new LogicExp(this.exp1.deepCopy(),this.exp2.deepCopy(),operation);
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dictionary) throws MyException {
        IValue value1 = exp1.eval(dictionary);
        IValue value2 = exp2.eval(dictionary);

        if(value1.getType().equals(new BoolType())) {
            throw new MyException("First operand is not a boolean!");
        }

        if(value2.getType().equals(new BoolType())) {
            throw new MyException("Second operand is not a boolean!");
        }

        BoolValue b1 = (BoolValue) value1;
        BoolValue b2 = (BoolValue) value2;

        boolean bol1 = b1.getBool();
        boolean bol2 = b2.getBool();

        return switch (operation) {
            case 1 -> new BoolValue(bol1 && bol2);
            case 2 -> new BoolValue(bol1 || bol2);
            default -> throw new MyException("Unknown operator: " + operation);
        };
    }
}
