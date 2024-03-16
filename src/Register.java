public class Register {
    int name;
    int value;

    public Register(int name)
    {
        this.name = name;
        setValue(0);
    }

    public Register(int name, int value)
    {
        this.name = name;
        setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
