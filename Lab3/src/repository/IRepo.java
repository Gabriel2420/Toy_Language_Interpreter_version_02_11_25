package repository;

import exceptions.MyException;
import model.PrgState;


public interface IRepo {
    public PrgState getCrtPrg() throws MyException;
    public void addPrg(PrgState state);
}
