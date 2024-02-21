import java.util.ArrayList;

public class BranchUnit extends ExecutionUnit{
    public RegisterFile registerFile;
    public WriteBackUnit writeBackUnit;
    public FetchUnit fetchUnit;

    public BranchUnit()
    {
        instructionsBuffer = new ArrayList<>();
        instructions = new ArrayList<>();
        resultForwardingRegisters = new ArrayList<>();
    }

    public int execute(int programCounter)
    {

//        System.out.print(instructionsBuffer);
//        System.out.println(instructions);

        if (!instructions.isEmpty())
        {
            ControlInstruction current = (ControlInstruction) instructions.get(0);

//            System.out.println(current);

            if (current.instructionType == "beq")
            {
                if(!checkDataDependency(current.op1) && !checkDataDependency(current.op2))
                {
                    beq(current);

                    current.executed = true;

                    fetchUnit.branchStall = false;

                    int targetProgramCounter = current.targetProgramCounter;

                    current.retired = true;

//                    writeBackUnit.instructionsBuffer.add(current);

                    instructions.remove(0);

                    if (current.taken)
                    {
                        programCounter = targetProgramCounter;
                    }
                }
            }
        }



        //should place buffer instructions in the list of instructions to execute as the thing this unit does
        for (Instruction instruction: instructionsBuffer)
        {
            instructions.add(instruction);
        }

        instructionsBuffer.clear();

        return programCounter;
    }

    public void beq(ControlInstruction controlInstruction)
    {
        int op1 = registerFile.registers.get(controlInstruction.op1).getValue();
        int op2 = registerFile.registers.get(controlInstruction.op2).getValue();

        boolean taken = op1 == op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        controlInstruction.taken = taken;
    }

    public void init (RegisterFile registerFile, FetchUnit fetchUnit, WriteBackUnit writeBackUnit)
    {
        this.registerFile = registerFile;
        this.fetchUnit = fetchUnit;
        this.writeBackUnit = writeBackUnit;
    }

    public boolean checkDataDependency(int opRegName)
    {
        for (Instruction instruction : writeBackUnit.instructionsBuffer)
        {
            if (instruction.instructionUnit == "ALU")
            {
                if (((InstructionALU) instruction).destRegName == opRegName)
                {
                    return true;
                }
            }
        }

        for (Instruction instruction : writeBackUnit.instructions)
        {
            if (instruction.instructionUnit == "ALU")
            {
                if (((InstructionALU) instruction).destRegName == opRegName)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkForwardedResults(InstructionALU instruction)
    {
        boolean op1Forwarded = false;
        boolean op2Forwarded = false;

        for (Register register : resultForwardingRegisters)
        {
            if (register.name == instruction.op1)
            {
                op1Forwarded = true;
            }

            if (register.name == instruction.op2)
            {
                op2Forwarded = true;
            }
        }

        return (op1Forwarded && op2Forwarded);
    }

}
