import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.net.*;

public class run {

    public static final int SOCKET_PORT = 6789;
    public static void main(String[] args) {
        try {
            startClient();
        } catch (IOException e) {
            System.out.println("Error: problem connecting to server");
            e.printStackTrace();
        }
    }

    public static void startClient()throws IOException{
        String hostName = "hostname";

        //Create input stream
        BufferedReader inFromUser
                = new BufferedReader(new InputStreamReader(System.in));

        //Create client socket, connect to server
        Socket clientSocket = new Socket(hostName,SOCKET_PORT);

        //Create output stream attached to socket
        DataOutputStream outToServer
                = new DataOutputStream(clientSocket.getOutputStream());

        //Create input stream attach to socket
        BufferedReader inFromServer
                = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        //GEt input
        String in = inFromServer.readLine();

        //Send line to server
        outToServer.writeBytes(in + "\n");

        //Read line from server
        String inServer = inFromServer.readLine();

        System.out.println("FROM SERVER " + inServer);

        clientSocket.close();

    }
}
