package view;

import controller.Controller;
import exceptions.MyException;
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
import view.command.ExitCommand;
import view.command.RunExample;


public class Interpreter {
    public static void main(String[] args) {
        TextMenu menu = new TextMenu();
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
        addProgram(menu, "1", ex1, "log1.txt");

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
        addProgram(menu, "2", ex2, "log2.txt");

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
        addProgram(menu, "3", ex3, "log3.txt");

        IStmt testExample = new CompStmt(
                new VarDeclStmt("varf", new StringType()), // string varf;
                new CompStmt(
                        new AssignStmt("varf", new ValueExp(new StringValue("C:\\Users\\Gabriel Nasui\\InteliJProjects\\Lab3\\src\\test.in"))), // varf="test.in";
                        new CompStmt(
                                new OpenRFile(new VarExp("varf")), // openRFile(varf);
                                new CompStmt(
                                        new VarDeclStmt("varc", new IntType()), // int varc;
                                        new CompStmt(
                                                new ReadFile(new VarExp("varf"), "varc"), // readFile(varf,varc);
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("varc")), // print(varc);
                                                        new CompStmt(
                                                                new ReadFile(new VarExp("varf"), "varc"), // readFile(varf,varc);
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("varc")), // print(varc);
                                                                        new CloseRFile(new VarExp("varf")) // closeRFile(varf)
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        addProgram(menu, "4", testExample, "logTest.txt");

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
        addProgram(menu, "5", heapAllocExample, "log5.txt");

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
        addProgram(menu, "6", heapReadingExample, "log6.txt");

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
        addProgram(menu, "7", heapWritingExample, "log7.txt");

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
        addProgram(menu, "8", whileStmtExample, "log8.txt");

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
        addProgram(menu, "9", garbageCollectorExample, "log9.txt");

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
        addProgram(menu, "10", deepRefGarbageCollector, "log10.txt");

        menu.addCommand(new ExitCommand("0","exit"));
        menu.show();
    }

    private static void addProgram(TextMenu menu, String key, IStmt stmt, String logFilePath) {
        try {
            MyDictionary<String, IType> typeEnv = new MyDictionary<>();
            stmt.typecheck(typeEnv);

            PrgState prg = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), stmt);
            IRepo repo = new Repo(prg, logFilePath);
            Controller controller = new Controller(repo);

            menu.addCommand(new RunExample(key, stmt.toString(), controller));
        } catch (MyException | RuntimeException e) {
            System.out.println("------------------------------------------------------");
            System.out.println("ERROR: Example " + key + " failed type checking!");
            System.out.println("Reason: " + e.getMessage());
            System.out.println("------------------------------------------------------");
        }
    }
}
