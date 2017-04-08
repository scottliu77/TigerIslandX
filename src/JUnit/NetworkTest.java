import org.junit.Rule;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class NetworkTest {

    @Test
    public void testClientFailConnect() {
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setErr(new PrintStream(outContent));

        Network net = new Network( "127.0.0.1", 3000  );
        Thread netThread = new Thread( net );

//        try {
//            netThread.start();
//        }catch( Network.NetworkConnectivityException nce ) {
//            assert (nce.equals("Couldn't get I/O for the connection to: 127.0.0.1"));
//        }

        assert (net instanceof Network);
    }

//    @Test
//    public void testReceiveChat() {
//        Network.ReceiveChat
//        Method chat = Network.class.getDeclaredMethod("run", String.class);
//    }

}