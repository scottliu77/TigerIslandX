import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.System.exit;

/**
 * Created by mike on 4/8/17.
 */
public class TigerIsland {
    public static void main(String[] args) {
        String a = args[0];
        if(a.equals("UI")){
            UI.startUI();
            while(true){}
        }
        int port = 0000;
        String address = "127.0.0.1";

        try {
            address = args[0];
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Must provide an address and a port: XXX.XXX.XXX.XXX XX");
            exit(1);
        }

        BlockingQueue<String> serverToClient = new ArrayBlockingQueue<String>(64);
        BlockingQueue<String> clientToServer = new ArrayBlockingQueue<String>(64);

        Thread net = new Thread( new Network(address, port, serverToClient, clientToServer ) );
        net.setName("network");
        net.start();


        Thread parser1 = new Thread( new Parser(serverToClient, clientToServer) );
        Thread parser2 = new Thread( new Parser(serverToClient, clientToServer) );

        parser1.setName("parser 1");
        parser2.setName("parser 2");
        parser1.start();
        parser2.start();


        while (true) {

        }
    }
}
