package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIDictionaryHeap;
import model.types.IType;
import model.values.IValue;

public interface IExp {
    IValue eval (MyIDictionary<String, IValue> dictionary, MyIDictionaryHeap heap) throws MyException;
    public  IExp deepCopy();
    IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException;
}
