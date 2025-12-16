package model.statements;
import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.types.IType;

public interface IStmt {
    public PrgState execute(PrgState state) throws MyException;
    public IStmt deepCopy();
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException;
}
