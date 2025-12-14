package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.expressions.IExp;
import model.types.IType;
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
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(this.varName,this.exp.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + exp.toString() + ")";
    }
}
