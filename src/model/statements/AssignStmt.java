package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.IExp;
import model.types.IType;
import model.values.IValue;

public class AssignStmt  implements IStmt{
    private final String id;
    private final IExp exp;

    public AssignStmt(String id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return this.id + "=" + this.exp.toString();
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(this.id,this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.getValue(id);
        IType typeExp = exp.typecheck(typeEnv);
        if(typeVar.equals(typeExp)) return typeEnv;
        else throw new MyException("Assignment: right hand side and left hand side have different types!");
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable();

        if(symTbl.isDefined(id)){
            IValue value = exp.eval(symTbl, state.getHeap());
            IType typeID = (symTbl.getValue(id)).getType();
            if(value.getType().equals(typeID)){
                symTbl.put(id,value);
            }
            else throw new MyException("Declared type of variable " + id + " and type of the assigned expression do not match");
        }
        else throw new MyException("The used variable " + id + " was not declared before");
        return state;
    }
}
