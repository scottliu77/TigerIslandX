/**
 * Created by mike on 3/21/17.
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {
    private static final int P1_PORT = 3232;
    private static final int P2_PORT = 3434;


    public static void main(String[] args) {
        ArrayList<String> messageQueue = new ArrayList<String>();

        Thread Player1 = new Thread( new NetPlayer( P1_PORT, messageQueue ) );
        Thread Player2 = new Thread( new NetPlayer( P2_PORT, messageQueue ) );

        Player1.start();
        Player2.start();
    }
}

class NetPlayer implements Runnable {
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private int port;

    private PrintWriter out;
    private BufferedReader in;
    private String outputLine;
    private ArrayList<String> msgQ;

    NetPlayer(int port, ArrayList<String> queue) {
        this.port = port;
        this.msgQ = queue;

        Thread sendMessages = new Thread(new MessageListener());
        sendMessages.start();
    }

    private class MessageListener implements Runnable {

        public void run() {
            while (true) {
                if ( !msgQ.isEmpty() ) {
                    for (int i = 0; i < msgQ.size(); i++) {
                        String messageID = msgQ.get(i).substring(0,3);
                        if ( !messageID.equals( Integer.toString(port) ) ) {
                            out.println( msgQ.get(i).substring(4) );
                            msgQ.remove(i);
                        }
                    }
                }
            }
        }
    }

    private void awaitConnection() {
        try {
            serverSocket = new ServerSocket( port );
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + " .");
            System.exit(1);
        }
    }

    private void acceptConnection() {
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
    }

    private void connectStreamsToBuffers() {
        try {
            out = new PrintWriter( clientSocket.getOutputStream(), true);
            in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
        } catch (IOException e) {
            System.err.println("Failed to read client sockets.");
            System.exit(1);
        }
    }

    private void listenForExit() throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            outputLine = inputLine;

            msgQ.add( Integer.toString(port) + outputLine);

            if (outputLine.equals("exit"))
                closeConnection();
        }
    }

    private void closeConnection() {
        try {
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Failed to close client sockets.");
            System.exit(1);
        }
    }

    public void run() {
        awaitConnection();
        System.out.println("Awaiting connections on port " + port + "...");

        acceptConnection();
        connectStreamsToBuffers();

        System.out.println( "Connected client on port " + port + "." );
        out.println ( "####Connected to server on port " + port + "." );

        try {
            listenForExit();
        } catch (IOException e) {
            System.err.println("failed to read incoming stream ");
        }
    }
}

