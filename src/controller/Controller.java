package controller;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.statements.IStmt;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepo;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private final IRepo repo;

    public Controller(IRepo repo) {
        this.repo = repo;
    }

    public Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, IValue> heap, Set<Integer> removedOut) {
        return heap.entrySet().stream()
                .filter(e -> {boolean keep = symTableAddresses.contains(e.getKey());
                    if(!keep) {
                        removedOut.add(e.getKey());
                    }
                    return keep;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Integer> getTransitiveClosure(List<Integer> roots, Map<Integer, IValue> heap) {

        List<Integer> heapReferenced = heap.entrySet().stream()
                .filter(entry -> roots.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toList());

        List<Integer> merged = Stream.concat(roots.stream(), heapReferenced.stream())
                .distinct()
                .collect(Collectors.toList());

        if (merged.size() == roots.size())
            return merged;

        return getTransitiveClosure(merged, heap);
    }

    public List<Integer> getAddressFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
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
        repo.logPrgStateExec();

        while (!program.getExeStack().isEmpty()) {
            oneStep(program);
            displayCrtPrgState(program);


            List<Integer> roots = getAddressFromSymTable(program.getSymTable().getContent().values());
            List<Integer> reachable = getTransitiveClosure(roots, program.getHeap().getContent());
            Set<Integer> removed = new HashSet<>();
            Map<Integer, IValue> newHeap = safeGarbageCollector(reachable,program.getHeap().getContent(), removed);
            program.getHeap().setContent(newHeap);

            repo.logGarbageCollector(removed);
            repo.logPrgStateExec();
        }
    }

    public void displayCrtPrgState(PrgState state) {
        System.out.println(state);
    }
}
