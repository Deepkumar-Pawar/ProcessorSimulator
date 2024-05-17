import java.util.List;
import java.util.ArrayList;

public class Processor {

    public int programCounter = 0;
    public boolean initialised = false;

    public int width = 4;

    public RegisterFile registerFile;
    public int memorySize = 1000;
    public Memory memory;

    public FetchUnit fetchUnit;
    public DecodeUnit decodeUnit;

    public int aluNum = 3;
    public int buNum = 2;
    public int lsuNum = 2;

    public List<ArithmeticLogicUnit> alus;
    public List<BranchUnit> branchUnits;
    public List<LoadStoreUnit> loadStoreUnits;
//    public BranchUnit branchUnit;
//    public LoadStoreUnit loadStoreUnit;
    public WriteBackUnit writeBackUnit;

    public CommitUnit commitUnit;

    public List<List<Instruction>> instructionLocations;

    public ROB rob;

    public ReservationStations reservationStations;

    public boolean running = false;

    public void init()
    {
        //initialise processor related units and memory units etc

        registerFile = new RegisterFile();
        memory = new Memory(memorySize);



//        registerFile.registers.get(0).setValue(2);
//        registerFile.registers.get(1).setValue(3);

        rob = new ROB();

        reservationStations = new ReservationStations();

        fetchUnit = new FetchUnit();
        decodeUnit = new DecodeUnit();

        alus = new ArrayList<>();
        branchUnits = new ArrayList<>();
        loadStoreUnits = new ArrayList<>();

        for (int i = 0; i < aluNum; i++)
        {
            alus.add(new ArithmeticLogicUnit());
        }

        for (int i = 0; i < buNum; i++)
        {
            branchUnits.add(new BranchUnit());
        }

        for (int i = 0; i < lsuNum; i++)
        {
            loadStoreUnits.add(new LoadStoreUnit());
        }

        writeBackUnit = new WriteBackUnit();
        commitUnit = new CommitUnit();



        fetchUnit.init(decodeUnit, registerFile);
        decodeUnit.init(rob, reservationStations);

        for (int i = 0; i < alus.size(); i++)
        {
            alus.get(i).init(registerFile, writeBackUnit, reservationStations, rob);
        }

        for (int i = 0; i < branchUnits.size(); i++)
        {
            branchUnits.get(i).init(registerFile, fetchUnit, writeBackUnit, commitUnit, reservationStations, rob);
        }

        for (int i = 0; i < loadStoreUnits.size(); i++)
        {
            loadStoreUnits.get(i).init(registerFile, memory, writeBackUnit, commitUnit, rob, reservationStations);
        }


        writeBackUnit.init(registerFile, commitUnit, rob);
        commitUnit.init(rob, registerFile, memory);

        instructionLocations = new ArrayList<>();
        instructionLocations.add(decodeUnit.instructions);
        instructionLocations.add(decodeUnit.instructionsBuffer);

//        instructionLocations.add(alu1.instructions);
//        instructionLocations.add(alu2.instructions);
//        instructionLocations.add(alu3.instructions);

        for (int i = 0; i < alus.size(); i++)
        {
            instructionLocations.add(alus.get(i).instructions);
        }

        for (int i = 0; i < branchUnits.size(); i++)
        {
            instructionLocations.add(branchUnits.get(i).instructions);
        }

        for (int i = 0; i < loadStoreUnits.size(); i++)
        {
            instructionLocations.add(loadStoreUnits.get(i).instructions);
        }


//        instructionLocations.add(branchUnit.instructions);
//        instructionLocations.add(loadStoreUnit.instructions);
        instructionLocations.add(writeBackUnit.instructions);
        instructionLocations.add(writeBackUnit.instructionsBuffer);
        instructionLocations.add(commitUnit.instructions);
        instructionLocations.add(commitUnit.instructionsBuffer);



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

        int maxCycles = 10000;

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

            if (cycles == 2000)
            {
                System.out.print("");   //Do nothing just for debugging to see what's up at arbitrary cycle number
            }

//            System.out.println("A cycle occurred");

            if (programCounter > endOfProgram || fetchUnit.exited)
            {
                boolean allEmpty = true;

                for (List<Instruction> instructionList : instructionLocations)
                {
                    if (!instructionList.isEmpty())
                    {
                        allEmpty = false;
                    }
                }
                if (allEmpty)
                {
                    running = false;
                }
//                if (decodeUnit.instructions.isEmpty() && alu.instructions.isEmpty() && loadStoreUnit.instructions.isEmpty() && branchUnit.instructions.isEmpty() && writeBackUnit.instructions.isEmpty() && commitUnit.instructions.isEmpty())
//                {
//                    if (decodeUnit.instructionsBuffer.isEmpty() && alu.instructionsBuffer.isEmpty() && loadStoreUnit.instructionsBuffer.isEmpty() && writeBackUnit.instructionsBuffer.isEmpty() && commitUnit.instructionsBuffer.isEmpty()) {
//                        running = false;
//                    }
//                }
            }
        }

