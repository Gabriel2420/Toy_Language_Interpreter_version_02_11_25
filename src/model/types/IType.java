package model.types;

import model.values.IValue;

public interface IType {
    public IValue defaultValue();
    public IType deepCopy();
}
