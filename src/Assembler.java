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
            if (rawInstructions.get(i) == "" || rawInstructions.get(i).startsWith("#"))
            {
                continue;
            }

//            rawInstructions.get

            String[] parts = rawInstructions.get(i).strip().split(" ");

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
            else if (parts[0].equals("mod"))
            {
                instructions.add(new InstructionALU(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[1]), "mod"));
            }
            else if (parts[0].equals("beq"))
            {
                instructions.add(new ControlInstruction(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], "beq", false));
            }
            else if (parts[0].equals("bne"))
            {
                instructions.add(new ControlInstruction(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], "bne", false));
            }
            else if (parts[0].equals("bgt"))
            {
                instructions.add(new ControlInstruction(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], "bgt", false));
            }
            else if (parts[0].equals("bge"))
            {
                instructions.add(new ControlInstruction(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], "bge", false));
            }
            else if (parts[0].equals("blt"))
            {
                instructions.add(new ControlInstruction(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], "blt", false));
            }
            else if (parts[0].equals("ble"))
            {
                instructions.add(new ControlInstruction(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], "ble", false));
            }
            else if (parts[0].endsWith(":"))
            {
                labelPCMapping.put(parts[0].replace(":", ""), instructions.size());
            }
            else if (parts[0].equals("j"))
            {
                instructions.add(new ControlInstruction(0, 0, parts[1], "j", true));
            }
            else if (parts[0].equals("jb"))
            {
                instructions.add(new ControlInstruction(0, 0, parts[1], "jb", true));
            }
            else if (parts[0].equals("jr"))
            {
                instructions.add(new ControlInstruction(Integer.parseInt(parts[1]), 0, "", "jr", true));
            }
            else if (parts[0].equals("jal"))
            {
                instructions.add(new ControlInstruction(0, 0, parts[1], "jal", true));
            }
            else if (parts[0].equals("jalb"))
            {
                instructions.add(new ControlInstruction(0, 0, parts[1], "jalb", true));
            }
            else if (parts[0].equals("exit"))
            {
                instructions.add(new ControlInstruction(0, 0, "", "exit", true));
            }
        }

//        System.out.println(labelPCMapping);

        for (Instruction instruction : instructions)
        {
            if (instruction.instructionUnit == "BranchUnit")
            {
                if (((ControlInstruction) instruction).instructionType == "j" ||((ControlInstruction) instruction).instructionType == "jal")
                {
                    ((ControlInstruction) instruction).targetProgramCounter = Integer.parseInt(((ControlInstruction) instruction).targetLabel);
                }
                else if (((ControlInstruction) instruction).instructionType == "jb" || ((ControlInstruction) instruction).instructionType == "jalb")
                {
                    ((ControlInstruction) instruction).targetProgramCounter = labelPCMapping.get(((ControlInstruction) instruction).targetLabel);
                }
                else if (((ControlInstruction) instruction).isJumpInstruction)
                {
                    continue;
                }
                else{
                    ((ControlInstruction) instruction).targetProgramCounter = labelPCMapping.get(((ControlInstruction) instruction).targetLabel);
//                System.out.println(((ControlInstruction) instruction).targetProgramCounter);
                }

            }
        }

        return instructions;
    }
}
