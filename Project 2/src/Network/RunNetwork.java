package Network;

import java.io.IOException;

public class RunNetwork {

    private static int port_number; //6321 for testing
    private static boolean running = false;

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
        
    }


}
