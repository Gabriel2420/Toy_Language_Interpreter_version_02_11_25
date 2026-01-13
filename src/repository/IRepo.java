package repository;

import exceptions.MyException;
import model.PrgState;

import java.util.Set;
import java.util.List;


public interface IRepo {
    public void logPrgStateExec(PrgState state) throws MyException;
    public void logGarbageCollector(Set<Integer> removed) throws MyException;
    public List<PrgState> getPrgList();
    public void setPrgList(List<PrgState> prgStateList);
}
