package view;

import controller.Controller;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyList;
import model.adt.MyStack;
import model.statements.IStmt;
import repository.IRepo;
import repository.Repo;
import exceptions.MyException;

import java.util.Scanner;

public class View {
    private final Scanner scanner;
    private IStmt selectedProgram;

    public View() {
        this.scanner = new Scanner(System.in);
        this.selectedProgram = null;
    }

    public void menu() {
        while (true) {
            System.out.println("===== MENU =====");
            System.out.println("1. Select a program");
            System.out.println("2. Execute program");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1" -> selectProgram();
                case "2" -> executeProgram();
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again!");
            }
        }
    }

    private void selectProgram() {
        System.out.println("Select a program:");
        System.out.println("1. int v; v=2; Print(v)");
        System.out.println("2. int a; int b; a=2+3*5; b=a+1; Print(b)");
        System.out.println("3. bool a; int v; a=true; If a Then v=2 Else v=3; Print(v)");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine();
        this.selectedProgram = switch (choice) {
            case "1" -> Examples.getEx1();
            case "2" -> Examples.getEx2();
            case "3" -> Examples.getEx3();
            default -> {
                System.out.println("Invalid choice. Defaulting to program 1.");
                yield Examples.getEx1();
            }
        };

        System.out.println("Program selected!");
    }

    private void executeProgram() {
        if (this.selectedProgram == null) {
            System.out.println("No program selected! Please select one first.");
            return;
        }

        System.out.println("Executing program...");
        try {
            PrgState prg = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    this.selectedProgram
            );

            IRepo repo = new Repo();
            repo.addPrg(prg);

            Controller controller = new Controller(repo);
            controller.allStep();
            System.out.println("Execution finished!");
        } catch (MyException e) {
            System.out.println("Error during execution: " + e.getMessage());
        }
    }
}
