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

                if(!hazardStall)
                {

                    if (executeCycle(current))
                    {
                        return;
                    }

                    current.result = add(op1, op2);
                    
                    current.executed = true;

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);
                    cleanForwardedResultRegisters(current.destRegName);
                    addForwardedResultRegister(current.destRegName, current.result);

                    writeBackUnit.instructionsBuffer.add(current);

                    instructions.remove(0);
                }
            }
            else if (current.instructionType == "addi")
            {
                int forwarded1Index = checkForwardedResultRegisters(current.op1);

                boolean hazardStall = false;

                int op1 = 0, op2 = current.op2;

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

                if(!hazardStall)
                {

                    if (executeCycle(current))
                    {
                        return;
                    }

                    current.result = addi(op1, op2);

                    current.executed = true;

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.destRegName);
                    addForwardedResultRegister(current.destRegName, current.result);

                    writeBackUnit.instructionsBuffer.add(current);

                    instructions.remove(0);
                }
            }
            else if (current.instructionType == "mul")
            {
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

                if(!hazardStall)
                {

                    if (executeCycle(current))
                    {
                        return;
                    }

                    current.result = mul(op1, op2);

                    current.executed = true;

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);
                    cleanForwardedResultRegisters(current.destRegName);
                    addForwardedResultRegister(current.destRegName, current.result);

                    writeBackUnit.instructionsBuffer.add(current);

                    instructions.remove(0);
                }
            }
            else if (current.instructionType == "mod")
            {
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

                if(!hazardStall)
                {

                    if (executeCycle(current))
                    {
                        return;
                    }

                    current.result = mod(op1, op2);

                    current.executed = true;

                    cleanForwardedResultRegisters(current.op1);
                    cleanForwardedResultRegisters(current.op2);
                    cleanForwardedResultRegisters(current.destRegName);
                    addForwardedResultRegister(current.destRegName, current.result);

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

    public int add(int op1, int op2)
    {

        int result = op1 + op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

//        instructionALU.result = result;
        return result;
    }

    public int addi(int op1, int op2)
    {

        int result = op1 + op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return result;
    }

    public int mul(int op1, int op2)
    {


        int result = op1 * op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return result;
    }

    public int mod(int op1, int op2)
    {

        int result = op1 % op2;

//        resultForwardingRegisters.add(new Register(instructionALU.destRegName));

        return result;
    }

    public void init (RegisterFile registerFile, WriteBackUnit writeBackUnit)
    {
        this.registerFile = registerFile;
        this.writeBackUnit = writeBackUnit;
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

//    public boolean checkForwardedResults(int op1, int op2)
//    {
//        boolean op1Forwarded = false;
//        boolean op2Forwarded = false;
//
//        for (Register register : resultForwardingRegisters)
//        {
//            if (register.name == op1)
//            {
//                op1Forwarded = true;
//            }
//
//            if (register.name == op2)
//            {
//                op2Forwarded = true;
//            }
//        }
//
//        return (op1Forwarded && op2Forwarded);
//    }

}
