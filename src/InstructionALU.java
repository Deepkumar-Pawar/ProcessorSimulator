public class InstructionALU extends Instruction {
    int op1RegName;
    int op2RegName;
    int destRegName;

    int result;

    String instructionType;

    public InstructionALU(int op1RN, int op2RN, int destRN, String instructionType)
    {
        op1RegName = op1RN;
        op2RegName = op2RN;
        destRegName = destRN;
        this.instructionType = instructionType;

        instructionUnit = "ALU";
    }
}
