import java.util.List;

public class ROBEntry {

    public Instruction instruction;
    public List<Integer> destinationRegisters;

    public boolean changesMemory;
    public boolean changesRegistry;

    public int memoryAddress;
    public int destReg;

    public int data;

    public ROBEntry (Instruction instruction, List<Integer> destinationRegisters)
    {
        this.instruction = instruction;
        this.destinationRegisters = destinationRegisters;

        this.changesMemory = false;
        this.changesRegistry = false;
    }

}
