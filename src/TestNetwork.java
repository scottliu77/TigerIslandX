import static java.lang.System.exit;

public class TestNetwork {
    public static void main(String[] args) {
        int port = 0000;
        String address = "127.0.0.1";

        try {
            address = args[0];
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Must provide an address and a port: XXX.XXX.XXX.XXX XXXX");
            exit(1);
        }

        Thread alien = new Thread( new Network(address, port) );

        alien.start();
    }
}
