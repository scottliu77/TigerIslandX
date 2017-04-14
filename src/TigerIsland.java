import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.System.exit;

/**
 * Created by mike on 4/8/17.
 */
public class TigerIsland {
    public static String tournamentPassword = "";
    public static String teamNameUserPass = "";
    public static boolean UIshouldRun = false;

    public static void main(String[] args) {
        String startUI = args[0];
        if (startUI.equals("UI")) {
            UIshouldRun = true;
            UI.startUI();
        }else{
            int port = 3000;
            String address = "127.0.0.1";

            try {
                address = args[0];
                port = Integer.parseInt(args[1]);
                tournamentPassword = args[2];
                teamNameUserPass = args[3] + " " + args[4];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Must provide an address, port, tournament password, team name, and team password:\n<iPv4 address> <port #> <tournament password> <team name> <team password>");
                exit(1);
            }

            BlockingQueue<String> serverToClient = new ArrayBlockingQueue<String>(64);
            BlockingQueue<String> clientToServer = new ArrayBlockingQueue<String>(64);
            GameIDs games = new GameIDs();

            Thread net = new Thread( new Network(address, port, serverToClient, clientToServer ) );
            net.setName("network");
            net.start();


            Thread parser1 = new Thread( new Parser(serverToClient, clientToServer, games) );
            Thread parser2 = new Thread( new Parser(serverToClient, clientToServer, games) );

            parser1.setName("parser 1");
            parser2.setName("parser 2");
            parser1.start();
            parser2.start();
        }
    }
}

class GameIDs {
    private String game1 = "empty";
    private String game2 = "empty";

    public boolean checkGame(String parserName, String gameIDToCheck) {
        if (parserName.equals( "parser 1" )) {
            //System.out.println("Comparing " + parserName + "'s id of " + gameIDToCheck + " with " + game2);
            return game2.equals(gameIDToCheck);
        }else{
            //System.out.println("Comparing " + parserName + "'s id of " + gameIDToCheck + " with " + game1);
            return game1.equals(gameIDToCheck);
        }
    }

    public void setGameID(String parserName, String gameID) {
        if (parserName.equals( "parser 1" )) {
            game1 = gameID;
        }else{
            game2 = gameID;
        }
    }
}

