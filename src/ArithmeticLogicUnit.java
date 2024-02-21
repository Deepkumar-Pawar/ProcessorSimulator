import java.util.ArrayList;

public class ArithmeticLogicUnit extends ExecutionUnit {

    public RegisterFile registerFile;
    public WriteBackUnit writeBackUnit;

    public ArithmeticLogicUnit()
    {
        instructionsBuffer = new ArrayList<>();
        instructions = new ArrayList<>();
        resultForwardingRegisters = new ArrayList<>();
    }

    public void execute()
    {
        if (!instructions.isEmpty())
        {
            InstructionALU current = (InstructionALU) instructions.get(0);

            if(!checkDataDependency(current))
            {
                if (current.instructionType == "add")
                {
                    add(current);
                }

                current.executed = true;

                writeBackUnit.instructionsBuffer.add(current);

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

    public void add(InstructionALU instructionALU)
    {
        int op1 = registerFile.registers.get(instructionALU.op1RegName).getValue();
        int op2 = registerFile.registers.get(instructionALU.op2RegName).getValue();

        int result = op1 + op2;

        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        instructionALU.result = result;
    }

    public void init (RegisterFile registerFile, WriteBackUnit writeBackUnit)
    {
        this.registerFile = registerFile;
        this.writeBackUnit = writeBackUnit;
    }

    public boolean checkDataDependency(InstructionALU currentInstruction)
    {
        for (Instruction instruction : writeBackUnit.instructionsBuffer)
        {
            if (instruction.instructionUnit == "ALU")
            {
                if (((InstructionALU) instruction).destRegName == currentInstruction.op1RegName || ((InstructionALU) instruction).destRegName == currentInstruction.op2RegName)
                {
                    return true;
                }
            }
        }

        for (Instruction instruction : writeBackUnit.instructions)
        {
            if (instruction.instructionUnit == "ALU")
            {
                if (((InstructionALU) instruction).destRegName == currentInstruction.op1RegName || ((InstructionALU) instruction).destRegName == currentInstruction.op2RegName)
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
            if (register.name == instruction.op1RegName)
            {
                op1Forwarded = true;
            }

            if (register.name == instruction.op2RegName)
            {
                op2Forwarded = true;
            }
        }

        return (op1Forwarded && op2Forwarded);
    }

}
