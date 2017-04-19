import java.util.ArrayList;
import java.util.Random;

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
    private static final int PADDY = 4;

    private Random rand;

    private Tile topTile;
    private ArrayList<Tile> tiles;

    private int tileCount;
    private Orientation orientation;

    public Deck()
    {
        rand = new Random();
        tiles = new ArrayList<Tile>();
        tileCount = 1;
        orientation = Orientation.N;
        nextTile();
    }

    public void nextTile()
    {
        topTile = new Tile(randomHex(), randomHex(), tileCount, orientation);
        tileCount++;
    }

    public Tile getTopTile()
    {
        return topTile;
    }

    public void setTopTile(Tile topTile)
    {
        this.topTile = topTile;
    }

    public Hex randomHex()
    {
        Hex nextHex;
        int number = rand.nextInt(5);

        nextHex = new Hex(Terrain.values()[number], tileCount);

        return nextHex;
    }

    public void rotLeft()
    {
        topTile.rotLeft();
        orientation = orientation.rotLeft();
    }

    public void rotRight()
    {
        topTile.rotRight();
        orientation = orientation.rotRight();
    }

    public void resetTileCount()
    {
        tileCount = 1;
        orientation = Orientation.N;
        nextTile();
    }

    public Orientation getOrientation()
    {
        return orientation;
    }
}
