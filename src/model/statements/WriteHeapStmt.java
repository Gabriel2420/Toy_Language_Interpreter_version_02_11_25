package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.IExp;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class WriteHeapStmt implements IStmt{
    private String varName;
    private IExp exp;

    public WriteHeapStmt(String varName, IExp exp) {
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
        if(!(state.getHeap().containsAddress(refValue.getAddress()))) {
            throw new MyException("The RefValue associated in symTable is not a key in heap!");
        }

        IType locationType = refValue.getLocationType();
        IValue expValue = exp.eval(state.getSymTable(),state.getHeap());

        if(!expValue.getType().equals(locationType)) {
            throw new MyException("Type mismatch!");
        }
        state.getHeap().put(refValue.getAddress(), expValue);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WriteHeapStmt(this.varName, this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.getValue(varName);
        IType typeExp = exp.typecheck(typeEnv);
        if (typeVar instanceof RefType refType) {
            if (typeExp.equals(refType.getInner())) {
                return typeEnv;
            } else {
                throw new MyException("WRITE HEAP: Expression type does not match the reference location type!");
            }
        } else {
            throw new MyException("WRITE HEAP: Variable " + varName + " is not a Reference Type!");
        }
    }

    @Override
    public String toString() {
        return "wH(" + varName + "," + exp.toString() + ")";
    }
}
