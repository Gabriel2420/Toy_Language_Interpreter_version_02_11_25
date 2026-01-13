package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.IExp;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class NewStmt implements IStmt{
    private final String varName;
    private final IExp exp;

    public NewStmt(String varName, IExp exp) {
        this.varName = varName;
        this.exp = exp;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if(!state.getSymTable().containsKey(varName)) {
            throw new MyException("Name variable is not defined!");
        }
        IValue value = state.getSymTable().getValue(varName);
        if(!(value instanceof RefValue refValue))
        {
            throw new MyException("Name variable is not RefType!");
        }

        IType locationType = refValue.getLocationType();
        IValue expValue = exp.eval(state.getSymTable(),state.getHeap());

        if(!expValue.getType().equals(locationType)) {
            throw new MyException("Type mismatch!");
        }

        int address = state.getHeap().allocate(expValue);
        state.getSymTable().put(varName, new RefValue(address, locationType));
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(this.varName,this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.getValue(varName);
        IType typeExp = exp.typecheck(typeEnv);
        if(typeVar.equals(new RefType(typeExp))) {
            return typeEnv;
        }
        else throw new MyException("New stmt: right hand side and left hand side have different types");
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + exp.toString() + ")";
    }
}
