package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.expressions.IExp;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStmt implements IStmt{
    private final IExp exp;
    private final IStmt thenStmt;
    private final IStmt elseStmt;

    public IfStmt(IExp expression, IStmt thenStmt, IStmt elseStmt){
        this.exp = expression;
        this.elseStmt = elseStmt;
        this.thenStmt = thenStmt;
    }

    @Override
    public String toString() {
        return "(IF(" + exp.toString() + ") THEN(" + thenStmt.toString()
                + ") ELSE(" + elseStmt.toString() + "))";
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(this.exp.deepCopy(), this.thenStmt.deepCopy(), this.elseStmt.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        IValue value = exp.eval(state.getSymTable(), state.getHeap());

        if(!value.getType().equals(new BoolType())) throw new MyException("Conditional expression is not a boolean!");
        BoolValue cond = (BoolValue) value;

        if(cond.getBool())
            stk.push(thenStmt);
        else stk.push(elseStmt);
        return state;
    }
}
