import java.util.LinkedList;
import java.util.List;

public class ROB {

    LinkedList<ROBEntry> robEntries;

    public void add(Instruction instruction, List<Integer> destRegs)
    {
        robEntries.add(new ROBEntry(instruction, destRegs));
    }

    public void remove(Instruction instruction)
    {
//        for (int i = 0; );
//
//        robEntries.
    }

}
