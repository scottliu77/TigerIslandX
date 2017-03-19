import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Wylie on 3/15/2017.
 */
public class Deck
{

    private static final int ROCKY = 0;
    private static final int LAKE = 1;
    private static final int JUNGLE = 2;
    private static final int GRASS = 3;

    private Random rand;

    private Tile topTile;
    private ArrayList<Tile> tiles;

    private int tileCount;

    public Deck()
    {
        rand = new Random();
        tiles = new ArrayList<Tile>();
        tileCount = 0;
        nextTile();
    }

    public void nextTile()
    {
        topTile = new Tile(randomHex(), randomHex(), tileCount);
        tileCount++;
    }

    public Tile getTopTile()
    {
        return topTile;
    }

    public Hex randomHex()
    {
        Hex nextHex;
        int number = rand.nextInt(4);

        switch(number)
        {
            case ROCKY:
                nextHex = new RockyHex();
                break;

            case LAKE:
                nextHex = new LakeHex();
                break;

            case JUNGLE:
                nextHex = new JungleHex();
                break;

            case GRASS:
                nextHex = new GrassHex();
                break;

            default:
                nextHex = new EmptyHex();
                break;
        }

        return nextHex;

    }

    public int getTileCount()
    {
        return tileCount;
    }
}
