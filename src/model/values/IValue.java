package model.values;

import model.types.IType;

public interface IValue {
    public IType getType();
    public IValue deepCopy();
}
