package repository;

import exceptions.MyException;
import model.PrgState;

import java.util.Set;


public interface IRepo {
    public PrgState getCrtPrg() throws MyException;
    public void addPrg(PrgState state);
    public void logPrgStateExec() throws MyException;
    public void logGarbageCollector(Set<Integer> removed) throws MyException;
}
