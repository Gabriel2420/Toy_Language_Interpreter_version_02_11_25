package model.types;

import model.values.BoolValue;
import model.values.IValue;

public class BoolType implements IType {
    public boolean equals(Object another) {
        return another instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public IValue defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public IType deepCopy() {
        return this;
    }
}
