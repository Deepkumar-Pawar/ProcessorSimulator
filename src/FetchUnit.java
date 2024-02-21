import java.util.ArrayList;
import java.util.List;

public class FetchUnit implements Unit {

    public ArrayList<Instruction> instructions;

    public boolean branchStall = false;

//    public int programCounter;

    public DecodeUnit decodeUnit;

    public int fetch(int programCounter)
    {
        if (programCounter >= instructions.size() || branchStall)
        {
            return programCounter;
        }

        Instruction fetched = instructions.get(programCounter);

        if (!(fetched.instructionUnit == "BranchUnit"))
        {
            programCounter++;
        }
        else
        {
            branchStall = true;
        }

        decodeUnit.instructionsBuffer.add(fetched.copyOf());


        return programCounter;

        //add logic to update program counter
        //maybe add a small decoder that checks if this is a branch and updates program counter accordingly
    }

    public void init(DecodeUnit decodeUnit)
    {
        this.decodeUnit = decodeUnit;
//        this.instructions = new ArrayList<>();
    }

    public void giveInstructions (ArrayList<Instruction> instructions)
    {
        this.instructions = new ArrayList<>(instructions);
    }
}
