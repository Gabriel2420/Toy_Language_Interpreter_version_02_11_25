package model.types;

import model.values.IValue;
import model.values.RefValue;

public class RefType  implements IType{
    private final IType inner;

    public RefType(IType inner) {
        this.inner = inner;
    }

    @Override
    public String toString() {
        return "Ref(" + this.inner.toString() + ")";
    }

    @Override
    public IValue defaultValue() {
        return new RefValue(0,this.inner);
    }

    @Override
    public IType deepCopy() {
        return new RefType(inner.deepCopy());
    }

    public IType getInner() {
        return this.inner;
    }
    @Override
    public boolean equals(Object another) {
        if(another instanceof RefType)
            return inner.equals(((RefType) another).getInner());
        else return false;
    }

    @Override
    public int hashCode() {
        return inner.hashCode() * 31;
    }
}
