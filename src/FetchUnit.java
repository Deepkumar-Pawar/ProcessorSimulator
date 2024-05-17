import java.util.ArrayList;
import java.util.List;

public class FetchUnit implements Unit {

    public ArrayList<Instruction> instructions;

    public boolean branchStall = false;

//    public int programCounter;

    public DecodeUnit decodeUnit;

    public RegisterFile registerFile;

    public boolean exited;

    public int jumpInstructionsCounter = 0;

    public int instructionCounter;

    public int fetch(int programCounter)
    {
        if (programCounter >= instructions.size() || branchStall || exited)
        {
            return programCounter;
        }

        Instruction fetched = instructions.get(programCounter);
        fetched.updateID(instructionCounter);
        instructionCounter++;

        if (!(fetched.instructionUnit == "BranchUnit"))
        {
            programCounter++;
        }
        else
        {
            if (((ControlInstruction) fetched).isJumpInstruction)
            {

                jumpInstructionsCounter++;

                if (ExecutionUnit.executeCycle(fetched))
                {
                    return programCounter;
                }

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

    public int executeJumps(int pc, ControlInstruction instruction) //This function resets cyclesToExecute!!
    {
        if (instruction.instructionType == "j")
        {
            instruction.cyclesToExecute = 1;
            return instruction.targetProgramCounter;
        }
        else if (instruction.instructionType == "jb")
        {
            instruction.cyclesToExecute = 1;
            return instruction.targetProgramCounter;
        }
        else if(instruction.instructionType == "jr")
        {
            instruction.cyclesToExecute = 1;
            instruction.targetProgramCounter = registerFile.registers.get(instruction.op1).getValue();
            return instruction.targetProgramCounter;
        }
        else if (instruction.instructionType == "jal" || instruction.instructionType == "jalb")
        {
            instruction.cyclesToExecute = 1;
            registerFile.registers.get(31).setValue(pc+1);
            return instruction.targetProgramCounter;
        }
        else if (instruction.instructionType == "exit")
        {
            instruction.cyclesToExecute = 1;
            exited = true;
        }

        return pc;
    }
}
