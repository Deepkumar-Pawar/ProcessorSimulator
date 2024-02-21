import java.util.ArrayList;

public class DecodeUnit implements Unit {

    public ArrayList<Instruction> instructionsBuffer;
    public ArrayList<Instruction> instructions;

    public ArithmeticLogicUnit alu;

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

            if(current.instructionUnit == "ALU")
            {
                alu.instructionsBuffer.add(current);
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

    public void init(ArithmeticLogicUnit alu)
    {
        this.alu = alu;
    }
}
