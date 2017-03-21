import java.util.Random;

/**
 * Created by Wylie on 3/14/2017.
 */

// A Tile is a group of 3 adjacent Hexes: one Volcano and two non-Volcanos
public class Tile
{
    private VolcanoHex volcano;
    private Hex hexA;
    private Hex hexB;
    private Orientation orientation;
    private int tileId;

    private static final int MIN = 0;
    private static final int MAX = 5;

    // Orientation = the position of hexA relative to the volcano
    // The position of hexB relative to the volano is always orientation + 1

    public Tile(Hex hexA, Hex hexB, int tileId, Orientation orientation)
    {
        this.tileId = tileId;
        volcano = new VolcanoHex();
        volcano.setTileId(tileId);
        this.hexA = hexA;
        hexA.setTileId(tileId);
        this.hexB = hexB;
        hexB.setTileId(tileId);
        this.orientation = orientation;
    }

    public void rotRight()
    {
        orientation = orientation.rotRight();
    }

    public void rotLeft()
    {
        orientation = orientation.rotLeft();
    }

    public int getOrientation()
    {
        return orientation.getAsNum();
    }

    // NEVER MODIFY ORIENTATION OUTSIDE THE TILE CLASS!
    // If you need to modify a state by n places, ALWAYS use getOrientationPlus(n) or call rotLeft/rotRight in a loop
    // If you try to just use getOrientation() then modify that, it opens the door to a lot of errors
    public int getOrientationPlus(int number)
    {
        return orientation.getPlus(number).getAsNum();
    }

    public Hex getVolcano()
    {
        return volcano;
    }

    public Hex getA()
    {
        return hexA;
    }

    public Hex getB()
    {
        return hexB;
    }

    public int getTileId()
    {
        return tileId;
    }

}


// A Tile's Orientation is the position where Hex A will be placed relative to the Tile's Volcano.
// Hex B is placed at the position immediately after Hex A.
enum Orientation
{
    N, NE, SE, S, SW, NW;

    public Orientation rotRight()
    {
        int index = ordinal() + 1;
        return values()[wrapAround(index)];
    }

    public Orientation rotLeft()
    {
        int index = ordinal() - 1;
        return values()[wrapAround(index)];
    }

    public Orientation getPlus(int number)
    {
        int index = ordinal();
        int increment = (number < 0 ? -1 : 1);
        for(int i = 0; i < Math.abs(number); i++)
        {
            index = wrapAround(index + increment);
        }
        return values()[index];
    }

    public int wrapAround(int index)
    {
        if (index < 0) index = values().length - 1;
        if (index >= values().length) index = 0;
        return index;
    }

    public int getAsNum()
    {
        return ordinal();
    }
};