import java.util.ArrayList;

public class CommitUnit {

    public ArrayList<Instruction> instructionsBuffer;
    public ArrayList<Instruction> instructions;

    public ROB rob;

    public RegisterFile registerFile;

    public Memory memory;


    public CommitUnit()
    {
        instructionsBuffer = new ArrayList<>();
        instructions = new ArrayList<>();
    }

    public void init(ROB rob, RegisterFile registerFile, Memory memory)
    {
        this.rob = rob;
        this.registerFile = registerFile;
        this.memory = memory;
    }


    public void commit()
    {
        if (!instructions.isEmpty())
        {
            Instruction current = instructions.get(0);

            ROBEntry robHead = rob.head();

            if (canCommit(robHead.instruction.id))
            {
                // commit that instruction

//                if (robHead.changesMemory)
//                {
//                    memory.store(robHead.memoryAddress, robHead.data);
//                }
//                else if (robHead.changesRegistry)
//                {
//                    registerFile.registers.get(robHead.destReg).setValue(robHead.data);
//                }

                rob.removeHead();
            }
        }

        for (Instruction instruction: instructionsBuffer)
        {
            instructions.add(instruction);
        }

        instructionsBuffer.clear();
    }

    public boolean canCommit(int robHeadId)
    {
        for (int i = 0; i < instructions.size(); i++)
        {
            if (instructions.get(i).id == robHeadId)
            {
                instructions.remove(i);
                return true;
            }
        }

        return false;
    }

}
