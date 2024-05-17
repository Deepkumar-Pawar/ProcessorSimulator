import java.util.ArrayList;

public class BranchUnit extends ExecutionUnit{
    public RegisterFile registerFile;
    public WriteBackUnit writeBackUnit;
    public FetchUnit fetchUnit;

    public CommitUnit commitUnit;

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

            if (current.cyclesToExecute > 1)
            {
                current.cyclesToExecute--;
                return programCounter;
            }

//            System.out.println(current);

            int forwarded1Index = checkForwardedResultRegisters(current.op1);
            int forwarded2Index = checkForwardedResultRegisters(current.op2);

            boolean hazardStall = false;

            int op1 = 0, op2 = 0;

            if (forwarded1Index != -1)
            {
                op1 = resultForwardingRegisters.get(forwarded1Index).getValue();

                if (checkDataDependency(current.op1, writeBackUnit))
                {
                    System.out.println("used forwarded register!!!!");
                }

            }
            else if (!checkDataDependency(current.op1, writeBackUnit))
            {
                op1 = registerFile.registers.get(current.op1).getValue();
            }
            else {
                hazardStall = true;
            }

            if (forwarded2Index != -1)
            {
                op2 = resultForwardingRegisters.get(forwarded2Index).getValue();

                if (checkDataDependency(current.op2, writeBackUnit))
                {
                    System.out.println("used forwarded register!!!!");
                }
            }
            else if (!checkDataDependency(current.op2, writeBackUnit))
            {
                op2 = registerFile.registers.get(current.op2).getValue();
            }
            else {
                hazardStall = true;
            }

            if (!hazardStall)
            {
                if (executeCycle(current))
                {
                    return programCounter;
                }
            }

            if (current.instructionType == "beq")
            {
                if(!hazardStall)
                {
                    current.taken = beq(op1, op2);

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);

                    programCounter = branchExecute(current, programCounter);
                }
            }
            else if (current.instructionType == "bne")
            {
                if(!hazardStall)
                {
                    current.taken = bne(op1, op2);

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);

                    programCounter = branchExecute(current, programCounter);
                }
            }
            else if (current.instructionType == "bgt")
            {
                if(!hazardStall)
                {
                    current.taken = bgt(op1, op2);

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);

                    programCounter = branchExecute(current, programCounter);
                }
            }
            else if (current.instructionType == "bge")
            {
                if(!hazardStall)
                {
                    current.taken = bge(op1, op2);

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);

                    programCounter = branchExecute(current, programCounter);
                }
            }
            else if (current.instructionType == "blt")
            {
                if(!hazardStall)
                {
                    current.taken = blt(op1, op2);

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);

                    programCounter = branchExecute(current, programCounter);
                }
            }
            else if (current.instructionType == "ble")
            {
                if(!hazardStall)
                {
                    current.taken = ble(op1, op2);

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);

                    programCounter = branchExecute(current, programCounter);
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

    public int branchExecute(ControlInstruction current, int programCounter)
    {
        current.executed = true;

        fetchUnit.branchStall = false;

        int targetProgramCounter = current.targetProgramCounter;

        current.retired = true;

//                    writeBackUnit.instructionsBuffer.add(current);

        if (current.taken)
        {
//                        System.out.println(targetProgramCounter);
            programCounter = targetProgramCounter;
        }
        else
        {
            programCounter++;
        }

        commitUnit.instructionsBuffer.add(current);

        instructions.remove(0);

        return programCounter;
    }

    public boolean beq(int op1, int op2)
    {
        boolean taken = op1 == op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return taken;
    }

    public boolean bne(int op1, int op2)
    {
        boolean taken = op1 != op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return taken;
    }

    public boolean bgt(int op1, int op2)
    {
        boolean taken = op1 > op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return taken;
    }

    public boolean bge(int op1, int op2)
    {
        boolean taken = op1 >= op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return taken;
    }

    public boolean blt(int op1, int op2)
    {
        boolean taken = op1 < op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return taken;
    }

    public boolean ble(int op1, int op2)
    {
        boolean taken = op1 <= op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return taken;
    }

    public void init (RegisterFile registerFile, FetchUnit fetchUnit, WriteBackUnit writeBackUnit, CommitUnit commitUnit)
    {
        this.registerFile = registerFile;
        this.fetchUnit = fetchUnit;
        this.writeBackUnit = writeBackUnit;
        this.commitUnit = commitUnit;
    }

//    public boolean checkDataDependency(int opRegName)
//    {
//        for (Instruction instruction : writeBackUnit.instructionsBuffer)
//        {
//            if (instruction.instructionUnit == "ALU")
//            {
//                if (((InstructionALU) instruction).destRegName == opRegName)
//                {
//                    return true;
//                }
//            }
//        }
//
//        for (Instruction instruction : writeBackUnit.instructions)
//        {
//            if (instruction.instructionUnit == "ALU")
//            {
//                if (((InstructionALU) instruction).destRegName == opRegName)
//                {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

//    public boolean checkForwardedResults(InstructionALU instruction)
//    {
//        boolean op1Forwarded = false;
//        boolean op2Forwarded = false;
//
//        for (Register register : resultForwardingRegisters)
//        {
//            if (register.name == instruction.op1)
//            {
//                op1Forwarded = true;
//            }
//
//            if (register.name == instruction.op2)
//            {
//                op2Forwarded = true;
//            }
//        }
//
//        return (op1Forwarded && op2Forwarded);
//    }

}
