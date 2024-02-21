import java.util.ArrayList;

public class Processor {

    public int programCounter = 0;
    public boolean initialised = false;

    public RegisterFile registerFile;

    public FetchUnit fetchUnit;
    public DecodeUnit decodeUnit;
    public ArithmeticLogicUnit alu;
    public WriteBackUnit writeBackUnit;

    public boolean running = false;

    public void init()
    {
        //initialise processor related units and memory units etc

        registerFile = new RegisterFile();

        registerFile.registers.get(0).setValue(2);
        registerFile.registers.get(1).setValue(3);

        fetchUnit = new FetchUnit();
        decodeUnit = new DecodeUnit();
        alu = new ArithmeticLogicUnit();
        writeBackUnit = new WriteBackUnit();

        fetchUnit.init(decodeUnit);
        decodeUnit.init(alu);
        alu.init(registerFile, writeBackUnit);
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

        int cycles = 0;

        int i = 0;

        while (running)
        {
//            System.out.println(programCounter);
            tick();
            cycles++;

            i++;
            if (i >= 10)
                running = false;

//            System.out.println("A cycle occurred");

            if (programCounter > endOfProgram)
            {
                if (decodeUnit.instructions.isEmpty() && alu.instructions.isEmpty() && writeBackUnit.instructions.isEmpty())
                {
                    if (decodeUnit.instructionsBuffer.isEmpty() && alu.instructionsBuffer.isEmpty() && writeBackUnit.instructionsBuffer.isEmpty()) {
                        running = false;
                    }
                }
            }
        }

        System.out.println("cycles: " + cycles);
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
//            if (instruction.instructionUnit == "ALU")
//            {
//                System.out.println(((InstructionALU) instruction).op1RegName);
//                System.out.println(((InstructionALU) instruction).op2RegName);
//                System.out.println(((InstructionALU) instruction).destRegName);
//                System.out.println(((InstructionALU) instruction).instructionType);
//            }
//        }
    }

}
