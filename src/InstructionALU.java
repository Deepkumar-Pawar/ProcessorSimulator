public class InstructionALU extends Instruction {
    int op1;
    int op2;
    int destRegName;

    int result;

    String instructionType;

    public InstructionALU(int op1RN, int op2RN, int destRN, String instructionType, int cyclesToExecute)
    {
        op1 = op1RN;
        op2 = op2RN;
        destRegName = destRN;
        this.instructionType = instructionType;
        this.cyclesToExecute = cyclesToExecute;

        instructionUnit = "ALU";
    }

    @Override
    public Instruction copyOf()
    {
        InstructionALU copy = new InstructionALU(op1, op2, destRegName, instructionType, cyclesToExecute);

        copy.retired = this.retired;
        copy.decoded = this.decoded;
        copy.executed = this.executed;
        copy.writtenBack = this.writtenBack;
        copy.instructionUnit = this.instructionUnit;

        return copy;
    }
}
