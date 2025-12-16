package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIDictionaryHeap;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class RelationalExp implements IExp{

    private final IExp exp1;
    private final IExp exp2;
    private final int operation;

    public RelationalExp(IExp exp1, IExp exp2, int operation) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return switch (operation) {
            case 1 -> exp1.toString() + "<" + exp2.toString();
            case 2 -> exp1.toString() + "<=" + exp2.toString();
            case 3 -> exp1.toString() + "==" + exp2.toString();
            case 4 -> exp1.toString() + "!=" + exp2.toString();
            case 5 -> exp1.toString() + ">" + exp2.toString();
            case 6 -> exp1.toString() + ">=" + exp2.toString();
            default -> throw new RuntimeException(new MyException("Operation " + operation + "is not allowed."));
        };
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dictionary, MyIDictionaryHeap heap) throws MyException {
        IValue value1 = exp1.eval(dictionary, heap);
        IValue value2 = exp2.eval(dictionary, heap);

        if (!value1.getType().equals(new IntType()))
            throw new MyException("First operand is not an integer!");
        if (!value2.getType().equals(new IntType()))
            throw new MyException("Second operand is not an integer!");

        IntValue i1 = (IntValue) value1;
        IntValue i2 = (IntValue) value2;
        int n1 = i1.getValue();
        int n2 = i2.getValue();

        return switch (operation) {
            case 1 -> new BoolValue(n1 < n2);
            case 2 -> new BoolValue(n1 <= n2);
            case 3 -> new BoolValue(n1 == n2);
            case 4 -> new BoolValue(n1 != n2);
            case 5 -> new BoolValue(n1 > n2);
            case 6 -> new BoolValue(n1 >= n2);
            default -> throw new MyException("Unknown operator: " + operation);
        };
    }

    @Override
    public IExp deepCopy() {
        return new RelationalExp(this.exp1.deepCopy(),this.exp2.deepCopy(),operation);
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type1 = exp1.typecheck(typeEnv);
        IType type2 = exp2.typecheck(typeEnv);

        if(type1.equals(new IntType())) {
            if (type2.equals(new IntType())) return new BoolType();
            else throw new MyException("First operand is not an integer!");
        }
        else throw new MyException("Second operand is not an integer!");
    }
}
