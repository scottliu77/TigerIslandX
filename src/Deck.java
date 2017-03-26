import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Wylie on 3/15/2017.
 */

// The Deck generates and provides Tiles to be placed.
public class Deck
{

    private static final int ROCKY = 0;
    private static final int LAKE = 1;
    private static final int JUNGLE = 2;
    private static final int GRASS = 3;

    private Random rand;

    private Tile topTile;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private Stack<Tile> randomizedDeck = new Stack();
    private int tileCount;
    private Orientation orientation;

    public Deck()
    {
        resetTileCount();
        /*
        rand = new Random();
        tileCount = 0;
        orientation = Orientation.N;
        for (int hexAVariation = 0; hexAVariation < 4; hexAVariation++){
            for (int hexBVariation = 0; hexBVariation < 4; hexBVariation++){
                for (int numberOfRepeatedTiles = 0; numberOfRepeatedTiles < 3; numberOfRepeatedTiles++){
                    Hex hexAtemp = new Hex(Terrain.values()[hexAVariation],tileCount);
                    Hex hexBtemp = new Hex(Terrain.values()[hexBVariation],tileCount);
                    orientation = Orientation.N;
                    Tile generatedTile = new Tile(hexAtemp, hexBtemp, tileCount, orientation);
                    //System.out.println(""+hexAVariation+hexBVariation+numberOfRepeatedTiles);
                    tiles.add(generatedTile);
                    tileCount++;
                }
            }
        }


        for (int totalNumberOfTiles = 47; totalNumberOfTiles >= 0; totalNumberOfTiles--){
            System.out.println(rand.nextInt(totalNumberOfTiles));
            int selectedTileIndex = rand.nextInt(totalNumberOfTiles);
            randomizedDeck.push(tiles.get(selectedTileIndex));
            tiles.remove(selectedTileIndex);
        }
        topTile = randomizedDeck.peek();
        */
/*

        rand = new Random();
        tiles = new ArrayList<Tile>();
        tileCount = 1;
        orientation = Orientation.N;
        nextTile();*/
    }

    public void nextTile()
    {
        //topTile = new Tile(randomHex(), randomHex(), tileCount, orientation);
        //tileCount++;
        randomizedDeck.pop();
        topTile = randomizedDeck.peek();
    }

    public Tile getTopTile()
    {
        return topTile;
    }

    public Hex randomHex()
    {
        Hex nextHex;
        int number = rand.nextInt(4);

        nextHex = new Hex(Terrain.values()[number], tileCount);


        return nextHex;

    }

    public void rotLeft()
    {
        Tile temp = randomizedDeck.pop();
        temp.rotLeft();
        randomizedDeck.push(temp);
        //orientation = orientation.rotLeft();
    }

    public void rotRight()
    {
        Tile temp = randomizedDeck.pop();
        temp.rotRight();
        randomizedDeck.push(temp);
        //orientation = orientation.rotRight();
    }

    public void resetTileCount()
    {
        rand = new Random();
        tileCount = 0;
        orientation = Orientation.N;
        for (int hexAVariation = 0; hexAVariation < 4; hexAVariation++){
            for (int hexBVariation = 0; hexBVariation < 4; hexBVariation++){
                for (int numberOfRepeatedTiles = 0; numberOfRepeatedTiles < 3; numberOfRepeatedTiles++){
                    Hex hexAtemp = new Hex(Terrain.values()[hexAVariation],tileCount);
                    Hex hexBtemp = new Hex(Terrain.values()[hexBVariation],tileCount);
                    orientation = Orientation.N;
                    Tile generatedTile = new Tile(hexAtemp, hexBtemp, tileCount, orientation);
                    //System.out.println(""+hexAVariation+hexBVariation+numberOfRepeatedTiles);
                    tiles.add(generatedTile);
                    tileCount++;
                }
            }
        }


        for (int totalNumberOfTiles = 47; totalNumberOfTiles >= 1; totalNumberOfTiles--){
            System.out.println(totalNumberOfTiles);
            int selectedTileIndex = rand.nextInt(totalNumberOfTiles);
            System.out.println(selectedTileIndex);
            randomizedDeck.push(tiles.get(selectedTileIndex));
            tiles.remove(selectedTileIndex);
        }
        topTile = randomizedDeck.peek();
    }
}
