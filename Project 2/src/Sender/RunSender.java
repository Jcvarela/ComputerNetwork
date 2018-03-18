package Sender;

import Utils.Message;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class RunSender {

    private static String URL;
    private static int port_number;
    private static String MessageFileName;

    private static boolean isRunning = true;
    private static Socket socket;
    private static Scanner  sIn;

    public static void main(String[] args) {

        if(args.length < 3){
            System.err.println("Require URl, port_number and Message_File_Name to run program");
            System.exit(0);
        }

        try {
            URL = args[0];
            port_number = Integer.parseInt(args[1]);
            MessageFileName = args[2];

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

        //send info through socket


        String data[] = info;
        try {

            for(int i =0; i < data.length; i++){
                send(true,i+1,data[i]);
                System.out.println("sending " + data[i]);
                String input = getInput();

                if(input.equals("0")){
                    //it fail
                    i--;
                }
                System.out.println("I got " + input);
            }

        }catch(Exception e){
            System.out.println("Error sending packets");
            e.printStackTrace();
        }

        socket.close();
    }

    public static void send(boolean check, int id, String data) throws IOException {
        int sum = 0;
        for(int i =0; i < data.length(); i++){
            sum += data.charAt(i);
        }

        Message m = new Message(check,(byte)id,sum,data);

        PrintStream sout = new PrintStream(socket.getOutputStream());
        sout.println(m.getByte());
    }

    public static String getInput(){
        return sIn.next();
    }

    private static String[] info = "aaaaa bbb cc ffff ddd pppp eeee zzzzzzzzzzZ".trim().split(" ");

}
