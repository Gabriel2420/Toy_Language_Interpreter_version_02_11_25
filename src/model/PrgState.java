package model;

import model.adt.MyIDictionary;
import model.adt.MyIDictionaryHeap;
import model.adt.MyIList;
import model.adt.MyIStack;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

public class PrgState {

    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, IValue> symTable;
    private MyIList<IValue> out;
    private MyIDictionary<StringValue, BufferedReader> fileTable;
    private MyIDictionaryHeap heap;
    private IStmt originalProgram;

    public PrgState(MyIStack<IStmt> stack, MyIDictionary<String, IValue> symTable, MyIList<IValue> out,MyIDictionary<StringValue,BufferedReader>fileTable, MyIDictionaryHeap heap, IStmt program){
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
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
    public MyIDictionary<StringValue,BufferedReader> getFileTable() {
        return fileTable;
    }
    public MyIDictionaryHeap getHeap() {
        return heap;
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
    public void setFileTable(MyIDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }
    public void setHeap(MyIDictionaryHeap heap) {
        this.heap = heap;
    }
    public void setOriginalProgram(IStmt program) {
        this.originalProgram = program.deepCopy();
    }

    @Override
    public String toString(){
        return "ExeStack: " + exeStack + "\n" + "SymTable: " + symTable + "\n"+"Out: " + out + "\n" + "File table: " + fileTable + "\n" + heap;
    }
}


