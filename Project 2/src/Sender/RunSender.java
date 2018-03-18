package Sender;

import Utils.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class RunSender {

    private static String URL;
    private static int port_number;
    private static String MessageFileName;

    private static boolean isRunning = true;
    private static Socket socket;
    private static Scanner  sIn;
    private static String[] info;

    private static int pos = 1;

    public static void main(String[] args) {

        if(args.length < 3){
            System.err.println("Require URl, port_number and Message_File_Name to run program");
            System.exit(0);
        }

        try {
            readTextFile(args[2]);
        } catch (IOException e) {
            System.out.println("Error reading file");
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
                   // Thread.sleep(2000l);
                    continue;
                }
                throw e;
            }
        }

        sIn = new Scanner(socket.getInputStream());

        String data[] = info;
        try {

            for(int i =0; i < data.length; i++){
                System.out.print("Waiting ACK" + (i%2) + ", "+ pos++);

                send(i%2 == 1,i+1,data[i]);

                String input = getInput();

                if(input.equals("0")){
                    System.out.println(",\t DROP,\t  resend Packet"+ ((i)%2));
                    i--;
                }else if(input.equals("2")){
                    System.out.println(",\t CORRUPT, resend Packet"+ ((i)%2));
                    i--;
                }
                    else {
                    if(i+1 == data.length){
                        System.out.println(",\t ACK"+(i%2)+",\t  no more packets to send");
                    }else {
                    System.out.println(",\t ACK"+(i%2)+",\t  send Packet" + ((i+1)%2));
                    }
                }
            }

            PrintStream sout = new PrintStream(socket.getOutputStream());
            sout.println("-1");
        }catch(Exception e){
            System.out.println("Error sending packets");
            e.printStackTrace();
        }
        System.out.println("\n Stop Sender");
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
        return sIn.nextLine();
    }

    private static void readTextFile(String fileName) throws IOException {
        System.out.println(fileName);

        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        String output = "";
        while ((st = br.readLine()) != null){
            output += st + " ";
        }
        info = output.trim().split(" ");
    }
}
