package commands;

public class Empty implements Command {
    @Override
    public String execute(int a, int b) {
        return "Nothing to execute";
    }
}
