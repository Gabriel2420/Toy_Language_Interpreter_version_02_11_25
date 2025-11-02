package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.values.IValue;

public class ValueExp implements IExp{
    private IValue value;

    public ValueExp(IValue newValue){

        value = newValue;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dictionary) throws MyException {
        return value;
    }

    @Override
    public IExp deepCopy() {
        return new ValueExp(this.value.deepCopy());
    }

    @Override
    public String toString(){
        return value.toString();
    }
}
