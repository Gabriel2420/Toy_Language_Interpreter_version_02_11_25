package gui;

import controller.Controller;
import exceptions.MyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyDictionaryHeap;
import model.adt.MyList;
import model.adt.MyStack;
import model.expressions.*;
import model.statements.*;
import model.types.*;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepo;
import repository.Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramSelectorController {

    @FXML
    private ListView<IStmt> programListView;

    @FXML
    private Button runButton;

    @FXML
    public void initialize() {
        // 1. Allow single selection only
        programListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // 2. Populate the list with the hardcoded examples
        programListView.setItems(getAllStatements());

        // 3. Set the button action
        runButton.setOnAction(actionEvent -> {
            IStmt selectedStmt = programListView.getSelectionModel().getSelectedItem();
            if (selectedStmt == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a program!", ButtonType.OK);                alert.showAndWait();
                return;
            }

            // 4. Try to launch the selected program
            launchProgram(selectedStmt);
        });
    }

    private void launchProgram(IStmt stmt) {
        try {
            // STEP 1: Perform Type Checking (Important!)
            // We verify if the program is valid before creating the controller
            MyDictionary<String, IType> typeEnv = new MyDictionary<>();
            stmt.typecheck(typeEnv);

            // STEP 2: Create the Backend Infrastructure
            // This matches exactly what was inside your addProgram method in Interpreter.java
            PrgState prgState = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    new MyDictionary<>(),
                    new MyDictionaryHeap(),
                    stmt
            );

            // Create a dynamic log file name so we don't overwrite the same file every time
            String logFile = "log" + System.currentTimeMillis() + ".txt";
            IRepo repo = new Repo(prgState, logFile);
            Controller controller = new Controller(repo);

            // STEP 3: Load the Executor Window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProgramExecutor.fxml"));
            Parent root = loader.load();

            // STEP 4: Pass the Controller to the new Window
            ProgramExecutorController executorController = loader.getController();
            executorController.setController(controller);

            // STEP 5: Show the Window
            Stage stage = new Stage();
            stage.setTitle("Program Executor");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (MyException | RuntimeException e) {
            // If Type Check fails or other errors occur
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<IStmt> getAllStatements() {
        List<IStmt> allStmts = new ArrayList<>();

        // Example 1
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
        allStmts.add(ex1);

        // Example 2
        IStmt ex2 = new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a",
                                        new ArithExp(
                                                new ValueExp(new IntValue(2)),
                                                new ArithExp(
                                                        new ValueExp(new IntValue(3)),
                                                        new ValueExp(new IntValue(5)), 3), 1)
                                ),
                                new CompStmt(
                                        new AssignStmt("b",
                                                new ArithExp(
                                                        new VarExp("a"),
                                                        new ValueExp(new IntValue(1)), 1)
                                        ),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );
        allStmts.add(ex2);

        // Example 3
        IStmt ex3 = new CompStmt(
                new VarDeclStmt("a", new BoolType()),
                new CompStmt(
                        new VarDeclStmt("v", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("a"),
                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
                                                new AssignStmt("v", new ValueExp(new IntValue(3)))
                                        ),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );
        allStmts.add(ex3);

        // Example 4 (File Operations)
        IStmt testExample = new CompStmt(
                new VarDeclStmt("varf", new StringType()),
                new CompStmt(
                        new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(
                                new OpenRFile(new VarExp("varf")),
                                new CompStmt(
                                        new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(
                                                new ReadFile(new VarExp("varf"), "varc"),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(
                                                                new ReadFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFile(new VarExp("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStmts.add(testExample);

        // Example 5 (Heap Allocation)
        IStmt heapAllocExample = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))
                                )
                        )
                )
        );
        allStmts.add(heapAllocExample);

        // Example 6 (Heap Reading)
        IStmt heapReadingExample = new CompStmt(
                new VarDeclStmt("v",new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp(new ReadHeapExp(new ReadHeapExp(new VarExp("a"))), new ValueExp(new IntValue(5)), 1))
                                        )
                                )
                        )
                )
        );
        allStmts.add(heapReadingExample);

        // Example 7 (Heap Writing)
        IStmt heapWritingExample = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new CompStmt(
                                        new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp(new ReadHeapExp(new VarExp("v")), new ValueExp(new IntValue(5)),1))
                                )
                        )
                )
        );
        allStmts.add(heapWritingExample);

        // Example 8 (While Statement)
        IStmt whileStmtExample = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(
                                new WhileStmt(new RelationalExp(new VarExp("v"), new ValueExp(new IntValue(0)), 5), new CompStmt(
                                        new PrintStmt(new VarExp("v")),new AssignStmt("v", new ArithExp(new VarExp("v"), new ValueExp(new IntValue(1)), 2))
                                )),
                                new PrintStmt(new VarExp("v"))
                        )
                )
        );
        allStmts.add(whileStmtExample);

        // Example 9 (Garbage Collector)
        IStmt garbageCollectorExample = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new NewStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new ReadHeapExp(new ReadHeapExp(new VarExp("a"))))
                                        )
                                )
                        )
                )
        );
        allStmts.add(garbageCollectorExample);

        // Example 10 (Deep Ref Garbage Collector)
        IStmt deepRefGarbageCollector = new CompStmt(
                new VarDeclStmt("r1", new RefType(new IntType())),
                new CompStmt(
                        new VarDeclStmt("r2", new RefType(new RefType(new IntType()))),
                        new CompStmt(
                                new VarDeclStmt("r3", new RefType(new RefType(new RefType(new IntType())))),
                                new CompStmt(
                                        new VarDeclStmt("r4", new RefType(new RefType(new RefType(new RefType(new IntType()))))),
                                        new CompStmt(
                                                new NewStmt("r1", new ValueExp(new IntValue(100))),
                                                new CompStmt(
                                                        new NewStmt("r2", new VarExp("r1")),
                                                        new CompStmt(
                                                                new NewStmt("r3", new VarExp("r2")),
                                                                new CompStmt(
                                                                        new NewStmt("r4", new VarExp("r3")),
                                                                        new CompStmt(
                                                                                new NewStmt("r1", new ValueExp(new IntValue(999))),
                                                                                new CompStmt(
                                                                                        new NewStmt("r2", new VarExp("r1")),
                                                                                        new CompStmt(
                                                                                                new NewStmt("r3", new VarExp("r2")),
                                                                                                new CompStmt(
                                                                                                        new NewStmt("r4", new VarExp("r3")),
                                                                                                        new PrintStmt(new ReadHeapExp(
                                                                                                                new ReadHeapExp(
                                                                                                                        new ReadHeapExp(new VarExp("r4"))
                                                                                                                )
                                                                                                        ))
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStmts.add(deepRefGarbageCollector);

        // Example 11 (Threads)
        IStmt threadsExample = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(
                                        new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new WriteHeapStmt("a", new ValueExp(new IntValue(30))),
                                                                new CompStmt(
                                                                        new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                                        new CompStmt(
                                                                                new PrintStmt(new VarExp("v")),
                                                                                new PrintStmt(new ReadHeapExp(new VarExp("a")))
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(new ReadHeapExp(new VarExp("a")))
                                                )
                                        )
                                )
                        )
                )
        );
        allStmts.add(threadsExample);

        return FXCollections.observableArrayList(allStmts);
    }
}