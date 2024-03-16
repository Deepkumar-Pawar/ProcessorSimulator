import java.util.ArrayList;

public class ExecutionUnit implements Unit{
    ArrayList<Instruction> instructionsBuffer;

    ArrayList<Instruction> instructions;

    ArrayList<Register> resultForwardingRegisters;

    public int checkForwardedResultRegisters(int regName)
    {
        for (int i = 0; i < resultForwardingRegisters.size(); i++)
        {
            if (resultForwardingRegisters.get(i).name == regName)
            {
                return i;
            }
        }

        return -1;
    }

    public void cleanForwardedResultRegisters(int regName)
    {
        ArrayList<Integer> indices = new ArrayList<>();

        for (int i = 0; i < resultForwardingRegisters.size(); i++)
        {
            if (resultForwardingRegisters.get(i).name == regName)
            {
                indices.add(i);
            }
        }

        for (int i = indices.size()-1; i >= 0; i--)
        {
            resultForwardingRegisters.remove((int) indices.get(i));
//            System.out.println("CLEANED");
        }
    }

    public void addForwardedResultRegister(int name, int value)
    {
        cleanForwardedResultRegisters(name);
        resultForwardingRegisters.add(new Register(name, value));
    }

    public static boolean executeCycle(Instruction instruction)
    {
        instruction.cyclesToExecute--;

        if (instruction.cyclesToExecute > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
