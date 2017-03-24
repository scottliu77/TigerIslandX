/**
 * Created by mike on 3/21/17.
 */
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.net.*;

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

        tfield.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                // Here you will send it to the server side too, put that code here
                fromUser = tfield.getText() + "\n";
                if (fromUser != null)
                {
                    System.out.println("Client: " + fromUser);
                    tarea.append(fromUser);
                    out.println(fromUser);
                    tfield.setText("");
                }
            }
        });
        //out.close();
        //in.close();
        //stdIn.close();
        //kkSocket.close();
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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                KnockKnockClient client = new KnockKnockClient();
                try {
                    client.Client();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
