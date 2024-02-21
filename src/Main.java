import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static String programFileName = "program.txt";

    static ArrayList<String> rawInstructions = new ArrayList<>();
    static ArrayList<Instruction> instructions = new ArrayList<>();
    public static void main(String[] args)
    {
        readProgramFile();
        instructions = Assembler.assemble(rawInstructions);

        Processor myProcessor = new Processor();

        myProcessor.start(instructions);

    }

    public static void readProgramFile()
    {
        Scanner scanner;

        try {
            scanner = new Scanner(new File(programFileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            rawInstructions.add(line);
            // process the line
        }
    }


}