/**
 * Created by mike on 3/21/17.
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Network implements Runnable {
    private static final String TOURNEY_PASS = "heygang";
    private static final String USER_PASS = "O O";
    private long startTime = 0;

    private int port;
    private String address;
    private final BlockingQueue<String> serverToClient;
    private final BlockingQueue<String> clientToServer;

    Network (String address, int port,  BlockingQueue<String> output,  BlockingQueue<String> input) {
        this.address = address;
        this.port = port;
        this.serverToClient = output;
        this.clientToServer = input;
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
        Socket socket;
        BufferedReader in;
        PrintWriter out;

        try {
            socket = new Socket(address, port );
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to Server");
        } catch (UnknownHostException e) {
            throw new NetworkConnectivityException("Don't know about host: " + address);
        } catch (IOException e) {
            throw new NetworkConnectivityException("Couldn't get I/O for the connection to: " + address);
        }

        Thread receiveMessage = new Thread(new ReceiveMove(out, clientToServer));
        receiveMessage.setName("Network-receiving-moves");
        receiveMessage.start();

        String fromServer;
        while (true) {
            try {
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("\033[0;31mServer: \033[0m" + fromServer);

                    if ( fromServer.equals( "WELCOME TO ANOTHER EDITION OF THUNDERDOME!" ) ) {
                        out.println("ENTER THUNDERDOME " + TOURNEY_PASS);
                    }else
                    if ( fromServer.equals( "TWO SHALL ENTER, ONE SHALL LEAVE" )) {
                        out.println("I AM " + USER_PASS );
                    }else
                    if (fromServer.substring(0,4).equals( "WAIT" )) {
                        if ( fromServer.substring(13,17).equals("TOUR") ) {
                            serverToClient.put( fromServer );
                            serverToClient.put( fromServer );
                            synchronized (serverToClient) {
                                serverToClient.notifyAll();
                            }
                        }
                    }else
                    if (fromServer.substring(0,3).equals( "NEW" )) {
//                        System.out.println( "New challenge" );
                    }else
                    if (fromServer.substring(0,5).equals( "BEGIN" )) {
//                        System.out.println( "Begin challenge" );
                    }else
                    if (fromServer.substring(0,3).equals( "END" )) {
//                        System.out.println( "End of round " + fromServer.toLowerCase().substring(13) );
                    }else
                    if (fromServer.equals("THANK YOU FOR PLAYING! GOODBYE")) {
                        System.out.println( "All done." );
                        break;
                    }else{
                        //System.out.println( "Rec: " + fromServer );
                        startTime = System.currentTimeMillis();
                        serverToClient.put( fromServer );
                        synchronized (serverToClient) {
                            serverToClient.notifyAll();
                        }
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
        socket.close();
    }


    private class ReceiveMove implements Runnable {
        private final BlockingQueue clientToServer;
        private PrintWriter out;

        private ReceiveMove(PrintWriter out, BlockingQueue in) {
            this.clientToServer = in;
            this.out = out;
        }

        public void run() {
            while(true) {
                if ( !clientToServer.isEmpty() ) {
                    System.out.println("\033[1;34mSending: \033[0m" + clientToServer.peek());
                    final long endTime = System.currentTimeMillis();
                    long diff = (endTime - startTime);
                    out.println( clientToServer.poll() );
                    System.out.println("Total time: " + diff);
                }else{
                    synchronized (clientToServer) {
                        try {
                            clientToServer.wait(350);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private class NetworkConnectivityException extends Exception {
        private NetworkConnectivityException(String s) {
            super(s);
        }
    }
}

