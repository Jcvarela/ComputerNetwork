package exceptions;


public enum ExceptionEnum {
    INCORRECT_COMMAND_EXCEPTION(-1,"incorrect operation command."),
    LESS_INPUT_EXCEPTION(-2, "number of inputs is less than two."),
    EXTRA_INPUT_EXCEPTION(-3, "number of inputs is more than four."),
    INVALID_INPUT_EXCEPTION(-4, "one or more of the inputs contain(s) non-number(s)."),
    EXIT_EXCEPTION(-5, "exit");

    private int value;
    private String info;

    private ExceptionEnum(int v, String i){
        value = v;
        info = i;
    }
    public String getInfo(){
        return info;
    }

    public int getValue(){
        return value;
    }

}
