import commands.CommandEnum;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Run {
    public static final int SOCKET_NUMBER = 6789;

    public static void main(String[] args) {
        //testEnum();
        try {
            startServer();
        }catch(IOException e){
            System.out.println("Error: trying to open port: " + SOCKET_NUMBER);
            e.printStackTrace();
        }
    }

    public static void startServer() throws IOException {
        //Create welcome socket at port number
        ServerSocket welcomeSocket = new ServerSocket(SOCKET_NUMBER);

        while (true) {
            System.out.println("HI");
            //wait, on welcoming socket for contact by client
            Socket connectionSocket = welcomeSocket.accept();

            //Create input stream, attached to socket
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            //Create output stream, attahced to socket
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            //Read in Line from socket
            String input = inFromClient.readLine();
            String capInput = input.toUpperCase();

            //Write out line to socket
            outToClient.writeBytes(capInput);
        }
    }


    public static void testEnum() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("PUT name to get enum");
            String next = in.nextLine();
            String output = CommandEnum.Execute(next);
            System.out.println("Output => " + output + "\n\n");
        }
    }
}
