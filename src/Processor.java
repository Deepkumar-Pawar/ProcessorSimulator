import java.util.ArrayList;

public class Processor {

    public int programCounter = 0;
    public boolean initialised = false;

    public RegisterFile registerFile;

    public FetchUnit fetchUnit;
    public DecodeUnit decodeUnit;
    public ArithmeticLogicUnit alu;
    public BranchUnit branchUnit;
    public WriteBackUnit writeBackUnit;

    public boolean running = false;

    public void init()
    {
        //initialise processor related units and memory units etc

        registerFile = new RegisterFile();

//        registerFile.registers.get(0).setValue(2);
//        registerFile.registers.get(1).setValue(3);

        fetchUnit = new FetchUnit();
        decodeUnit = new DecodeUnit();
        alu = new ArithmeticLogicUnit();
        branchUnit = new BranchUnit();
        writeBackUnit = new WriteBackUnit();

        fetchUnit.init(decodeUnit, registerFile);
        decodeUnit.init(alu, branchUnit);
        alu.init(registerFile, writeBackUnit);
        branchUnit.init(registerFile, fetchUnit, writeBackUnit);
        writeBackUnit.init(registerFile);

    }

    public void start()
    {
        //entry point for the processor program to "start"
        //call init
        //call tick in a loop

        if (!initialised)
        {
            init();
        }

    }

    public void start(ArrayList<Instruction> instructions)
    {

        running = true;

//        printInstructions(instructions);

        if (!initialised)
        {
            init();
        }

        fetchUnit.giveInstructions(instructions);

        int endOfProgram = instructions.size()-1;
//        System.out.println(endOfProgram);

        int cycles = 0;

        int i = 0;

        int maxCycles = 500;

        while (running)
        {
//            System.out.println("Cycle " + cycles);
//
//            printProcessorState();

            tick();
            cycles++;

            i++;

            if (i >= maxCycles)
                running = false;

//            System.out.println("A cycle occurred");

            if (programCounter > endOfProgram || fetchUnit.exited)
            {
                if (decodeUnit.instructions.isEmpty() && alu.instructions.isEmpty() && branchUnit.instructions.isEmpty() && writeBackUnit.instructions.isEmpty())
                {
                    if (decodeUnit.instructionsBuffer.isEmpty() && alu.instructionsBuffer.isEmpty() && writeBackUnit.instructionsBuffer.isEmpty()) {
                        running = false;
                    }
                }
            }
        }

        System.out.println("cycles: " + cycles);
//        printProcessorState();
        System.out.println("program counter at end: " + programCounter);

//        printInstructions(decodeUnit.instructions);
//        System.out.println(decodeUnit.instructions.isEmpty());
//        printInstructions(decodeUnit.instructionsBuffer);
//        printInstructions(alu.instructions);
//        printInstructions(alu.instructionsBuffer);
//        printInstructions(writeBackUnit.instructions);
//        printInstructions(writeBackUnit.instructionsBuffer);


        printRegisterFile(registerFile);

    }

    public void tick()
    {
        //tick through each unit and let them "execute" their respective functions

        programCounter = fetchUnit.fetch(programCounter);

        decodeUnit.decode();

        alu.execute();

        programCounter = branchUnit.execute(programCounter);

        writeBackUnit.writeBack();

    }

    public void printRegisterFile(RegisterFile registerFile)
    {
        System.out.println("Register File: (name: value)");

        for (Register register : registerFile.registers)
        {
            System.out.println(String.valueOf(register.name) + ": " + String.valueOf(register.getValue()));
        }
    }

    public void printInstructions(ArrayList<Instruction> instructions)
    {
        System.out.println(instructions);
//        for (Instruction instruction : instructions)
//        {
//            if (instruction.instructionUnit == "BranchUnit")
//            {
//                System.out.println(((ControlInstruction) instruction).op1);
//                System.out.println(((InstructionALU) instruction).op2);
//                System.out.println(((InstructionALU) instruction).destRegName);
//                System.out.println(((InstructionALU) instruction).instructionType);
//            }
//        }
    }

    public void printProcessorState()
    {
        int registerNum = 5;
        System.out.println("Register File: (name: value)");
        for (int i = 0; i < registerNum; i++)
        {
            System.out.println(registerFile.registers.get(i).name + ": " + registerFile.registers.get(i).getValue());
        }

        System.out.println("FetchUnit");
        if (fetchUnit.branchStall)
        {
            System.out.println("\tis currently stalled");
        }
        System.out.println("\tProgram Counter: " + programCounter);

        System.out.println("DecodeUnit");
        System.out.println("\tBuffer Instructions: " + decodeUnit.instructionsBuffer);
        System.out.println("\tInstructions: " + decodeUnit.instructions);

        System.out.println("ALU");
        System.out.println("\tBuffer Instructions: " + alu.instructionsBuffer);
        System.out.println("\tInstructions: " + alu.instructions);

        System.out.println("BranchUnit");
        System.out.println("\tBuffer Instructions: " + branchUnit.instructionsBuffer);
        System.out.println("\tInstructions: " + branchUnit.instructions);

        System.out.println("WriteBackUnit");
        System.out.println("\tBuffer Instructions: " + writeBackUnit.instructionsBuffer);
        System.out.println("\tInstructions: " + writeBackUnit.instructions);

    }

}
