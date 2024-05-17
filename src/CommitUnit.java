import java.util.ArrayList;

public class CommitUnit {

    public ArrayList<Instruction> instructionsBuffer;
    public ArrayList<Instruction> instructions;

    public ROB rob;


    public CommitUnit(ROB rob)
    {
        instructionsBuffer = new ArrayList<>();
        instructions = new ArrayList<>();

        this.rob = rob;
    }


    public void commit()
    {
        if (!instructions.isEmpty())
        {
            Instruction current = instructions.get(0);

            rob.remove(current);
        }

        for (Instruction instruction: instructionsBuffer)
        {
            instructions.add(instruction);
        }

        instructionsBuffer.clear();
    }

}
