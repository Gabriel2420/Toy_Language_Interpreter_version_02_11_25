package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIDictionaryHeap;
import model.values.IValue;

public class VarExp implements IExp{
    private String id;

    public VarExp(String givenId) {
        id = givenId;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dictionary, MyIDictionaryHeap heap) throws MyException {
        return dictionary.getValue(id);
    }

    @Override
    public IExp deepCopy() {
        return new VarExp(this.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
