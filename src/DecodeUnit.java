import java.util.ArrayList;

public class DecodeUnit implements Unit {

    public ArrayList<Instruction> instructionsBuffer;
    public ArrayList<Instruction> instructions;

    public ArithmeticLogicUnit alu;
    public BranchUnit branchUnit;

    public DecodeUnit()
    {
        instructionsBuffer = new ArrayList<>();
        instructions = new ArrayList<>();
    }

    public void decode()
    {
        //for instruction to decode, set as decoded and add it to appropriate execution unit

        if (!instructions.isEmpty())
        {
            Instruction current = instructions.get(0);

//            System.out.println(current);

            if(current.instructionUnit == "ALU")
            {
                alu.instructionsBuffer.add(current);
//                System.out.println("decoder decoded an alu instruction");
            }
            else if (current.instructionUnit == "BranchUnit")
            {
//                System.out.println("decoder decoded a branch");
                branchUnit.instructionsBuffer.add(current);
            }

            current.decoded = true;

            instructions.remove(0);
        }

        for (Instruction instruction: instructionsBuffer)
        {
            instructions.add(instruction);
        }

        instructionsBuffer.clear();
    }

    public void init(ArithmeticLogicUnit alu, BranchUnit branchUnit)
    {
        this.alu = alu;
        this.branchUnit = branchUnit;
    }
}
