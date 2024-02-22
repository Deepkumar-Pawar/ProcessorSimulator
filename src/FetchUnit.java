import java.util.ArrayList;
import java.util.List;

public class FetchUnit implements Unit {

    public ArrayList<Instruction> instructions;

    public boolean branchStall = false;

//    public int programCounter;

    public DecodeUnit decodeUnit;

    public RegisterFile registerFile;

    public boolean exited;

    public int fetch(int programCounter)
    {
        if (programCounter >= instructions.size() || branchStall || exited)
        {
            return programCounter;
        }

        Instruction fetched = instructions.get(programCounter);

        if (!(fetched.instructionUnit == "BranchUnit"))
        {
            programCounter++;
        }
        else
        {
            if (((ControlInstruction) fetched).isJumpInstruction)
            {
                programCounter = executeJumps(programCounter, (ControlInstruction) fetched);
                return programCounter;
            }
            else
            {
                branchStall = true;
            }
        }


        decodeUnit.instructionsBuffer.add(fetched.copyOf());


        return programCounter;

        //add logic to update program counter
        //maybe add a small decoder that checks if this is a branch and updates program counter accordingly
    }

    public void init(DecodeUnit decodeUnit, RegisterFile registerFile)
    {
        this.decodeUnit = decodeUnit;
        this.registerFile = registerFile;
        exited = false;
//        this.instructions = new ArrayList<>();
    }

    public void giveInstructions (ArrayList<Instruction> instructions)
    {
        this.instructions = new ArrayList<>(instructions);
    }

    public int executeJumps(int pc, ControlInstruction instruction)
    {
        if (instruction.instructionType == "j")
        {
            return instruction.targetProgramCounter;
        }
        else if (instruction.instructionType == "jb")
        {
            return instruction.targetProgramCounter;
        }
        else if(instruction.instructionType == "jr")
        {
            instruction.targetProgramCounter = registerFile.registers.get(instruction.op1).getValue();
            return instruction.targetProgramCounter;
        }
        else if (instruction.instructionType == "jal" || instruction.instructionType == "jalb")
        {
            registerFile.registers.get(31).setValue(pc+1);
            return instruction.targetProgramCounter;
        }
        else if (instruction.instructionType == "exit")
        {
            exited = true;
        }

        return pc;
    }
}
