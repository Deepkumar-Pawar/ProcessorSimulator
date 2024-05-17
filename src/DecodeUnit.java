import java.util.ArrayList;
import java.util.List;

public class DecodeUnit implements Unit {

    public ArrayList<Instruction> instructionsBuffer;
    public ArrayList<Instruction> instructions;

    public ArithmeticLogicUnit alu;
    public BranchUnit branchUnit;
    public LoadStoreUnit loadStoreUnit;

    public ROB rob;

    public ReservationStations reservationStations;

    public int aluInstructionsCounter = 0;
    public int controlInstructionsCounter = 0;
    public int loadStoreInstructionsCounter = 0;

    public DecodeUnit()
    {
        instructionsBuffer = new ArrayList<>();
        instructions = new ArrayList<>();
    }

    public void decode()
    {
        //for instruction to decode, set as decoded and add it to appropriate execution unit

        List<Integer> instructionDestRegs;

        if (!instructions.isEmpty())
        {
            Instruction current = instructions.get(0);

//            System.out.println(current);

            if(current.instructionUnit == "ALU")
            {
                aluInstructionsCounter++;

                reservationStations.aluReservationStation.add(current);

                //pre reservation stations
//                alu.instructionsBuffer.add(current);

//                System.out.println("decoder decoded an alu instruction");

            }
            else if (current.instructionUnit == "BranchUnit")
            {
                controlInstructionsCounter++;

                reservationStations.buReservationStation.add(current);

//                System.out.println("decoder decoded a branch");
                //pre reservation stations
//                branchUnit.instructionsBuffer.add(current);
            }
            else if (current.instructionUnit == "LoadStoreUnit")
            {
                loadStoreInstructionsCounter++;

                reservationStations.lsuReservationStation.add(current);

//                System.out.println("decoder decoded a branch");
                //pre reservation stations
//                loadStoreUnit.instructionsBuffer.add(current);
            }

            //adding to ROB
            instructionDestRegs = new ArrayList<>(current.destRegs);

            rob.add(current, instructionDestRegs);
            //

            current.decoded = true;

            instructions.remove(0);
        }

        for (Instruction instruction: instructionsBuffer)
        {
            instructions.add(instruction);
        }

        instructionsBuffer.clear();
    }

    public void issue(ExecutionUnit executionUnit, Instruction instruction)
    {
//        executionUnit.instructionsBuffer.add(instruction)
    }

    public void init(ArithmeticLogicUnit alu, BranchUnit branchUnit, LoadStoreUnit loadStoreUnit, ROB rob, ReservationStations reservationStations)
    {
        this.alu = alu;
        this.branchUnit = branchUnit;
        this.loadStoreUnit = loadStoreUnit;
        this.rob = rob;
        this.reservationStations = reservationStations;
    }
}
