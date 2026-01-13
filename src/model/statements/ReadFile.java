package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.IExp;
import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStmt{
    private IExp exp;
    private String varName;

    public ReadFile(IExp exp, String varName) {
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if(!state.getSymTable().isDefined(varName)) {
            throw new MyException("Variable" + varName + "is not defined!");
        }

        IValue varValue = state.getSymTable().getValue(varName);
        if(!varValue.getType().equals(new IntType())) {
            throw new MyException("The type of the variable" + varName + "is not int");
        }

        IValue value = exp.eval(state.getSymTable(), state.getHeap());
        if(!value.getType().equals(new StringType())) {
            throw new MyException("The file name must be a string!");
        }

        StringValue fileNameValue = (StringValue) value;
        System.out.println(fileNameValue);
        if(!state.getFileTable().isDefined(fileNameValue)) {
            throw new MyException("The file " + fileNameValue.toString() + " it's not open in FileTable.");
        }
        BufferedReader reader = state.getFileTable().getValue(fileNameValue);

        try {
            String line = reader.readLine();
            int intValue;

            if (line == null) {
                intValue = 0;
            } else {
                try {
                    intValue = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    intValue = 0;
                }
            }
            state.getSymTable().put(varName, new IntValue(intValue));
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFile(this.exp.deepCopy(), this.varName);
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = exp.typecheck(typeEnv);
        IType typeVar = typeEnv.getValue(varName);

        if (!typeExp.equals(new StringType())) {
            throw new MyException("ReadFile statement: The file name expression is not a StringType!");
        }

        if (!typeVar.equals(new IntType())) {
            throw new MyException("ReadFile statement: The variable " + varName + " is not of type IntType!");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "readFile(" + this.exp.toString() + "," + this.varName + ")";
    }
}
