package commands;

import exceptions.ExceptionEnum;

public class Bye implements Command{
    @Override
    public String execute(int a, int b) {
        return ExceptionEnum.EXIT_EXCEPTION.getValue() + "";
    }
}
