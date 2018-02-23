import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Iterator;

public class server {
    private static int port_number;

    private static HashSet<ServerThread> clients = new HashSet<ServerThread>();

    private static boolean running = true;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Require port_number to run server");
            System.exit(0);
        }

        try {
            port_number = Integer.parseInt(args[0]);
            startServer();
        } catch (IOException e) {
            System.out.println("Error: trying to open port: " + port_number);
            e.printStackTrace();
        }
    }


    public static void startServer() throws IOException {
        //Create welcome socket at port number
        ServerSocket serverSocket = new ServerSocket(port_number);

        System.out.println("Server Up");
        System.out.println("Port number: " + port_number);

        Thread loop = loopClients(serverSocket);
        loop.start();

        while (running) {
            try {
                Thread.sleep(1000l);

            } catch (Exception e) {

            }
        }

        loop.stop();
        removeAllClients();
    }

    private static Thread loopClients(ServerSocket serverSocket) {
        Thread loop = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        //wait, on welcoming socket for contact by client
                        Socket socket = serverSocket.accept();
                        addClient(new ServerThread(socket));
                    }
                } catch (Exception e) {

                }
            }
        };

        return loop;
    }


    private static void addClient(ServerThread newClient) {
        clients.add(newClient);

        String ip = (((InetSocketAddress) newClient.getSocket().getRemoteSocketAddress()).getAddress()).toString().replace("/","");

        System.out.println("==========================");
        System.out.println("get connection from " + ip);
        System.out.println("Total: " + clients.size());

        newClient.start();
    }

    public static void removeClient(ServerThread clientLeft) {
        clients.remove(clientLeft);

        System.out.println("==========================");
        System.out.println("Client left");
        System.out.println("Total: " + clients.size());
    }

    public static void removeAllClients() {
        Iterator<ServerThread> it = clients.iterator();
        while (it.hasNext()) {
            ServerThread client = it.next();
            client.disconnect();
        }

        System.out.println("Server down");
    }

    public static void stopServer() {
        running = false;
    }
}
