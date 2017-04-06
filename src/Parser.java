import java.awt.*;

public class Parser{
    private String input[];
    private String output;
    private GameManager manager;

    private int challenges;
    private int cid;
    private int gid;
    private int orientation;
    private int pid;
    private int pidOpponent;
    private int rid;
    private int rounds;
    private int score;
    private Terrain expandTerrain;
    private Terrain terrainHexA;
    private Terrain terrainHexB;
    private Tile nextTile;
    private String tileUnifiedName;
    private int time;
    private int moveNumber;
    private int tileCount = 2;




    public void Parser(){   }

    public void Parser(GameManager m){
        manager = m;
    }

    public void receiveMessage(String inputMessage){
        boolean check;
        input = inputMessage.split(" ");
        check = input[0].equals("MAKE")&&input[1].equals("YOUR")&&input[2].equals("MOVE")&&input[3].equals("IN");
        if(check){
            gid = Integer.parseInt(input[5]);
            time = Integer.parseInt(input[7]);
            moveNumber = Integer.parseInt(input[10]);
            tileUnifiedName = input[12];
            String tileTerrainNames[] = tileUnifiedName.split("\\+");
            if(tileTerrainNames[0].equals("JUNGLE")){ terrainHexA = Terrain.JUNGLE;  }
            else if(tileTerrainNames[0].equals("GRASS")){ terrainHexA = Terrain.GRASS;  }
            else if(tileTerrainNames[0].equals("ROCKY")){ terrainHexA = Terrain.ROCKY;  }
            else if(tileTerrainNames[0].equals("LAKE")){ terrainHexA = Terrain.LAKE;  }
            if(tileTerrainNames[1].equals("JUNGLE")){ terrainHexB = Terrain.JUNGLE;  }
            else if(tileTerrainNames[1].equals("GRASS")){ terrainHexB = Terrain.GRASS;  }
            else if(tileTerrainNames[1].equals("ROCKY")){ terrainHexB = Terrain.ROCKY;  }
            else if(tileTerrainNames[1].equals("LAKE")){ terrainHexB = Terrain.LAKE;  }
            Hex hexA = new Hex(terrainHexA, tileCount);
            Hex hexB = new Hex(terrainHexB, tileCount);
            nextTile = new Tile(hexA,hexB,tileCount, Orientation.N);
                //
                //The AI should use place this tile at this point. (by putting it in deck or something similar.
                //
            tileCount++;


        }
        check = input[0].equals("WAIT")&&input[1].equals("FOR")&&input[2].equals("THE")&&input[3].equals("TOURNAMENT")&&input[4].equals("TO")&&input[5].equals("BEGIN");
        if(check){
            pid = Integer.parseInt(input[6]);
        }
        check = input[0].equals("NEW")&&input[1].equals("CHALLENGE");
        if(check){
            cid = Integer.parseInt(input[2]);
            rounds = Integer.parseInt(input[6]);
        }
        check = input[0].equals("BEGIN")&&input[1].equals("ROUND");
        if(check){
            rid = Integer.parseInt(input[2]);
            rounds = Integer.parseInt(input[4]);
        }
        check = input[0].equals("NEW")&&input[1].equals("MATCH")&&input[2].equals("BEGINNING")&&input[3].equals("NOW");
        if(check){
            pidOpponent = Integer.parseInt(input[8]);
        }
    }

    public String sendPlaceAction(Tile placingTile, Point location){
        return "";
    }

    public String sendBuildAction(){
        return "";
    }



    public int getPid(){ return pid;}
    public int getPidOpponent(){return pidOpponent;}
    public int getCid(){ return cid;}
    public int getRounds(){ return rounds;}
}