package commands;

public class Subtract implements Command {
    @Override
    public String execute(int[] val){
        if(val.length == 0){
            return "0";
        }

        int output = val[0];

        for(int i =1; i < val.length; i++){
            output -= val[i];
        }
        return output + "";
    }
}
