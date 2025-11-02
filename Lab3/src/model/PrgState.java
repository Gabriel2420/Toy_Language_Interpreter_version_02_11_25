package model;

import model.adt.MyIDictionary;
import model.adt.MyIList;
import model.adt.MyIStack;
import model.statements.IStmt;
import model.values.IValue;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, IValue> symTable;
    private MyIList<IValue> out;
    private IStmt originalProgram;

    public PrgState(MyIStack<IStmt> stack, MyIDictionary<String, IValue> symTable, MyIList<IValue> out, IStmt program){
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program.deepCopy();
        stack.push(program);
    }

    public MyIStack<IStmt> getExeStack() {
        return exeStack;
    }

    public MyIDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public MyIList<IValue> getOut() {
        return out;
    }

    public void setExeStack(MyIStack<IStmt> stack) {
        this.exeStack = stack;
    }

    public void setSymTable(MyIDictionary<String,IValue> symTable) {
        this.symTable = symTable;
    }

    public void setOut(MyIList<IValue> out) {
        this.out = out;
    }

    public void setOriginalProgram(IStmt program) {
        this.originalProgram = program.deepCopy();
    }

    @Override
    public String toString(){
        return "ExeStack: " + exeStack + "\n" + "SymTable: " + symTable + "\n"+"Out: " + out;
    }
}

/// Intreabare : de ce facem deepCopy doar la  originalProgram si nu si la celelalte ?
/// Ar fi ok sa facem deepCopy si la celelalte ?
