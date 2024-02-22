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
            
            if (current.instructionType == "add")
            {
                if(!checkDataDependency(current.op1) && !checkDataDependency(current.op2))
                {
                    add(current);
                    
                    current.executed = true;

                    writeBackUnit.instructionsBuffer.add(current);

                    instructions.remove(0);
                }
            }
            else if (current.instructionType == "addi")
            {
                if(!checkDataDependency(current.op1))
                {
                    addi(current);

                    current.executed = true;

                    writeBackUnit.instructionsBuffer.add(current);

                    instructions.remove(0);
                }
            }
            else if (current.instructionType == "mul")
            {
                if(!checkDataDependency(current.op1) && !checkDataDependency(current.op2))
                {
                    mul(current);

                    current.executed = true;

                    writeBackUnit.instructionsBuffer.add(current);

                    instructions.remove(0);
                }
            }
            else if (current.instructionType == "mod")
            {
                if(!checkDataDependency(current.op1) && !checkDataDependency(current.op2))
                {
                    mod(current);

                    current.executed = true;

                    writeBackUnit.instructionsBuffer.add(current);

                    instructions.remove(0);
                }
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
        int op1 = registerFile.registers.get(instructionALU.op1).getValue();
        int op2 = registerFile.registers.get(instructionALU.op2).getValue();

        int result = op1 + op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        instructionALU.result = result;
    }

    public void addi(InstructionALU instructionALU)
    {
        int op1 = registerFile.registers.get(instructionALU.op1).getValue();
        int op2 = instructionALU.op2;

        int result = op1 + op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        instructionALU.result = result;
    }

    public void mul(InstructionALU instructionALU)
    {
        int op1 = registerFile.registers.get(instructionALU.op1).getValue();
        int op2 = registerFile.registers.get(instructionALU.op2).getValue();

        int result = op1 * op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        instructionALU.result = result;
    }

    public void mod(InstructionALU instructionALU)
    {
        int op1 = registerFile.registers.get(instructionALU.op1).getValue();
        int op2 = registerFile.registers.get(instructionALU.op2).getValue();

        int result = op1 % op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        instructionALU.result = result;
    }

    public void init (RegisterFile registerFile, WriteBackUnit writeBackUnit)
    {
        this.registerFile = registerFile;
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
