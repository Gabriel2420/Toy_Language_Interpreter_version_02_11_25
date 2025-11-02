package controller;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.statements.IStmt;
import repository.IRepo;

public class Controller {
    private final IRepo repo;

    public Controller(IRepo repo) {
        this.repo = repo;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        MyIStack<IStmt> stack = state.getExeStack();
        if(stack.isEmpty())throw new MyException("PrgState stack is empty");
        IStmt currentStmt = stack.pop();
        return currentStmt.execute(state);
    }

    public void allStep() throws MyException {
        PrgState program = repo.getCrtPrg();

        if(program == null) throw new MyException("Repository is empty...");

        while (!program.getExeStack().isEmpty()) {
            displayCrtPrgState(oneStep(program));
        }
    }

    public void displayCrtPrgState(PrgState state) {
        System.out.println(state);
    }
}
