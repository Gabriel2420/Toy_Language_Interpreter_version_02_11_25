package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.expressions.IExp;
import model.values.BoolValue;
import model.values.IValue;

public class WhileStmt implements IStmt{
    private IExp exp;
    private IStmt stmt;

    public WhileStmt(IExp exp, IStmt stmt) {
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = exp.eval(state.getSymTable(), state.getHeap());

        if(!(value instanceof BoolValue)) {
            throw new MyException("Condition is not a boolean!");
        }
        else {
            BoolValue val = (BoolValue) value;
            if(val.getBool()) {
                state.getExeStack().push(this);
                state.getExeStack().push(stmt);
            }
        }
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(this.exp.deepCopy(), this.stmt.deepCopy());
    }

    @Override
    public String toString() {
        return "while(" + exp.toString() + ") " + stmt.toString();
    }
}
