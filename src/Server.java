/**
 * Created by mike on 3/21/17.
 */

import java.net.*;
import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.System.exit;

public class Server {
    public static void main(String[] args) {

        int p1_Port = 0;
        int p2_Port = 0;

        try {
            p1_Port = Integer.parseInt( args[0] );
            p2_Port = Integer.parseInt( args[1] );
        }catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Must provide two ports: XXXX XXXX");
            exit(1);
        }

        BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(64);

        Thread Player1 = new Thread( new NetPlayer( p1_Port, messageQueue ) );
        Thread Player2 = new Thread( new NetPlayer( p2_Port, messageQueue ) );

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

    private final BlockingQueue<String> msgQ;


    private Boolean authenticated = false;

    NetPlayer(int port, BlockingQueue<String> queue) {
        this.port = port;
        this.msgQ = queue;
    }

    private class MessageListener implements Runnable {

        public void run() {
            while (true) {
                if ( !msgQ.isEmpty() ) {
                    try {
                        if ( !msgQ.peek().substring(0,4).equals( Integer.toString( port ) ) ) {
//                            System.out.println("waiting...");
                            synchronized (this) {
                                this.wait(250);
                            }
                        }else{
//                            System.out.println("Sent: " + msgQ.peek() );
                            if (!authenticated && msgQ.peek().substring(4, 21).equals("ENTER THUNDERDOME") ) {
                                System.out.println("tourney password: " + msgQ.poll().substring(22) );
                                out.println( "TWO SHALL ENTER, ONE SHALL LEAVE" );
                                authenticated = true;
                            }else if ( msgQ.peek().substring(4, 9).equals("I AM ") ) {
                                System.out.println("username and password: " + msgQ.poll().substring(9));
                                out.println("WAIT FOR THE TOURNAMENT TO BEGIN " + "player1" );
                            }else{
                                System.out.println( msgQ.poll() );
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else try {
                    synchronized (this) {
                        this.wait(250);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void awaitConnection() {
        try {
            serverSocket = new ServerSocket( port );
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + " .");
            exit(1);
        }
    }

    private void acceptConnection() {
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            exit(1);
        }
    }

    private void connectStreamsToBuffers() {
        try {
            out = new PrintWriter( clientSocket.getOutputStream(), true);
            in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
        } catch (IOException e) {
            System.err.println("Failed to read client sockets.");
            exit(1);
        }
    }

    private void listenForIncoming() throws IOException {
        String inputLine;
        String outputLine;

        while ((inputLine = in.readLine()) != null) {
            outputLine = inputLine;

            try {
                System.out.println("Rec: " + outputLine);
                msgQ.put(outputLine);
                synchronized (this) {
                    this.notifyAll();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (outputLine.equals("exit")) {
                closeConnection();
                break;
            }

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
            exit(1);
        }
    }

    public void run() {
        awaitConnection();
        System.out.println("Awaiting connections on port " + port + "...");

        acceptConnection();
        connectStreamsToBuffers();

        System.out.println( "Connected client on port " + port + "." );
        out.println ( "WELCOME TO ANOTHER EDITION OF THUNDERDOME!" );

        Thread sendMessages = new Thread(new MessageListener());
        sendMessages.start();

        try {
            listenForIncoming();
        } catch (IOException e) {
            System.err.println("failed to read incoming stream ");
        }
    }
}

