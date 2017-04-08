/**
 * Created by mike on 3/21/17.
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Network implements Runnable {
    private int port;
    private String address;

    Network (String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void run() {
        try {
            this.Client();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void Client() throws IOException, NetworkConnectivityException {
        Socket kkSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        try {
            kkSocket = new Socket(address, port );
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            throw new NetworkConnectivityException("Don't know about host: " + address);
        } catch (IOException e) {
            throw new NetworkConnectivityException("Couldn't get I/O for the connection to: " + address);
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
        private String fromServer;
//        private BufferedReader stdIn;
//        private PrintWriter out;

        private ReceiveChat(BufferedReader in, BufferedReader stdIn, PrintWriter out) {
            this.in = in;
//            this.stdIn = stdIn;
//            this.out = out;
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

    class NetworkConnectivityException extends Exception {
        private NetworkConnectivityException(String s) {
            super(s);
        }
    }
}

