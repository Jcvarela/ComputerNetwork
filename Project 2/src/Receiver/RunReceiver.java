package Receiver;

import Utils.Message;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class RunReceiver {

    private static String URL;
    private static int port_number;

    private static Socket socket;
    private static Scanner  sIn;
    private static boolean isRunning = true;

    public static void main(String[] args) {
        if(args.length < 2){
            System.err.println("Require URl and port_number to run program");
            System.exit(0);
        }

        try {
            URL = args[0];
            port_number = Integer.parseInt(args[1]);

            startClient();
        } catch (IOException e) {
            System.out.println("Error: problem connecting to Network");
            //  e.printStackTrace();
        }catch (NumberFormatException e){
            System.out.println("Error: Invalid port_number value: " + args[0]);
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void startClient() throws IOException, InterruptedException {
        int count = 0;

        while(true) {
            //Trying to connect to network
            try {
                System.out.println("Trying to connect to Network");
                //Create client socket, connect to server
                socket = new Socket(URL, port_number);
                System.out.println("Connection to Network successful");
                break;
            } catch(IOException e){
                count++;
                if(count < 4){
                    Thread.sleep(2000l);
                    continue;
                }
                throw e;
            }
        }

        //get input from socket
        sIn = new Scanner(socket.getInputStream());

        try {

            while(isRunning){
                String input = getInput();
                System.out.println(input);
                Message m = new Message(input);
                System.out.println("I got " + input);
                if(checkInput(m)){
                    send("1");
                }
                else {
                    send("0");
                }
            }


        }catch(Exception e){
            System.out.println("Error sending packets");
            e.printStackTrace();
        }

        socket.close();
    }

    public static boolean checkInput(Message m){
        int count = 0;
        for (char i: m.packetContent.toCharArray()){
            count += i;
        }
        return count == m.checkSum;
    }

    public static void send(String text) throws IOException {
        PrintStream sout = new PrintStream(socket.getOutputStream());
        sout.println(text);
    }

    public static String getInput(){
        return sIn.next();
    }
}
