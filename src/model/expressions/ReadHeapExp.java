package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIDictionaryHeap;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class ReadHeapExp implements IExp{
    private IExp exp;

    public ReadHeapExp(IExp exp) {
        this.exp = exp;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dictionary, MyIDictionaryHeap heap) throws MyException {
        IValue value = exp.eval(dictionary, heap);
        if(!(value instanceof RefValue refValue)) {
            throw new MyException("The expression doesn't evaluate to a RefValue!");
        }
        int address = refValue.getAddress();

        if(!(heap.containsAddress(address))) {
            throw new MyException("Address not found in heap!");
        }
        return heap.getValue(address);
    }

    @Override
    public IExp deepCopy() {
        return new ReadHeapExp(this.exp.deepCopy());
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type = exp.typecheck(typeEnv);
        if(type instanceof RefType refType) {
            return refType.getInner();
        }
        else throw new MyException("The ReadHeap argument is not a RefType!");
    }

    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }
}
