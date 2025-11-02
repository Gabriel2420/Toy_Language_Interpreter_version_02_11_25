package repository;

import exceptions.MyException;
import model.PrgState;

import java.util.ArrayList;
import java.util.List;

public class Repo implements IRepo{
    private final List<PrgState> program;

    public Repo() {
        this.program = new ArrayList<>();
    }

    @Override
    public void addPrg(PrgState state) {
        this.program.add(state);
    }

    @Override
    public PrgState getCrtPrg() throws MyException{
       if(program.isEmpty()) throw new MyException("The program is empty...");
       else return program.get(program.size() - 1);
    }
}
