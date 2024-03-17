import java.util.ArrayList;

public class LoadStoreUnit extends ExecutionUnit {

    public RegisterFile registerFile;
    public Memory memory;

    public WriteBackUnit writeBackUnit;

    public LoadStoreUnit()
    {
        instructionsBuffer = new ArrayList<>();
        instructions = new ArrayList<>();
        resultForwardingRegisters = new ArrayList<>();
    }

    public void execute()
    {

        if (!instructions.isEmpty()) {
            LoadStoreInstruction current = (LoadStoreInstruction) instructions.get(0);

            if (current.instructionType == "lw") {
                int forwarded1Index = checkForwardedResultRegisters(current.op1);

                boolean hazardStall = false;

                int op1 = 0, op2 = current.op2;

                if (forwarded1Index != -1) {
                    op1 = resultForwardingRegisters.get(forwarded1Index).getValue();

                    if (checkDataDependency(current.op1, writeBackUnit)) {
                        System.out.println("used forwarded register!!!!");
                    }

                } else if (!checkDataDependency(current.op1, writeBackUnit)) {
                    op1 = registerFile.registers.get(current.op1).getValue();
                } else {
                    hazardStall = true;
                }

                if (!hazardStall) {

                    if (executeCycle(current)) {
                        return;
                    }

                    int address = op1 + op2;

                    int loaded = lw(address);

                    registerFile.registers.get(current.dest).setValue(loaded);

                    current.executed = true;

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.dest);

                    instructions.remove(0);
                }
            } else if (current.instructionType == "sw") {

                int forwarded1Index = checkForwardedResultRegisters(current.op1);
                int forwarded2Index = checkForwardedResultRegisters(current.dest);

                boolean hazardStall = false;

                int op1 = 0, destVal = 0;

                if (forwarded1Index != -1) {
                    op1 = resultForwardingRegisters.get(forwarded1Index).getValue();

                    if (checkDataDependency(current.op1, writeBackUnit)) {
                        System.out.println("used forwarded register!!!!");
                    }
                } else if (!checkDataDependency(current.op1, writeBackUnit)) {
                    op1 = registerFile.registers.get(current.op1).getValue();
                } else {
                    hazardStall = true;
                }

                if (forwarded2Index != -1) {
                    destVal = resultForwardingRegisters.get(forwarded2Index).getValue();

                    if (checkDataDependency(current.dest, writeBackUnit)) {
                        System.out.println("used forwarded register!!!!");
                    }
                } else if (!checkDataDependency(current.dest, writeBackUnit)) {
                    destVal = registerFile.registers.get(current.dest).getValue();
                } else {
                    hazardStall = true;
                }

                if (!hazardStall) {

                    if (executeCycle(current)) {
                        return;
                    }

                    int address = current.op2 + destVal;
                    int data = op1;

                    sw(address, data);

                    current.executed = true;

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.dest);

                    instructions.remove(0);
                }
            } else if (current.instructionType == "li") {

                if (executeCycle(current)) {
                    return;
                }

                li(current.op1, current.dest);

                current.executed = true;

                instructions.remove(0);
            }
        }



        //should place buffer instructions in the list of instructions to execute as the thing this unit does
        for (Instruction instruction: instructionsBuffer)
        {
            instructions.add(instruction);
        }

        instructionsBuffer.clear();
    }

    public int lw(int address)
    {
        return memory.load(address);
    }

    public void sw(int address, int data)
    {
        memory.store(address, data);
    }

    public void li(int op1, int dest)
    {
        registerFile.registers.get(dest).setValue(op1);
    }

    public void init (RegisterFile registerFile, Memory memory, WriteBackUnit writeBackUnit)
    {
        this.registerFile = registerFile;
        this.memory = memory;
        this.writeBackUnit = writeBackUnit;
    }
}
