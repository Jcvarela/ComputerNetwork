package commands;

/**
 * Created by jcvar on 2/22/2018.
 */
public class Terminate implements Command {
    @Override
    public String execute(int[] val) {
        return CommandEnum.TERMINATE.toString();
    }
}
