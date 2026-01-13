package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIList;
import model.expressions.IExp;
import model.types.IType;
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
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<IValue> out = state.getOut();
        IValue value = exp.eval(state.getSymTable(), state.getHeap());
        out.add(value);
        return null;

    }
}
