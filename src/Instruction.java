public abstract class Instruction {

    public boolean retired;
    boolean decoded = false;
    boolean executed = false;
    boolean writtenBack = false;

    String instructionUnit = "";

}
