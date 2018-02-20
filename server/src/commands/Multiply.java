package commands;

public class Multiply implements Command {
    @Override
    public String execute(int a, int b) {

        long value = (long)a * (long)b;
        return value + "";
    }
}
