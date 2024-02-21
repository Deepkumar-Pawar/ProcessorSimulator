import java.util.ArrayList;

public class WriteBackUnit implements  Unit {
    public ArrayList<Instruction> instructionsBuffer;
    public ArrayList<Instruction> instructions;

    public RegisterFile registerFile;

    public WriteBackUnit()
    {
        instructionsBuffer = new ArrayList<>();
        instructions = new ArrayList<>();
    }

    public void writeBack()
    {
        if (!instructions.isEmpty())
        {
            Instruction current = instructions.get(0);

            if (current.instructionUnit == "ALU")
            {
                int destRegName = ((InstructionALU) current).destRegName;
//                System.out.println(((InstructionALU) current).result);
                registerFile.registers.get(destRegName).setValue(((InstructionALU) current).result);
            }

            current.writtenBack = true;
            current.retired = true;

            instructions.remove(0);

        }

        for (Instruction instruction: instructionsBuffer)
        {
            instructions.add(instruction);
        }

        instructionsBuffer.clear();
    }

    public void init(RegisterFile registerFile)
    {
        this.registerFile = registerFile;
    }
}
