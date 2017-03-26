/**
 * Created by mike on 3/21/17.
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Network {
    public String fromServer;
    public String fromUser;

    private PrintWriter out = null;

    public void Client() throws IOException {
        Socket kkSocket = null;
        BufferedReader in = null;

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            kkSocket = new Socket("127.0.0.1", 4444);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: 127.0.0.1");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: 127.0.0.1");
            System.exit(1);
        }

        Thread receiveMessage = new Thread(new ReceiveChat(in, stdIn, out));
        receiveMessage.start();

        Scanner console = new Scanner(System.in);
        String message;

        while (true) {
            message = console.nextLine();
            out.println( message );
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

        public ReceiveChat(BufferedReader in, BufferedReader stdIn, PrintWriter out) {
            this.in = in;
            this.stdIn = stdIn;
            this.out = out;
        }

        public void run() {
            try {
                while ((fromServer = in.readLine()) != null) {
                    //System.out.println("Server: " + fromServer);
                    String str = "Server : " + fromServer + "\n";
                    System.out.println(str);

                    if (fromServer.equals("Bye."))
                        break;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
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
