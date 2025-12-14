package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class VarDeclStmt implements IStmt {
    private final String id;
    private final IType type;

    public VarDeclStmt(String newId, IType newType) {
        this.id = newId;
        this.type = newType;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + this.id;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(this.id, this.type.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        if(!symTable.isDefined(this.id)){
            symTable.put(id,this.type.defaultValue());
        }
        else
            throw new MyException("This variable is already defined");

        return state;
    }
}
