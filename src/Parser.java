import javafx.geometry.Point3D;

import java.awt.*;

public class Parser{
    private String input[];
    private String output;
    private GameManager manager;

    private int challenges;
    private String cid;
    private String gid;
    private int orientation;
    private String pid;
    private String pidOpponent;
    private int rid;
    private int rounds;
    private int score1;
    private int score2;
    private Terrain expandTerrain;
    private Terrain terrainHexA;
    private Terrain terrainHexB;
    private Tile nextTile;
    private String tileUnifiedName;
    private int time;
    private int moveNumber;
    private int tileCount = 100;


    public Parser(GameManager m){
        manager = m;
    }

    public void receiveMessage(String inputMessage){
        boolean check;
        boolean check2;
        input = inputMessage.split(" ");

        //Server supplies tile to be placed
        check = input[0].equals("MAKE")&&input[1].equals("YOUR")&&input[2].equals("MOVE")&&input[3].equals("IN");
        if(check){
            gid = input[5];
            time = Integer.parseInt(input[7]);
            moveNumber = Integer.parseInt(input[10]);
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
            }

            Hex hexA = new Hex(terrainHexA, tileCount);
            Hex hexB = new Hex(terrainHexB, tileCount);
            nextTile = new Tile(hexA,hexB,tileCount, Orientation.N);
            tileCount++;

            //manager.getBoard().getDeck().setTopTile(nextTile);
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

        //Received move from opponent
        check = input[0].equals("GAME")&&input[2].equals("MOVE")&&input[4].equals("PLAYER");
        if(check) {
            gid = input[1];
            moveNumber = Integer.parseInt(input[3]);
            pidOpponent = input[5];

            check = input[6].equals("PLACED") && input[8].equals("AT");
            if(check){
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
                }

                Hex hexA = new Hex(terrainHexA, tileCount);
                Hex hexB = new Hex(terrainHexB, tileCount);
                nextTile = new Tile(hexA,hexB,tileCount, Orientation.N);
                tileCount++;
                orientation = Integer.parseInt(input[12]);
                Point3D tilePlacementPoint3d = new Point3D(Integer.parseInt(input[9]),Integer.parseInt(input[10]),Integer.parseInt(input[11]));

                //place tile here


                check2 = input[13].equals("FOUNDED") && input[14].equals("SETTLEMENT");
                if(check2){
                    Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[16]),Integer.parseInt(input[17]),Integer.parseInt(input[18]));
                    Point pixelPoint = HexButton.toPixelPt(buildPlacementPoint3d);
                    //buildPlacementPoint3d to build settlement

                }
                check2 = input[13].equals("EXPANDED") && input[14].equals("SETTLEMENT");
                if(check2){
                    Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[16]),Integer.parseInt(input[17]),Integer.parseInt(input[18]));

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
                    }

                    //use values to expand settlement here

                }
                check2 = input[13].equals("BUILT") && input[14].equals("TOTORO");
                if(check2){
                    Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[17]),Integer.parseInt(input[18]),Integer.parseInt(input[19]));

                    //build totoro here
                }
                check2 = input[13].equals("BUILT") && input[14].equals("TIGER");
                if(check2){
                    Point3D buildPlacementPoint3d = new Point3D(Integer.parseInt(input[17]),Integer.parseInt(input[18]),Integer.parseInt(input[19]));

                    //build tiger here

                }
            }
            else{
                //Opponent either forfeited or lost
            }


        }

        check = input[0].equals("GAME")&&input[2].equals("OVER");
        if(check){
            gid = input[1];
            pid = input[4];
            score1 = Integer.parseInt(input[5]);
            pidOpponent = input[7];
            score2 = Integer.parseInt(input[8]);

        }
        check = input[0].equals("END")&&input[1].equals("OF")&&input[2].equals("ROUND");
        if(check){
            rid = Integer.parseInt(input[3]);
            rounds = Integer.parseInt(input[5]);
            //System.out.println("Round over. Round ID: "+ input[3]);
        }


    }
    //Normal build actions

    public void extractAndSendAction(TilePlacementMove tilePlacement, PlayerMove buildAction)
    {
        HexButton tileHex = tilePlacement.getTargetHex();
        Orientation orientation = tilePlacement.getOrientation();

        if(buildAction instanceof BuildingPlacementMove)
        {
            Building building = ((BuildingPlacementMove) buildAction).getBuilding();
            HexButton targetHex = ((BuildingPlacementMove) buildAction).getTargetHex();
            sendAction(tileHex, orientation.ordinal(), building, targetHex);
        }
        else if(buildAction instanceof SettlementExpansionMove)
        {
            Settlement settlement = ((SettlementExpansionMove) buildAction).getSettlement();
            HexButton targetHex = settlement.getHexes().get(0);
            Terrain terrain = ((SettlementExpansionMove) buildAction).getTerrain();
            sendAction(tileHex, orientation.ordinal(), terrain, targetHex);
        }
    }

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
        System.out.println("Returned: " + outputMessage);
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
        System.out.println("Returned: " + outputMessage);
        return outputMessage;
    }
    //Unable to build case
    public String sendAction(HexButton tileLocation, int orientation){
        String outputMessage = "GAME " + gid + " MOVE " + moveNumber + " PLACE " + tileUnifiedName;
        outputMessage += " AT " + tileLocation.getABCPoint().getX() + " " + tileLocation.getABCPoint().getY() + " " + tileLocation.getABCPoint().getZ() + " " + orientation;
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


    public String getPid(){ return pid;}
    public String getPidOpponent(){return pidOpponent;}
    public String getGid() { return gid;}
    public String getCid(){ return cid;}
    public int getRounds(){ return rounds;}
}
