package model.expressions;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.values.IValue;

public interface IExp {
    IValue eval (MyIDictionary<String, IValue> dict) throws MyException;
    public  IExp deepCopy();
}
