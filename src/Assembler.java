import java.util.ArrayList;

public class Assembler {
    public static ArrayList<Instruction> assemble(ArrayList<String> rawInstructions)
    {

        ArrayList<Instruction> instructions = new ArrayList<>();

        int numInstructions = rawInstructions.size();

        for (int i = 0; i < numInstructions; i++)
        {

            String[] parts = rawInstructions.get(i).split(" ");

            for (int j = 0; j < parts.length; j++)
            {
                parts[j] = parts[j].replace(",", "");
            }

            if (parts[0].startsWith("add"))
            {
                instructions.add(new InstructionALU(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[1]), "add"));
            }
        }

        return instructions;
    }
}
