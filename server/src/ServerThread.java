import commands.CommandEnum;
import exceptions.ExceptionEnum;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by jcvar on 2/22/2018.
 */
public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket(){return socket;}

    @Override
    public void run() {
        try {
            //Create input stream, attached to socket
            Scanner in = new Scanner(socket.getInputStream());

            //Create output stream, attached to socket
            PrintStream outToClient = new PrintStream(socket.getOutputStream());

            outToClient.println("Hello!");
            String output = "";
            String exitValue = ExceptionEnum.EXIT_EXCEPTION.getValue() + "";

            while (!output.equals(exitValue)) {
                //Read in Line from socket
                String input = in.nextLine();
                output = CommandEnum.execute(input);


                System.out.println("get: " + input + ", return " + output);
                if(CommandEnum.isSpecificCommandENum(output,CommandEnum.TERMINATE)){
                    outToClient.println("-5");
                    server.stopServer();
                    break;
                }

                //Write out line to socket
                outToClient.println(output);

            }

        } catch (Exception e) {
            System.out.println("Client lost");
            e.printStackTrace();
        }

        server.removeClient(this);
        disconnect();
    }

    public void disconnect(){
        try{

            PrintStream outToClient = new PrintStream(socket.getOutputStream());
            String exitValue = ExceptionEnum.EXIT_EXCEPTION.getValue() + "";
            outToClient.println(exitValue);
            this.stop();
            socket.close();

        }catch(Exception e){
            System.out.println("Client lost");
            e.printStackTrace();
        }
    }
}
