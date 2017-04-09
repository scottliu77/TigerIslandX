import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.System.exit;

/**
 * Created by mike on 4/8/17.
 */
public class TigerIsland {
    public static void main(String[] args) {
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
        net.start();

        GameManager game1 = new GameManager(true);
        GameManager game2 = new GameManager(true);
        Thread parser1 = new Thread( new Parser(game1, serverToClient, clientToServer) );
        Thread parser2 = new Thread( new Parser(game2, serverToClient, clientToServer) );

        parser1.start();
        parser2.start();

        while (true) {

        }
    }
}
