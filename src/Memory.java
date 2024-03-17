public class Memory implements MemoryUnit {

    public int size;

    int[] memory;

    public Memory(int size)
    {
        this.size = size;

        memory = new int[size];
    }

    public int load(int address)
    {
        try {
            return memory[address];
        } catch (IndexOutOfBoundsException e)
        {
            throw new IndexOutOfBoundsException("ERROR: ACCESSING MEMORY - ADDRESS OUT OF BOUNDS");
        } catch (NullPointerException e)
        {
            throw new NullPointerException("ERROR: ACCESSING MEMORY - ADDRESS LOCATION IS NULL");
        }
    }

    public void store(int address, int data)
    {
        try {
        memory[address] = data;
        } catch (IndexOutOfBoundsException e)
        {
            throw new IndexOutOfBoundsException("ERROR: ACCESSING MEMORY - ADDRESS OUT OF BOUNDS");
        } catch (NullPointerException e)
        {
            throw new NullPointerException("ERROR: ACCESSING MEMORY - ADDRESS LOCATION IS NULL");
        }
    }

}
