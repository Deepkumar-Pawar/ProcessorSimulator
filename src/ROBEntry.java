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

    public ROBEntry setChangesMemory(boolean changesMemory) {
        this.changesMemory = changesMemory;
        return this;
    }

    public ROBEntry setChangesRegistry(boolean changesRegistry) {
        this.changesRegistry = changesRegistry;
        return this;
    }

    public ROBEntry setMemoryAddress(int memoryAddress) {
        this.memoryAddress = memoryAddress;
        return this;
    }

    public ROBEntry setDestReg(int destReg) {
        this.destReg = destReg;
        return this;
    }

    public ROBEntry setData(int data) {
        this.data = data;
        return this;
    }

}
