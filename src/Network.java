/**
 * Created by mike on 3/21/17.
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Network implements Runnable {
    private int port;
    private String address;
    private BlockingQueue<String> output;
    private BlockingQueue<String> input;

    Network (String address, int port,  BlockingQueue<String> output,  BlockingQueue<String> input) {
        this.address = address;
        this.port = port;
        this.output = output;
        this.input = input;
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

//        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        try {
            kkSocket = new Socket(address, port );
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            throw new NetworkConnectivityException("Don't know about host: " + address);
        } catch (IOException e) {
            throw new NetworkConnectivityException("Couldn't get I/O for the connection to: " + address);
        }


        String fromServer;
        while (true) {
            try {
                Scanner console = new Scanner(System.in);
                String message;
                while ((fromServer = in.readLine()) != null) {
                    if ( fromServer.equals( "WELCOME TO ANOTHER EDITION OF THUNDERDOME!" ) ) {
                        System.out.println( "Welcome to the ThunderDome, enter tournament password: " );
                        message = console.nextLine();
                        out.println( port + "ENTER THUNDERDOME " + message );
                    }else
                    if ( fromServer.equals( "TWO SHALL ENTER, ONE SHALL LEAVE" )) {
                        System.out.println( "Two enter, one will leave. Enter credentials (username password): " );
                        message = console.nextLine();
                        out.println( port + "I AM " + message );
                    }else
                    if (fromServer.substring(0,32).equals( "WAIT FOR THE TOURNAMENT TO BEGIN" )) {
                        System.out.println( "Waiting for tournament to begin..." + fromServer.substring(32));
                    }else
                    if (fromServer.substring(0,13).equals( "NEW CHALLENGE" )) {
                        System.out.println( "New challenge" );
                    }else
                    if (fromServer.substring(0,12).equals( "END OF ROUND" )) {
                        System.out.println( "End of round " + fromServer.toLowerCase().substring(13) );
                    }else
                    if (fromServer.equals("END OF CHALLENGES")) {
                        System.out.println( "End of challenges" );
                    }else
                    if (fromServer.equals("WAIT FOR THE NEXT CHALLENGE TO BEGIN")) {
                        System.out.println( "Waiting for another challenge" );
                    }else
                    if (fromServer.equals("THANK YOU FOR PLAYING! GOODBYE")) {
                        System.out.println( "All done." );
                        break;
                    }else{
                        // TODO:
                        // put into queue for parser
                        System.out.println( fromServer );
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                break;
            }
        }

        out.close();
        in.close();
//        stdIn.close();
        kkSocket.close();
    }

//
//    private class ReceiveChat implements Runnable {
//        private BufferedReader in;
//        private String fromServer;
//        private PrintWriter out;
//
//        private ReceiveChat(BufferedReader in, PrintWriter out) {
//            this.in = in;
//            this.out = out;
//        }
//
//        public void run() {
//
//        }
//    }

    class NetworkConnectivityException extends Exception {
        private NetworkConnectivityException(String s) {
            super(s);
        }
    }
}

