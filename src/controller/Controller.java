package controller;

import exceptions.MyException;
import model.PrgState;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepo;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private final IRepo repo;
    private ExecutorService executor;

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

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(p -> p.isNotCompleted()).collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<PrgState> prgStateList) {
        prgStateList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });

        List<Callable<PrgState>> callableList = prgStateList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                .collect(Collectors.toList());
        List<PrgState> newPrgList = null;
        try {
            newPrgList = executor.invokeAll(callableList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            System.out.println("Error in thread: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(p -> p != null)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        prgStateList.addAll(newPrgList);

        prgStateList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });
        repo.setPrgList(prgStateList);
    }

    public void allStep() throws MyException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgStateList = removeCompletedPrg(repo.getPrgList());
        while(prgStateList.size() > 0) {

            List<Integer> allSymTableAddresses = prgStateList.stream()
                            .flatMap(p -> getAddressFromSymTable(p.getSymTable().getContent().values()).stream())
                            .collect(Collectors.toList());
            Map<Integer, IValue> currentHeap = prgStateList.get(0).getHeap().getContent();
            List<Integer> allReachableAddresses = getTransitiveClosure(allSymTableAddresses, currentHeap);
            Set<Integer> removedAddresses = new HashSet<>();
            Map<Integer, IValue> newHeapContent = safeGarbageCollector(
                    allReachableAddresses,
                    currentHeap,
                    removedAddresses);
            prgStateList.get(0).getHeap().setContent(newHeapContent);

            repo.logGarbageCollector(removedAddresses);
            oneStepForAllPrg(prgStateList);
            prgStateList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        repo.setPrgList(prgStateList);
    }
}
