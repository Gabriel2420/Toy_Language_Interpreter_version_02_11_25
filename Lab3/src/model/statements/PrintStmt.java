package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIList;
import model.expressions.IExp;
import model.values.IValue;

import java.beans.Expression;

public class PrintStmt implements IStmt{
    private final IExp exp;
    public PrintStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(this.exp.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<IValue> out = state.getOut();
        IValue value = exp.eval(state.getSymTable());
        out.add(value);
        return state;

    }
}