        //stats to print
        System.out.println("Statistics:");
        System.out.println("cycles: " + cycles);
        int instructionsNum = fetchUnit.instructionCounter;
        System.out.println("number of instructions executed: " + instructionsNum);
        double instructionsPerCycle = ((double) Math.round(((double) instructionsNum) / ((double) cycles) * 1000)) / 1000;
        System.out.println("instructions per cycle: " + instructionsPerCycle);
//        printProcessorState();
        System.out.println("program counter at end: " + programCounter);
        System.out.println();

//        printInstructions(decodeUnit.instructions);
//        System.out.println(decodeUnit.instructions.isEmpty());
//        printInstructions(decodeUnit.instructionsBuffer);
//        printInstructions(alu.instructions);
//        printInstructions(alu.instructionsBuffer);
//        printInstructions(writeBackUnit.instructions);
//        printInstructions(writeBackUnit.instructionsBuffer);






        printRegisterFile(registerFile);
        System.out.println();
        printMemory(memory);

    }

    public void tick()
    {
        //tick through each unit and let them "execute" their respective functions





        for (int i = 0; i < width; i++)
        {
            programCounter = fetchUnit.fetch(programCounter);
            decodeUnit.decode();
        }



        for (int i = 0; i < alus.size(); i++)
        {
            alus.get(i).execute();
        }

        for (int i = 0; i < branchUnits.size(); i++)
        {
            programCounter = branchUnits.get(i).execute(programCounter);
        }

        for (int i = 0; i < loadStoreUnits.size(); i++)
        {
            loadStoreUnits.get(i).execute();;
        }




        for (int i = 0; i < width; i++)
        {
            writeBackUnit.writeBack();

            commitUnit.commit();
        }



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
//        System.out.println("\tBuffer Instructions: " + alu1.instructionsBuffer);
//        System.out.println("\tInstructions: " + alu1.instructions);
//        System.out.println("\tBuffer Instructions: " + alu2.instructionsBuffer);
//        System.out.println("\tInstructions: " + alu2.instructions);
//        System.out.println("\tBuffer Instructions: " + alu3.instructionsBuffer);
//        System.out.println("\tInstructions: " + alu3.instructions);
//
//        System.out.println("BranchUnit");
//        System.out.println("\tBuffer Instructions: " + branchUnit.instructionsBuffer);
//        System.out.println("\tInstructions: " + branchUnit.instructions);
//
//        System.out.println("LoadStoreUnit");
//        System.out.println("\tBuffer Instructions: " + loadStoreUnit.instructionsBuffer);
//        System.out.println("\tInstructions: " + loadStoreUnit.instructions);

        System.out.println("WriteBackUnit");
        System.out.println("\tBuffer Instructions: " + writeBackUnit.instructionsBuffer);
        System.out.println("\tInstructions: " + writeBackUnit.instructions);

    }

    public void printMemory(Memory memory)
    {

        System.out.println("Main Memory (length = " + memorySize + " (configurable)):\nPairwise (index : value)");
        List<Integer> memoryItems = new ArrayList<>();

        int n = memory.size;



        for (int i = 0; i < n; i++)
        {
            memoryItems.add(memory.load(i));
            System.out.print("("+ Integer.toString(i) + " : " + memoryItems.get(i).toString() + ")");
//            if (i <= 300) {
//                if ((isPrime(i) && memoryItems.get(i) == 0) || (!isPrime(i) && memoryItems.get(i) == 1)) {
//                    System.out.println(i + "WRONG" + memoryItems.get(i));
//                }
//            }

        }
        System.out.println();
        System.out.println("As a whole, sequentially:");

        System.out.println(memoryItems);

    }

//    boolean isPrime(int num)
//    {
//        if(num<=1)
//        {
//            return false;
//        }
//        for(int i=2;i<=num/2;i++)
//        {
//            if((num%i)==0)
//                return  false;
//        }
//        return true;
//    }


}
