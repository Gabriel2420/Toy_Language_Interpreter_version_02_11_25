package view;

import controller.Controller;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyDictionaryHeap;
import model.adt.MyList;
import model.adt.MyStack;
import model.expressions.*;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepo;
import repository.Repo;
import view.command.ExitCommand;
import view.command.RunExample;

import java.nio.channels.NonWritableChannelException;

public class Interpreter {
    public static void main(String[] args) {
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );

        PrgState prg1 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), ex1);
        IRepo repo1 = new Repo(prg1,"log1.txt");
        Controller controller1 = new Controller(repo1);

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

        PrgState prg2 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), ex2);
        IRepo repo2 = new Repo(prg2,"log2.txt");
        Controller controller2 = new Controller(repo2);

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

        PrgState prg3 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), ex3);
        IRepo repo3 = new Repo(prg3,"log3.txt");
        Controller controller3 = new Controller(repo3);

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

        PrgState prg4 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), testExample);
        IRepo repo4 = new Repo(prg4,"logTest.txt");
        Controller controller4 = new Controller(repo4);

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

        PrgState prg5 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), heapAllocExample);
        IRepo repo5 = new Repo(prg5,"log5.txt");
        Controller controller5 = new Controller(repo5);

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

        PrgState prg6 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), heapReadingExample);
        IRepo repo6 = new Repo(prg6, "log6.txt");
        Controller controller6 = new Controller(repo6);

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

        PrgState prg7 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), heapWritingExample);
        IRepo repo7 = new Repo(prg7, "log7.txt");
        Controller controller7 = new Controller(repo7);

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

        PrgState prg8 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), whileStmtExample);
        IRepo repo8 = new Repo(prg8, "log8.txt");
        Controller controller8 = new Controller(repo8);

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

        PrgState prg9 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(),garbageCollectorExample);
        IRepo repo9 = new Repo(prg9, "log9.txt");
        Controller controller9 = new Controller(repo9);

        IStmt deepRefGarbageCollector = new CompStmt(
                // 1. Declare the variables with increasing reference depth
                new VarDeclStmt("r1", new RefType(new IntType())),
                new CompStmt(
                        new VarDeclStmt("r2", new RefType(new RefType(new IntType()))),
                        new CompStmt(
                                new VarDeclStmt("r3", new RefType(new RefType(new RefType(new IntType())))),
                                new CompStmt(
                                        new VarDeclStmt("r4", new RefType(new RefType(new RefType(new RefType(new IntType()))))),
                                        new CompStmt(
                                                // 2. Build the chain in the Heap
                                                // Heap: Addr1(100)
                                                new NewStmt("r1", new ValueExp(new IntValue(100))),
                                                new CompStmt(
                                                        // Heap: Addr2 -> Addr1
                                                        new NewStmt("r2", new VarExp("r1")),
                                                        new CompStmt(
                                                                // Heap: Addr3 -> Addr2
                                                                new NewStmt("r3", new VarExp("r2")),
                                                                new CompStmt(
                                                                        // Heap: Addr4 -> Addr3
                                                                        new NewStmt("r4", new VarExp("r3")),
                                                                        new CompStmt(
                                                                                // 3. CHANGE r1, r2, r3 to point to something new.
                                                                                // Now, the old chain (Addr1, Addr2, Addr3) is ONLY held together
                                                                                // by 'r4' (which points to Addr4).
                                                                                new NewStmt("r1", new ValueExp(new IntValue(999))),
                                                                                new CompStmt(
                                                                                        new NewStmt("r2", new VarExp("r1")),
                                                                                        new CompStmt(
                                                                                                new NewStmt("r3", new VarExp("r2")),
                                                                                                new CompStmt(
                                                                                                        // 4. THE KILL SWITCH
                                                                                                        // We overwrite 'r4'. Now NOTHING points to the old Addr4.
                                                                                                        // Because Addr4 is gone, Addr3 is gone, etc.
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

        PrgState prg10 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyDictionaryHeap(), deepRefGarbageCollector);
        IRepo repo10 = new Repo(prg10, "log10.txt");
        Controller controller10 = new Controller(repo10);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0","exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), controller1));
        menu.addCommand(new RunExample("2", ex2.toString(), controller2));
        menu.addCommand(new RunExample("3", ex3.toString(), controller3));
        menu.addCommand(new RunExample("4", testExample.toString(), controller4));
        menu.addCommand(new RunExample("5", heapAllocExample.toString(), controller5));
        menu.addCommand(new RunExample("6", heapReadingExample.toString(), controller6));
        menu.addCommand(new RunExample("7", heapWritingExample.toString(), controller7));
        menu.addCommand(new RunExample("8", whileStmtExample.toString(), controller8));
        menu.addCommand(new RunExample("9", garbageCollectorExample.toString(), controller9));
        menu.addCommand(new RunExample("10", deepRefGarbageCollector.toString(), controller10));
        menu.show();
    }
}
