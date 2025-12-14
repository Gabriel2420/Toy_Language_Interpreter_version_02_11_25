package model.statements;
import exceptions.MyException;
import model.PrgState;

public interface IStmt {
    public PrgState execute(PrgState state) throws MyException;
    public IStmt deepCopy();
}
