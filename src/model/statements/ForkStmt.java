package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.*;
import model.types.IType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

public class ForkStmt implements IStmt{

    private IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public String toString() {
        return "fork(" + this.stmt.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> newExeStack = new MyStack<>();
        MyIDictionary<String, IValue> newSymTable = state.getSymTable().clone();
        MyIDictionaryHeap newHeap = state.getHeap();
        MyIDictionary<StringValue, BufferedReader> newFileTable = state.getFileTable();
        MyIList<IValue> newOut = state.getOut();
        return new PrgState(newExeStack, newSymTable, newOut, newFileTable, newHeap, stmt);
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(stmt.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        stmt.typecheck(typeEnv.clone());
        return typeEnv;
    }
}
