package repository;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionaryHeap;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Repo implements IRepo{
    private List<PrgState> program;
    private final String logFilePath;

    public Repo(PrgState state,String logFilePath) {
        this.program = new ArrayList<>();
        this.program.add(state);
        this.logFilePath = logFilePath;
    }

    @Override
    public List<PrgState> getPrgList() {
        return program;
    }

    @Override
    public void setPrgList(List<PrgState> prgStateList) {
        this.program = prgStateList;
    }

    @Override
    public void logGarbageCollector(Set<Integer> removed) throws MyException {
        try(PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println("Garbage collector removed: ");
            if(removed.isEmpty())
                logFile.println("  (nothing)");
            else {
                removed.forEach(address -> logFile.println("  address " + address));
            }
            logFile.println("-----------------------------------\n");
        } catch (IOException e) {
            throw new MyException("Writing in Garbage Collector log file error: " + e.getMessage());
        }
    }

    @Override
    public void logPrgStateExec(PrgState state) throws MyException {
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true))))
        {
            logFile.println("Id:");
            logFile.println(state.getId());

            logFile.println("Exe stack:");
            List<IStmt> stackContent = new ArrayList<>(state.getExeStack().getContent());
            Collections.reverse(stackContent);
            for(IStmt stmt : stackContent) {
                logFile.println(stmt.toString());
            }

            logFile.println("SymTable:");
            for (Map.Entry<String, IValue> entry : state.getSymTable().getContent().entrySet()) {
                logFile.println(entry.getKey() + " --> " + entry.getValue().toString());
            }

            logFile.println("Out:");
            for (IValue value : state.getOut().getContent()) {
                logFile.println(value.toString());
            }

            logFile.println("FileTable:");
            for(StringValue filename : state.getFileTable().getContent().keySet()) {
                logFile.println(filename);
            }

            logFile.println("Heap:");
            for(Integer address : state.getHeap().getContent().keySet()) {
                logFile.println(address + " --> " + state.getHeap().getValue(address));
            }

            logFile.println("\n-----------------------------------\n");
        }   catch (IOException e){
            throw new MyException("Writing in log file error: " + e.getMessage());
        }
    }
}
