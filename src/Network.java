/**
 * Created by mike on 3/21/17.
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Network {
    private static final int P1_PORT = 3232;
    private static final int P2_PORT = 3434;
    private static final String ADDRESS = "10.136.18.224";
//  private static final String ADDRESS = "10.0.1.45";

    private String fromServer;

    private PrintWriter out = null;

    private void Client() throws IOException {
        Socket kkSocket = null;
        BufferedReader in = null;

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        int port;
        port = available(P1_PORT) ? P1_PORT : P2_PORT;

        try {
            kkSocket = new Socket(ADDRESS, port );
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + ADDRESS);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + ADDRESS);
            System.exit(1);
        }

        Thread receiveMessage = new Thread(new ReceiveChat(in, stdIn, out));
        receiveMessage.start();

        Scanner console = new Scanner(System.in);
        String message;

        while (true) {
            message = console.nextLine();
            out.println( port + message );
            if (message.equals("exit"))
                break;
        }

        out.close();
        in.close();
        stdIn.close();
        kkSocket.close();
    }

    private class ReceiveChat implements Runnable {
        private BufferedReader in;
        private BufferedReader stdIn;
        private PrintWriter out;

        private ReceiveChat(BufferedReader in, BufferedReader stdIn, PrintWriter out) {
            this.in = in;
            this.stdIn = stdIn;
            this.out = out;
        }

        public void run() {
            try {
                while ((fromServer = in.readLine()) != null) {

                    String str = "Opponent: " + fromServer.substring(4).toLowerCase();
                    System.out.println(str);

                    if (fromServer.equals("exit"))
                        break;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            System.out.println("port is available");
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }

        return false;
    }

    public static void main(String... args) {

       Thread alien = new Thread(new Runnable() {
            public void run() {
                Network net = new Network();
                try {
                    net.Client();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
       alien.start();
    }
}
