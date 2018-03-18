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

    private static String message = "";
    private static int count = 1;

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
            System.out.print("Waiting ");
            while(isRunning){

                String input = getInput();


                if(input.equals("0")|| input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4")){
                    continue;
                }
                if(input.equals("-1")){
                    break;
                }

                Message m = new Message(input);

                System.out.print((m.sequenceNo?1:0) + ", " + count++ +", " + m.checkSum +", " + m.packetContent + ", ");

                if(checkInput(m)){
                    message += m.packetContent + " ";
                    System.out.println("ACK" + (m.sequenceNo?1:0));
                    send(((m.sequenceNo?1:0)*2 + 1) + "");
                    String n = m.packetContent;
                    if(n.trim().charAt(n.length() -1) == '.'){
                        break;
                    }
                }
                else {
                    System.out.println("ACK" +(m.sequenceNo?0:1));
                    send(((m.sequenceNo?0:1)*2 + 2) + "");
                }
                System.out.print("Waiting ");
            }

            System.out.println("\n Message: " + message);

            System.out.println("\n Stop receiver.");

        }catch(Exception e){
            System.out.println("Error sending packets");
            e.printStackTrace();
        }

        socket.close();
    }

    public static boolean checkInput(Message m){
        int count = 0;

        try {
            for (char i : m.packetContent.toCharArray()) {
                count += i;
            }
            return count == m.checkSum;
        }catch(Exception e){
            return false;
        }
    }

    public static void send(String text) throws IOException {
        PrintStream sout = new PrintStream(socket.getOutputStream());
        sout.println(text);
    }

    public static String getInput(){
        return sIn.nextLine();
    }
}
