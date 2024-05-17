import java.util.ArrayList;

public class ControlInstruction extends Instruction{
    int op1;
    int op2;
    int targetProgramCounter;

    String targetLabel;

    boolean taken;

    boolean isJumpInstruction;

    String instructionType;

    public ControlInstruction(int op1RN, int op2RN, String targetL, String instructionType, boolean isJumpInstruction, int cyclesToExecute)
    {
        op1 = op1RN;
        op2 = op2RN;
        targetLabel = targetL;
        this.instructionType = instructionType;
        this.isJumpInstruction = isJumpInstruction;
        this.cyclesToExecute = cyclesToExecute;

        instructionUnit = "BranchUnit";

        this.destRegs = new ArrayList<>();
    }

    @Override
    public Instruction copyOf() {
        ControlInstruction copy = new ControlInstruction(op1, op2, targetLabel, instructionType, isJumpInstruction, cyclesToExecute);

        copy.retired = this.retired;
        copy.decoded = this.decoded;
        copy.executed = this.executed;
        copy.writtenBack = this.writtenBack;
        copy.instructionUnit = this.instructionUnit;
        copy.targetProgramCounter = this.targetProgramCounter;

        return copy;
    }
}
