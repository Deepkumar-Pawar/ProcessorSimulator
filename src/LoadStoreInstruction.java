import java.util.ArrayList;

public class LoadStoreInstruction  extends Instruction{
    int op1;
    int op2;
    int dest;

    String instructionType;

    public LoadStoreInstruction(int op1, int op2, int dest, String instructionType, int cyclesToExecute)
    {
        this.op1 = op1;
        this.op2 = op2;
        this.dest = dest;
        this.instructionType = instructionType;
        this.cyclesToExecute = cyclesToExecute;

        instructionUnit = "LoadStoreUnit";

        destRegs = new ArrayList<>();
        destRegs.add(dest);

        opRegs = new ArrayList<>();

        if (instructionType == "sw")
        {
            opRegs.add(op1);
        }
    }

    @Override
    public Instruction copyOf() {
        LoadStoreInstruction copy = new LoadStoreInstruction(op1, op2, dest, instructionType, cyclesToExecute);

        copy.retired = this.retired;
        copy.decoded = this.decoded;
        copy.executed = this.executed;
        copy.writtenBack = this.writtenBack;
        copy.instructionUnit = this.instructionUnit;
        copy.id = this.id;
        copy.destRegs = new ArrayList<>(this.destRegs);
        copy.opRegs = new ArrayList<>(this.opRegs);


        return copy;
    }
}
