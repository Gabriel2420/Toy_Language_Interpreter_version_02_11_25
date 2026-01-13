package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.IExp;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenRFile implements IStmt{
    private final IExp exp;

    public OpenRFile(IExp exp1) {
        this.exp = exp1;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = exp.eval(state.getSymTable(), state.getHeap());

        if (!value.getType().equals(new StringType()))
            throw new MyException("The file name must be a string!");

        StringValue value1 = (StringValue) value;
        if (state.getFileTable().isDefined(value1))
            throw new MyException("The file is already open!");

        try {
            state.getFileTable().put(value1, new BufferedReader(new FileReader(value1.getStringValue())));
        } catch (FileNotFoundException e){
            throw new MyException(e.getMessage());
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new OpenRFile(this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = exp.typecheck(typeEnv);

        if(typeExp.equals(new StringType())) {
            return typeEnv;
        }
        else throw new MyException("OpenRFile stmt: The file name expression is not a StringType!");
    }

    @Override
    public String toString() {
        return "openRFile(" + this.exp.toString() + ")";
    }
}
