package Network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

class Client extends Thread {
    private Socket socket;

    public Queue<String> input;

    public Client(Socket socket) {
        this.socket = socket;

        input = new LinkedList<>();
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Queue<String> data = read.data;
            String text = "";
            while (text != null) {
                text = in.readLine();

                if(text != null){
                    input.add(text);
                }
            }

        } catch (Exception e) {
            System.out.println("Client lost");
            e.printStackTrace();
        }

        disconnect();
    }

    public void send(String info) throws IOException{
        PrintStream sout = new PrintStream(socket.getOutputStream());
        sout.println(info);
    }

    public void disconnect() {
        RunNetwork.removeClient(this);
    }
}