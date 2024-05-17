import java.util.List;

public abstract class Instruction {


    public int id;

    public boolean retired = false;
    boolean decoded = false;
    boolean executed = false;
    boolean writtenBack = false;

    int cyclesToExecute;

    String instructionUnit = "";

    public abstract Instruction copyOf();

    public List<Integer> opRegs;

    public List<Integer> destRegs;

    public void updateID(int id)
    {
        this.id = id;
    }

}
