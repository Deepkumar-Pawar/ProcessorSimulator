import java.util.ArrayList;

public class RegisterFile {

    public int numRegisters = 32;

    public ArrayList<Register> registers;

    public RegisterFile()
    {
        registers = new ArrayList<>();

        for (int i = 0; i < numRegisters; i++)
        {
            registers.add(new Register(i));
        }
    }
}
