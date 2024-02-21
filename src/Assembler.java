import java.util.ArrayList;
import java.util.HashMap;

public class Assembler {
    public static ArrayList<Instruction> assemble(ArrayList<String> rawInstructions)
    {

        ArrayList<Instruction> instructions = new ArrayList<>();

        HashMap<String, Integer> labelPCMapping = new HashMap<>();

        int numInstructions = rawInstructions.size();

        for (int i = 0; i < numInstructions; i++)
        {

            String[] parts = rawInstructions.get(i).split(" ");

            for (int j = 0; j < parts.length; j++)
            {
                parts[j] = parts[j].replace(",", "");
                parts[j] = parts[j].replace("$", "");
            }

            if (parts[0].equals("add"))
            {
                instructions.add(new InstructionALU(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[1]), "add"));
            }
            else if (parts[0].equals("addi"))
            {
                instructions.add(new InstructionALU(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[1]), "addi"));
            }
            else if (parts[0].equals("beq"))
            {
                instructions.add(new ControlInstruction(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], "beq"));
            }
            else if (parts[0].endsWith(":"))
            {
                labelPCMapping.put(parts[0].replace(":", ""), instructions.size());
            }
        }

//        System.out.println(labelPCMapping);

        for (Instruction instruction : instructions)
        {
            if (instruction.instructionUnit == "BranchUnit")
            {
                ((ControlInstruction) instruction).targetProgramCounter = labelPCMapping.get(((ControlInstruction) instruction).targetLabel);
//                System.out.println(((ControlInstruction) instruction).targetProgramCounter);
            }
        }

        return instructions;
    }
}
