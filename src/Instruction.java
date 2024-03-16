public abstract class Instruction {

    public boolean retired = false;
    boolean decoded = false;
    boolean executed = false;
    boolean writtenBack = false;

    int cyclesToExecute;

    String instructionUnit = "";

    public abstract Instruction copyOf();

}
