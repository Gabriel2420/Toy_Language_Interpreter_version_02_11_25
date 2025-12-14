package model.values;

import model.types.IType;
import model.types.RefType;

public class RefValue implements IValue{
    private final int address;
    private final IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    @Override
    public String toString() {
        return String.valueOf(this.address);
    }

    public int getAddress() {
        return this.address;
    }

    public IType getLocationType() {
        return this.locationType;
    }

    @Override
    public IType getType() {
        return new RefType(this.locationType);
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(this.address,this.locationType.deepCopy());
    }

    @Override
    public boolean equals(Object another) {
        if(this == another)
            return true;

        if(!(another instanceof RefValue other))
            return false;

        return this.address == other.address && this.locationType.equals(other.locationType);
    }

    @Override
    public int hashCode() {
        return 31 * address + locationType.hashCode();
    }
}
