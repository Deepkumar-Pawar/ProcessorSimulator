import java.util.ArrayList;

public class ExecutionUnit implements Unit{
    ArrayList<Instruction> instructionsBuffer;

    ArrayList<Instruction> instructions;

    ArrayList<Register> resultForwardingRegisters;
}
