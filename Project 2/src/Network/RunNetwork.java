package Network;

import Utils.Message;
import org.omg.CORBA.INTERNAL;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;

public class RunNetwork {

    private static int port_number; //6321 for testing
    private static boolean running = true;

    private static HashSet<Client> clients = new HashSet<Client>();

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Require port_number to start Network");
            System.exit(0);
        }

        try {
            port_number = Integer.parseInt(args[0]);
            startNetwork();
        } catch (IOException e) {
            System.out.println("Error: trying to open port: " + port_number);
            e.printStackTrace();
        } catch (NumberFormatException e){
            System.out.println("Error: Invalid port_number value: " + args[0]);
            e.printStackTrace();
        }
    }

    private static void startNetwork() throws IOException{
        //Create welcome socket at port number
        ServerSocket serverSocket = new ServerSocket(port_number);

        System.out.println("Network Up");
        System.out.println("Port number: " + port_number);

        Thread loop = loopClients(serverSocket);
        loop.start();

        while (running) {
            try {
                update();
               Thread.sleep(50l);
            } catch (Exception e) {

            }
        }

        System.out.println("\n Stop Network");
        loop.stop();
        removeAllClients();
    }

    private static void update() throws Exception{
        if(clients.size() < 2){
            return;
        }
        Iterator<Client> it = clients.iterator();

        while (it.hasNext()){
            Client c = it.next();
            Queue<String> in = c.input;

            while(in.size() > 0){
                try {
                    String text = in.remove();
                    if(text.equals("1") || text.equals("2") || text.equals("3") || text.equals("4")){
                        int v = Integer.parseInt(text);
                        System.out.println("Received: ACK" + ((v<3)?0:1) + ", " + ((v%2 == 1)?"PASS":"DROP"));
                        String send = ((v%2 == 1)?1:2) + "";
                        // 1 -> PASS
                        //2 -> CORRUPTED
                        sendInfoToOther(send,c);
                        continue;
                    }

                    if(text.equals("-1")){
                        sendInfoToOther(text,c);
                        stopServer();
                        break;
                    }

                    Message m = new Message(text);
                    int val = m.sequenceNo?1:0;
                    //random here
                    int r = (int)(Math.random()*4);
                    if(r == 0) { //DROP
                        System.out.println("Received: Packet"+val+", " + m.id + ", DROP");

                        c.send("0"); // 0 drop

                    } else if(r == 1){ //CORRUPT
                        System.out.println("Received: Packet"+val+", " + m.id + ", CORRUPT");
                        m.checkSum++;
                        sendInfoToOther(m.getByte(),c);
                    }else { //PASS
                        System.out.println("Received: Packet"+val+", " + m.id + ", PASS");
                        sendInfoToOther(m.getByte(),c);
                    }

                } catch (IOException e) {
                    c.disconnect();
                    clients.remove(c);
                }
            }
        }
    }

    private static void sendInfoToOther(String info, Client c) throws Exception{
        Iterator<Client> it2 = clients.iterator();

        while (it2.hasNext()){
            Client temp = it2.next();
            if(temp != c){
                temp.send(info);
                return;
            }
        }
        c.send("0");
    }

    private static Thread loopClients(ServerSocket serverSocket) {
        Thread loop = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if(clients.size() >= 2){
                            Thread.sleep(5000);
                            continue;
                        }

                        //wait, on welcoming socket for contact by client
                        Socket socket = serverSocket.accept();
                        addClient(new Client(socket));
                    }
                } catch (Exception e) {

                }
            }
        };

        return loop;
    }

    private static void addClient(Client newClient) {
        clients.add(newClient);

        String ip = (((InetSocketAddress) newClient.getSocket().getRemoteSocketAddress()).getAddress()).toString().replace("/","");

        System.out.println("==========================");
        System.out.println("get connection from " + ip + "\n");

        newClient.start();
    }

    public static void removeClient(Client clientLeft) {
        clients.remove(clientLeft);

       // System.out.println("==========================");
        //System.out.println("Connection left");
        stopServer();
    }


    public static void removeAllClients() {
        Iterator<Client> it = clients.iterator();
        while (it.hasNext()) {
            Client client = it.next();

            try {
                client.send("-1");
            } catch (IOException e) {

            }
            client.disconnect();
        }
    }

    public static void stopServer() {
        running = false;
    }
}

