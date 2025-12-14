package model.values;

import model.types.IType;
import model.types.StringType;

import java.util.Objects;

public class StringValue implements IValue{
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    public String getStringValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof StringValue){
            return ((StringValue) another).value.equals(this.value);
        }
       return false;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(this.value);
    }
}
