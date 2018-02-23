package commands;

public class Empty implements Command {
    @Override
    public String execute(int[] val) {
        return "Nothing to execute";
    }
}
