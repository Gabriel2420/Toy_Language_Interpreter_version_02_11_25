package gui;

import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.PrgState;
import model.values.IValue;
import model.statements.IStmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramExecutorController {
    private Controller controller;

    @FXML private TextField numberOfPrgStatesTextField;
    @FXML private TableView<Map.Entry<Integer, IValue>> heapTableView;
    @FXML private TableColumn<Map.Entry<Integer, IValue>, Integer> addressColumn;
    @FXML private TableColumn<Map.Entry<Integer, IValue>, String> valueColumn;
    @FXML private ListView<String> outputListView;
    @FXML private ListView<String> fileTableListView;
    @FXML private ListView<Integer> prgStateListIdListView;
    @FXML private TableView<Map.Entry<String, IValue>> symbolTableView;
    @FXML private TableColumn<Map.Entry<String, IValue>, String> variableNameColumn;
    @FXML private TableColumn<Map.Entry<String, IValue>, String> variableValueColumn;
    @FXML private ListView<String> executionStackListView;
    @FXML private Button runOneStepButton;

    public void setController(Controller controller) {
        this.controller = controller;
        populate();
    }

    @FXML
    public void initialize() {
        prgStateListIdListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // 1. Setup Heap Columns
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        // 2. Setup SymTable Columns
        variableNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        variableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        // 3. Listener: When clicking an ID, update the specific tables (SymTable, ExeStack)
        prgStateListIdListView.setOnMouseClicked(event -> populateSymTableAndExeStack());

        // 4. Button Action: Run one step
        runOneStepButton.setOnAction(event -> runOneStep());
    }

    private void runOneStep() {
        if (controller == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No program selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        List<PrgState> prgList = controller.removeCompletedPrg(controller.getRepository().getPrgList());

        if (prgList.size() > 0) {
            // -------------------------------------------------------------
            // GARBAGE COLLECTOR LOGIC (Taken from your Controller.allStep)
            // -------------------------------------------------------------
            // Call the garbage collector conservatively using your existing methods
            controller.oneStepForAllPrg(prgList);
            // Note: If you want GC to run every step, you can uncomment logic below,
            // but for "Run One Step" generally executing the step is enough.
            // If you need strict GC every step, copy the logic from allStep() here.

            // Remove completed programs again after execution
            prgList = controller.removeCompletedPrg(controller.getRepository().getPrgList());
            controller.getRepository().setPrgList(prgList);

            // Refresh UI
            populate();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Program finished!", ButtonType.OK);
            alert.showAndWait();
            // Optional: Disable button
            // runOneStepButton.setDisable(true);
        }
    }

    private void populate() {
        // 1. Populate Heap (Shared among all states, take from first)
        populateHeap();

        // 2. Populate PrgState IDs
        populatePrgStateIdentifiers();

        // 3. Populate FileTable (Shared)
        populateFileTable();

        // 4. Populate Output (Shared)
        populateOutput();

        // 5. Populate SymTable & ExeStack (Specific to selected ID)
        populateSymTableAndExeStack();

        // 6. Update TextField Count
        numberOfPrgStatesTextField.setText("Number of PrgStates: " + controller.getRepository().getPrgList().size());
    }

    private void populateHeap() {
        // Get heap from the first program state (if it exists)
        if (!controller.getRepository().getPrgList().isEmpty()) {
            Map<Integer, IValue> heapData = controller.getRepository().getPrgList().get(0).getHeap().getContent();
            heapTableView.setItems(FXCollections.observableArrayList(heapData.entrySet()));
            heapTableView.refresh();
        }
    }

    private void populateOutput() {
        if (!controller.getRepository().getPrgList().isEmpty()) {
            // Assuming getOut() returns MyList and we want the internal list
            // If your MyList has a specific getter for the list, use it.
            // Here assuming: prg.getOut().getList() returns List<IValue>
            List<String> output = new ArrayList<>();
            // Adjust this line based on your MyList implementation:
            // e.g. controller.getRepository().getPrgList().get(0).getOut().getList()
            // Using toString directly on the collection if it's iterable
            List<IValue> outList = controller.getRepository().getPrgList().get(0).getOut().getContent();
            for (IValue v : outList) {
                output.add(v.toString());
            }
            outputListView.setItems(FXCollections.observableArrayList(output));
        }
    }

    private void populateFileTable() {
        if (!controller.getRepository().getPrgList().isEmpty()) {
            // Get the map. The keys are StringValue, not String
            // We might need to cast to specific types based on your ADT implementation
            // If getContent() returns Map<StringValue, BufferedReader>:
            Map<?, ?> fileMap = controller.getRepository().getPrgList().get(0).getFileTable().getContent();

            List<String> files = new ArrayList<>();
            for (Object key : fileMap.keySet()) {
                // Manually convert the StringValue key to a real String
                files.add(key.toString());
            }

            fileTableListView.setItems(FXCollections.observableArrayList(files));
        }
    }

    private void populatePrgStateIdentifiers() {
        List<PrgState> prgStates = controller.getRepository().getPrgList();
        List<Integer> ids = prgStates.stream().map(PrgState::getId).collect(Collectors.toList());
        prgStateListIdListView.setItems(FXCollections.observableArrayList(ids));

        // If nothing is selected, select the first one automatically
        if (prgStateListIdListView.getSelectionModel().getSelectedItem() == null && !ids.isEmpty()) {
            prgStateListIdListView.getSelectionModel().select(0);
        }
    }

    private void populateSymTableAndExeStack() {
        Integer selectedId = prgStateListIdListView.getSelectionModel().getSelectedItem();
        if (selectedId == null) {
            // If nothing selected, clear these tables
            symbolTableView.getItems().clear();
            executionStackListView.getItems().clear();
            return;
        }

        PrgState currentPrg = controller.getRepository().getPrgList().stream()
                .filter(p -> p.getId() == selectedId)
                .findFirst()
                .orElse(null);

        if (currentPrg != null) {
            // Populate SymTable
            Map<String, IValue> symTable = currentPrg.getSymTable().getContent();
            symbolTableView.setItems(FXCollections.observableArrayList(symTable.entrySet()));

            // Populate ExeStack
            // If your getStack() returns a Stack, we usually iterate it
            // Adjust this based on your MyStack implementation (e.g. .getStack(), .getAll(), .getReversed())
            List<String> stackStr = new ArrayList<>();
            for (IStmt stmt : currentPrg.getExeStack().getContent()) {
                stackStr.add(stmt.toString());
            }
            // Usually we want the top of the stack at the top of the list
            // Collections.reverse(stackStr);
            executionStackListView.setItems(FXCollections.observableArrayList(stackStr));
        }
    }
}