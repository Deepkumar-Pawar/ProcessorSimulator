import java.util.LinkedList;
import java.util.List;

public class ROB {

    LinkedList<ROBEntry> robEntries;

    public ROB()
    {
        robEntries = new LinkedList<>();
    }

    public void add(Instruction instruction, List<Integer> destRegs)
    {
        robEntries.addLast(new ROBEntry(instruction, destRegs));
    }

    public ROBEntry head()
    {
        return robEntries.getFirst();
    }

    public void removeHead()
    {
        robEntries.removeFirst();
    }

//    public ROBEntry remove(Instruction instruction)
//    {
////        return robEntries.remove(find(instruction));
//    }

    public int find(Instruction instruction)
    {
        for (int i = 0; i < robEntries.size(); i++)
        {
            if (robEntries.get(i).instruction.id == instruction.id)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean hasDataDependency(Instruction instruction)
    {
        boolean dataDependent = false;

        int index = find(instruction);

        for (int i = 0; i < index; i++)
        {
            for (int opReg : instruction.opRegs)
            {
                if (robEntries.get(i).destinationRegisters.contains(opReg))
                {
                    dataDependent = true;
                }
            }
        }

        return dataDependent;
    }

}
