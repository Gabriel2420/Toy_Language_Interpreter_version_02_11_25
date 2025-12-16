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
import java.io.IOException;

public class CloseRFile implements IStmt{
    private IExp exp;

    public CloseRFile(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = exp.eval(state.getSymTable(), state.getHeap());
        if(!value.getType().equals(new StringType())) {
            throw new MyException("The file name must be a string!");
        }
        StringValue fileNameValue = (StringValue) value;
        if(!state.getFileTable().isDefined(fileNameValue)) {
            throw new MyException("The file " + fileNameValue.toString() + " it's not open in FileTable.");
        }
        BufferedReader reader = state.getFileTable().getValue(fileNameValue);

        try{
        reader.close();
        } catch (IOException e){
            throw new MyException(e.getMessage());
        }
        state.getFileTable().remove(fileNameValue,reader);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new CloseRFile(this.exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = exp.typecheck(typeEnv);

        if(typeExp.equals(new StringType())) {
            return typeEnv;
        }
        else throw new MyException("CloseRFile stmt: The file name expression is not a StringType!");
    }

    @Override
    public String toString() {
        return "closeRFile(" + this.exp.toString() + ")";
    }
}
