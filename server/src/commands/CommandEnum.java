package commands;

import exceptions.CommandException;
import exceptions.ExceptionEnum;

public enum CommandEnum {



    ADD(new Add()),
    SUBTRACT(new Subtract()),
    MULTIPLY(new Multiply()),
    BYE(new Bye()),

    JOIN(new Join()),
    TERMINATE(new Terminate());


    public static final int MIN_PARAM = 3;
    public static final int MAX_PARAM = 5;

    private Command command;

    private CommandEnum(Command c){
        command = c;
    }

    public static String execute(String input){
        String[] in = null;

        //CHECK IF INPUT IS GOOD
        try {
            in = checkInput(input);
        }catch(CommandException e){
            return e.getExceptionEnum().getValue() + "";
        }catch(Exception e){
            //Unexpected Exception
            return "-6";
        }


        int[] val = new int[in.length-1];

        for(int i =0; i < val.length; i++){
            val[i] = Integer.parseInt(in[i+1]);
        }


        CommandEnum e = getCommandEnumFrom(in[0]);
        String output = e.command.execute(val);

        return output;
    }

    /**
     * Check if the user input is right
     * @param input: user input
     * @return a String array that the program can use to execute the command
     * @throws Exception: INVALID INPUT
     */
    private static String[] checkInput(String input) throws Exception{
        String[] in = input.trim().split(" ");

        if(in.length == 0){
            throw new CommandException(ExceptionEnum.INCORRECT_COMMAND_EXCEPTION);
        }

        if(CommandEnum.getCommandEnumFrom(in[0]) == null){
            throw new CommandException(ExceptionEnum.INCORRECT_COMMAND_EXCEPTION);
        }

        if(isSpecificCommandENum(in[0],BYE)){
            return in;
        }
        if(isSpecificCommandENum(in[0],TERMINATE)){
            return in;
        }

        if(in.length < MIN_PARAM){
            throw new CommandException(ExceptionEnum.LESS_INPUT_EXCEPTION);
        }

        if(in.length > MAX_PARAM){
            throw new CommandException(ExceptionEnum.EXTRA_INPUT_EXCEPTION);
        }

        //check input value
        for(int i = 1; i < in.length; i++){
            try{
                Integer.parseInt(in[i]);
            }catch(NumberFormatException e){
                throw new CommandException(ExceptionEnum.INVALID_INPUT_EXCEPTION);
            }
        }

        return in;
    }


    public static boolean isSpecificCommandENum(String s, CommandEnum e){
        //TODO: check to lower case
        s = s.trim().toLowerCase();

        String name = e.name();
        name = name.trim().toLowerCase();

        return s.equals(name);
    }

    public static CommandEnum getCommandEnumFrom(String name){

        CommandEnum[] enums = CommandEnum.values();
        for(int i = 0; i < enums.length; i++){
            if(isSpecificCommandENum(name,enums[i])){
                return enums[i];
            }
        }

        return null;
    }
}
