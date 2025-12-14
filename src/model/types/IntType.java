package model.types;

import model.values.IValue;
import model.values.IntValue;

public class IntType implements IType {
    public boolean equals(Object another) {
        return another instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }

    @Override
    public IType deepCopy() {
        return this;
    }
}
