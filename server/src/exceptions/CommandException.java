package exceptions;

public class CommandException extends Exception{
    private ExceptionEnum e;

    public CommandException(ExceptionEnum e){
        super(e.getValue() + "");
        this.e = e;
    }

    public ExceptionEnum getExceptionEnum(){
        return e;
    }
}
