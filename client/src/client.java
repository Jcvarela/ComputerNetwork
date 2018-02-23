import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class client {

    private static int port_number;
    private static String serverURL;

    private static HashMap<String,String> errorMap = setErrorMap();

            //['serverURL', 'port_number']
    public static void main(String[] args) {

        if(args.length < 2){
            System.err.println("Require ServerURl and port_number to run program");
            System.exit(0);
        }

        setErrorMap();

        try {
            port_number = Integer.parseInt(args[1]);
            serverURL = args[0];

            startClient();
        } catch (IOException e) {
            System.out.println("Error: problem connecting to server");
          //  e.printStackTrace();
        }
    }

    public static HashMap<String,String> setErrorMap(){
        HashMap<String,String> errorMap = new HashMap<>();

        errorMap.put("-1","incorrect operation command.");
        errorMap.put("-2"," number of inputs is less than two.");
        errorMap.put("-3","number of inputs is more than four.");
        errorMap.put("-4", "one or more of the inputs contain(s) non-number(s).");
        errorMap.put("-5", "exit");

        return  errorMap;
    }

    public static void startClient()throws IOException{
        Scanner in = new Scanner(System.in);

        //Create client socket, connect to server
        Socket socket = new Socket(serverURL, port_number);

        //get input from socket
        Scanner sIn = new Scanner(socket.getInputStream());

        //send input to socket
        PrintStream sout = new PrintStream(socket.getOutputStream());

         print(sIn.next());
         String output = "";
         try {
             while (!socket.isClosed() && !output.equals("exit")) {

                 System.out.print("Input: ");
                 String command = in.nextLine();

                 sout.println(command);


                 output = sIn.next();
                 if (errorMap.containsKey(output)) {
                     output = errorMap.get(output);
                 }

                 print(output);
             }
         }catch(Exception e){
             print("exit");
         }

        socket.close();
    }



    public static void print(String info){
        System.out.println("receive: " +info);
    }
}
