package commands;

public class Add implements Command{

    @Override
    public String execute(int a, int b) {

        int value = a + b;
        return value + "";
    }
}
