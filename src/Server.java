/**
 * Created by mike on 3/21/17.
 */
import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader( clientSocket.getInputStream() )
        );

        String inputLine, outputLine;

        outputLine = "Hello!";
        out.println(outputLine);

        while ((inputLine = in.readLine()) != null) {
            outputLine = inputLine;
            out.println( outputLine );
            if (outputLine.equals("Bye."))
                break;
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}

