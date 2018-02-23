import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class run {

    public static final int SOCKET_PORT = 6789;
    public static final String HOST_NAME = "127.0.0.1";
    public static void main(String[] args) {
        try {
            startClient();
        } catch (IOException e) {
            System.out.println("Error: problem connecting to server");
            e.printStackTrace();
        }
    }

    public static void startClient()throws IOException{
        Scanner in = new Scanner(System.in);

        //Create client socket, connect to server
        Socket socket = new Socket(HOST_NAME,SOCKET_PORT);

        //get input from socket
        Scanner sIn = new Scanner(socket.getInputStream());

        //send input to socket
        PrintStream print = new PrintStream(socket.getOutputStream());

        System.out.println();
        while(!socket.isClosed()){

        }

        socket.close();
    }
}
