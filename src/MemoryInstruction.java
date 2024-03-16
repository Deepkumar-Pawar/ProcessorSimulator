public class MemoryInstruction extends Instruction{
    int op1;
    int op2;
    int dest;

    String instructionType;

    public MemoryInstruction(int op1, int op2, int dest, String instructionType, int cyclesToExecute)
    {
        this.op1 = op1;
        this.op2 = op2;
        this.instructionType = instructionType;
        this.cyclesToExecute = cyclesToExecute;

        instructionUnit = "MemoryUnit";
    }

    @Override
    public Instruction copyOf() {
        MemoryInstruction copy = new MemoryInstruction(op1, op2, dest, instructionType, cyclesToExecute);

        copy.retired = this.retired;
        copy.decoded = this.decoded;
        copy.executed = this.executed;
        copy.writtenBack = this.writtenBack;
        copy.instructionUnit = this.instructionUnit;

        return copy;
    }
}
