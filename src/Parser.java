import javafx.geometry.Point3D;

import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Parser implements Runnable {
    private String input[];
    private String output;
    private GameManager manager;

    private int challenges;
    private String cid;
    private String gid = "empty";
    private int orientation;
    private String pid = "-1";
    private String pidOpponent;
    private String pidFound;
    private int rid;
    private int rounds;
    private String score1;
    private String score2;
    private Terrain expandTerrain;
    private Terrain terrainHexA;
    private Terrain terrainHexB;
    private Tile nextTile;
    private String tileUnifiedName;
    private String time;
    private String moveNumber;
    private int tileCount = 100;

    private String threadName;
    private final BlockingQueue<String> serverToClient;
    private final BlockingQueue<String> clientToServer;
    private final GameIDs games;

    public Parser(BlockingQueue<String> input, BlockingQueue<String> output, GameIDs games){
        this.games = games;
        manager = new GameManager(true, this);
        serverToClient = input;
        clientToServer = output;
    }

    public Parser(GameManager m){
        manager = m;
        games = new GameIDs();
        serverToClient = new ArrayBlockingQueue<String>(1000);
        clientToServer = new ArrayBlockingQueue<String>(1000);
    }

    public void run() {
        threadName = Thread.currentThread().getName();
        // check input queue for new message, send to receiveMessage()
        String messageSplit[];
        while(true) {
            if ( !serverToClient.isEmpty() ) {
                synchronized (serverToClient) {
                    if(serverToClient.isEmpty()) continue;
                    //System.out.println( Thread.currentThread().getName() + ": checking: " + serverToClient.peek() );
                    messageSplit = serverToClient.peek().split(" ");
                    String messageGID;
                    if (messageSplit[4].equals("GAME")) {
                        messageGID = messageSplit[5];
                    } else if (messageSplit[0].equals("GAME")) {
                        messageGID = messageSplit[1];
                    } else if (messageSplit[3].equals("TOURNAMENT")) {
                        if (pid.equals("-1")) {
                            receiveMessage(serverToClient.poll());
                        }
                        continue;
                    } else {
                        System.out.println("Bad Message: " + serverToClient.poll());
                        continue;
                    }

                    if (gid.equals("empty")) {
                        //check if the other parser hasn't already taken the messageGID
                        synchronized (games) {
                            if (!games.checkGame(threadName, messageGID)) {
                                gid = messageGID;
                                games.setGameID(threadName, gid);
                            }
                        }
                    }else
                    if (messageGID.equals(gid)) {
                        try {
                            receiveMessage(serverToClient.poll());
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        serverToClient.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                synchronized (serverToClient) {
                    try {
                        serverToClient.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void receiveMessage(String inputMessage){
        boolean check;
        boolean check2;
        String localGid;
        input = inputMessage.split(" ");

        //System.out.println("Parsing: " + inputMessage);

        //Server supplies tile to be placed
        check = input[0].equals("MAKE")&&input[1].equals("YOUR")&&input[2].equals("MOVE")&&input[3].equals("IN");
        if(check){
            System.out.println("Message parsed as move prompt");
            localGid = input[5];
            time = input[7];
            moveNumber = input[10];
            tileUnifiedName = input[12];

            String tileTerrainNames[] = tileUnifiedName.split("\\+");

            switch (tileTerrainNames[0]) {
                case "JUNGLE":
                    terrainHexA = Terrain.JUNGLE;
                    break;
                case "GRASS":
                    terrainHexA = Terrain.GRASS;
                    break;
                case "ROCK":
                    terrainHexA = Terrain.ROCKY;
                    break;
                case "LAKE":
                    terrainHexA = Terrain.LAKE;
                    break;
                case "PADDY":
                    terrainHexA = Terrain.PADDY;
            }

            switch (tileTerrainNames[1]) {
                case "JUNGLE":
                    terrainHexB = Terrain.JUNGLE;
                    break;
                case "GRASS":
                    terrainHexB = Terrain.GRASS;
                    break;
                case "ROCK":
                    terrainHexB = Terrain.ROCKY;
                    break;
                case "LAKE":
                    terrainHexB = Terrain.LAKE;
                    break;
                case "PADDY":
                    terrainHexB = Terrain.PADDY;
            }

            Hex hexA = new Hex(terrainHexA, tileCount);
            Hex hexB = new Hex(terrainHexB, tileCount);
            nextTile = new Tile(hexA,hexB,tileCount, Orientation.N);
            tileCount++;

            manager.getBoard().getDeck().setTopTile(nextTile);
            //
            //This may or not be the correct way of calling the AI to place the Tile and select a build action
            //
            manager.selectAndPlayMove();
            manager.selectAndPlayMove();

        }

        //Enter message for tournament. Includes Player ID
        check = input[0].equals("WAIT")&&input[1].equals("FOR")&&input[2].equals("THE")&&input[3].equals("TOURNAMENT")&&input[4].equals("TO")&&input[5].equals("BEGIN");
        if(check){
            pid = input[6];
            //System.out.println("client pid: "+pid);
        }

        //
        check = input[0].equals("NEW")&&input[1].equals("CHALLENGE");
        if(check){
            cid = input[2];
            rounds = Integer.parseInt(input[6]);
        }

        //
        check = input[0].equals("BEGIN")&&input[1].equals("ROUND");
        if(check){
            rid = Integer.parseInt(input[2]);
            rounds = Integer.parseInt(input[4]);
        }

        //
        check = input[0].equals("NEW")&&input[1].equals("MATCH")&&input[2].equals("BEGINNING")&&input[3].equals("NOW");
        if(check) {
            pidOpponent = input[8];
        }

        //Received move
        check = input[0].equals("GAME")&&input[2].equals("MOVE")&&input[4].equals("PLAYER");
        if(check) {

            //gid = input[1];
            moveNumber = input[3];
            pidFound = input[5];
            tileUnifiedName = input[7];

            System.out.println("Message parsed as move by " + threadName + " with game " + gid + " and player id " + pid);

            if(pidFound.equals(pid)){
                //do nothing

                check = input[6].equals("FORFEITED:")||input[6].equals("LOST:");
                if(check){
                    System.out.println("Reseting game " + gid);
                    manager = new GameManager(true,this);
                    gid = "empty";
                    games.setGameID(threadName, "empty");
                }

            }
            else {
                check = input[6].equals("PLACED") && input[8].equals("AT");
                if (check) {
                    String tileTerrainNames[] = tileUnifiedName.split("\\+");

                    switch (tileTerrainNames[0]) {
                        case "JUNGLE":
                            terrainHexA = Terrain.JUNGLE;
                            break;
                        case "GRASS":
                            terrainHexA = Terrain.GRASS;
                            break;
                        case "ROCK":
                            terrainHexA = Terrain.ROCKY;
                            break;
                        case "LAKE":
                            terrainHexA = Terrain.LAKE;
                            break;
                        case "PADDY":
                            terrainHexA = Terrain.PADDY;
                    }

                    switch (tileTerrainNames[1]) {
                        case "JUNGLE":
                            terrainHexB = Terrain.JUNGLE;
                            break;
                        case "GRASS":
                            terrainHexB = Terrain.GRASS;
                            break;
                        case "ROCK":
                            terrainHexB = Terrain.ROCKY;
                            break;
                        case "LAKE":
                            terrainHexB = Terrain.LAKE;
                            break;
                        case "PADDY":
                            terrainHexB = Terrain.PADDY;
                    }

                    Hex hexA = new Hex(terrainHexA, tileCount);
                    Hex hexB = new Hex(terrainHexB, tileCount);
                    nextTile = new Tile(hexA, hexB, tileCount, Orientation.N);
                    tileCount++;
                    orientation = Integer.parseInt(input[12]) - 1;
                    manager.getBoard().getDeck().setTopTile(nextTile);
                    Point3D tilePlacementPoint3d = new Point3D(Integer.parseInt(input[9]), Integer.parseInt(input[10]), Integer.parseInt(input[11]));
                    HexButton targetHex = manager.getBoard().getCubicMap().get(tilePlacementPoint3d);
                    TilePlacementMove tilePlacementMove = new TilePlacementMove(null, targetHex, Orientation.values()[orientation]);
                    PlayerMove buildAction = null;

                    //place tile here


                    check2 = input[13].equals("FOUNDED") && input[14].equals("SETTLEMENT");
                    if (check2) {
                        System.out.println("Parsing buildaction as villager placement");
                        Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[16]), Integer.parseInt(input[17]), Integer.parseInt(input[18]));
                        HexButton buildTarget = manager.getBoard().getCubicMap().get(buildPlacementPoint3d);
                        buildAction = new BuildingPlacementMove(null, buildTarget, Building.VILLAGER);
                        //buildPlacementPoint3d to build settlement

                    }

                    check2 = input[13].equals("FOUNDED") && input[14].equals("SHANGRILA");
                    if(check2) {
                        System.out.println("Parsing buildaction as shaman placement");
                        Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[16]), Integer.parseInt(input[17]), Integer.parseInt(input[18]));
                        HexButton buildTarget = manager.getBoard().getCubicMap().get(buildPlacementPoint3d);
                        buildAction = new BuildingPlacementMove(null, buildTarget, Building.SHAMAN);
                        //buildPlacementPoint3d to build settlement

                    }
                    check2 = input[13].equals("EXPANDED") && input[14].equals("SETTLEMENT");
                    if (check2) {
                        System.out.println("Parsing buildaction as expansion");
                        Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[16]), Integer.parseInt(input[17]), Integer.parseInt(input[18]));

                        switch (input[19]) {
                            case "JUNGLE":
                                expandTerrain = Terrain.JUNGLE;
                                break;
                            case "GRASS":
                                expandTerrain = Terrain.GRASS;
                                break;
                            case "ROCK":
                                expandTerrain = Terrain.ROCKY;
                                break;
                            case "LAKE":
                                expandTerrain = Terrain.LAKE;
                                break;
                            case "PADDY":
                                expandTerrain = Terrain.PADDY;
                        }

                        HexButton expansionTarget = manager.getBoard().getCubicMap().get(buildPlacementPoint3d);
                        buildAction = new SettlementExpansionMove(null, manager.getBoard().getSettlementManager().getSettlement(expansionTarget), expandTerrain);

                        //use values to expand settlement here

                    }
                    check2 = input[13].equals("BUILT") && input[14].equals("TOTORO");
                    if (check2) {
                        System.out.println("Parsing buildaction as totoro placement");
                        Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[17]), Integer.parseInt(input[18]), Integer.parseInt(input[19]));
                        buildAction = new BuildingPlacementMove(null, manager.getBoard().getCubicMap().get(buildPlacementPoint3d), Building.TOTORO);

                        //build totoro here
                    }
                    check2 = input[13].equals("BUILT") && input[14].equals("TIGER");
                    if (check2) {
                        System.out.println("Parsing buildaction as tiger placement");
                        Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[17]), Integer.parseInt(input[18]), Integer.parseInt(input[19]));
                        buildAction = new BuildingPlacementMove(null, manager.getBoard().getCubicMap().get(buildPlacementPoint3d), Building.TIGER);

                        //build tiger here

                    }

                    manager.processTurn(tilePlacementMove, buildAction);
                } else {
                    //Opponent either forfeited or lost
                    System.out.println("\033[0;35mOpponent Lost/Game over\033[0m");
                    System.out.println("Reseting game " + gid);
                    manager = new GameManager(true, this);
                    gid = "empty";
                    games.setGameID(threadName, "empty");
                }
            }

        }

        check = input[0].equals("GAME") && input[2].equals("OVER") && input[3].equals("SEND") && input[4].equals("OUTCOME");
        if(check) {
            gid = input[4];
            pid = getPid();
            pidOpponent = getPidOpponent();
            Player player1 = manager.getBoard().getPlayer1();
            Player player2 = manager.getBoard().getPlayer2();

            String outputMessage = sendAction(pid, pidOpponent, player1, player2);
            sendToQueue(outputMessage);

            manager = new GameManager(true,this);
            gid = "empty";
            games.setGameID(threadName, "empty");

        }

        check = input[0].equals("GAME")&&input[2].equals("OVER");
        if(check){
            //gid = input[1];
            //pid = input[4];
            score1 = input[5];
            pidOpponent = input[7];
            score2 = input[8];

            System.out.println("Reseting game " + gid);
            manager = new GameManager(true,this);
            gid = "empty";
            games.setGameID(threadName, "empty");

        }
        check = input[0].equals("END")&&input[1].equals("OF")&&input[2].equals("ROUND");
        if(check){
            rid = Integer.parseInt(input[3]);
            rounds = Integer.parseInt(input[5]);
            //System.out.println("Round over. Round ID: "+ input[3]);
        }

    }



    public void extractAndSendAction(TilePlacementMove tilePlacement, PlayerMove buildAction)
    {
        HexButton tileHex = tilePlacement.getTargetHex();
        Orientation orientation = tilePlacement.getOrientation();
        String sendActionResult = "";
        if(buildAction instanceof BuildingPlacementMove)
        {
            Building building = ((BuildingPlacementMove) buildAction).getBuilding();
            HexButton targetHex = ((BuildingPlacementMove) buildAction).getTargetHex();
            sendActionResult = sendAction(tileHex, orientation.ordinal()+1, building, targetHex);
        }
        else if(buildAction instanceof SettlementExpansionMove)
        {
            Settlement settlement = ((SettlementExpansionMove) buildAction).getSettlement();
            HexButton targetHex = settlement.getHexes().get(0);
            Terrain terrain = ((SettlementExpansionMove) buildAction).getTerrain();
            sendActionResult = sendAction(tileHex, orientation.ordinal()+1, terrain, targetHex);
        }

        sendToQueue(sendActionResult);
    }

    // Send scores case
    public String sendAction(String playerID1, String playerID2, Player player1, Player player2) {
        String outputMessage = "GAME " + gid + " PLAYER ";
        if(player1.getName() == playerID1) {
            outputMessage += playerID1 + player1.getScore() + " PLAYER " + playerID2 + player2.getScore();
        }
        else {
            outputMessage += playerID2 + player1.getScore() + " PLAYER " + playerID1 + player2.getScore();

        }
        return outputMessage;
    }


    //unable to build case
    public void extractAndSendAction(TilePlacementMove tilePlacement){
        HexButton tileHex = tilePlacement.getTargetHex();
        Orientation orientation = tilePlacement.getOrientation();
        String sendActionResult = sendAction(tileHex, orientation.ordinal()+1);

        sendToQueue(sendActionResult);

        System.out.println("Reseting game " + gid);
        manager = new GameManager(true,this);
        gid = "empty";
        games.setGameID(threadName, "empty");

    }

    //Normal build actions
    public String sendAction(HexButton tileLocation, int orientation, Building buildingType, HexButton buildLocation){
        String outputMessage = "GAME " + gid + " MOVE " + moveNumber + " PLACE " + tileUnifiedName;
        outputMessage += " AT " + (int) tileLocation.getABCPoint().getX() + " " + (int) tileLocation.getABCPoint().getY() + " " + (int) tileLocation.getABCPoint().getZ() + " " + orientation;
        if(buildingType.equals(Building.VILLAGER))
        {
            outputMessage += " FOUND SETTLEMENT AT ";
        }
        else if(buildingType.equals(Building.TIGER))
        {
            outputMessage += " BUILD TIGER PLAYGROUND AT ";
        }
        else if(buildingType.equals(Building.TOTORO)){
            outputMessage += " BUILD TOTORO SANCTUARY AT ";
        }
        outputMessage += (int) buildLocation.getABCPoint().getX() + " " + (int) buildLocation.getABCPoint().getY() + " " + (int) buildLocation.getABCPoint().getZ();
        //System.out.println("Returned: " + outputMessage);
        return outputMessage;
    }
    //Expansion case
    public String sendAction(HexButton tileLocation, int orientation, Terrain terrainType, HexButton settlementLocation){
        String outputMessage = "GAME " + gid + " MOVE " + moveNumber + " PLACE " + tileUnifiedName;
        outputMessage += " AT " + (int) tileLocation.getABCPoint().getX() + " " + (int) tileLocation.getABCPoint().getY() + " " + (int) tileLocation.getABCPoint().getZ() + " " + orientation;
        outputMessage += " EXPAND SETTLEMENT AT " + (int) settlementLocation.getABCPoint().getX() + " " + (int) settlementLocation.getABCPoint().getY() + " " + (int) settlementLocation.getABCPoint().getZ();
        String temp = terrainType.name();
        if(temp.equals("ROCKY")){
            temp = "ROCK";          //since the server uses rock instead of rocky
        }
        outputMessage += " " + temp;
        //System.out.println("Returned: " + outputMessage);
        return outputMessage;
    }
    //Unable to build case
    public String sendAction(HexButton tileLocation, int orientation){
        String outputMessage = "GAME " + gid + " MOVE " + moveNumber + " PLACE " + tileUnifiedName;
        outputMessage += " AT " + (int) tileLocation.getABCPoint().getX() + " " + (int) tileLocation.getABCPoint().getY() + " " + (int) tileLocation.getABCPoint().getZ() + " " + orientation;
        outputMessage += " UNABLE TO BUILD";
        return outputMessage;
    }


    public String sendTournamentPassword(String tournamentPassword){
        String outputMessage = "ENTER THUNDERDOME " + tournamentPassword;

        return outputMessage;
    }

    public String sendLoginCredentials(String username, String userPassword){
        String outputMessage = "I AM " + username + " " + userPassword;

        return outputMessage;
    }


    public void sendToQueue(String message) {
        try {
            clientToServer.put( message );
            synchronized (clientToServer) {
                clientToServer.notifyAll();
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String getPid(){ return pid;}
    public String getPidOpponent(){return pidOpponent;}
    public String getGid() { return gid;}
    public String getCid(){ return cid;}
    public int getRounds(){ return rounds;}
}
