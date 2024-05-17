import java.util.List;

public class ROBEntry {

    public Instruction instruction;
    public List<Integer> destinationRegisters;

    public ROBEntry (Instruction instruction, List<Integer> destinationRegisters)
    {
        this.instruction = instruction;
        this.destinationRegisters = destinationRegisters;
    }

}
