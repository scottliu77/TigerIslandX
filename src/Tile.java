/**
 * Created by Wylie on 3/14/2017.
 */

// A Tile is a group of 3 adjacent Hexes: one Volcano and two non-Volcanos
public class Tile
{
    private Hex volcano;
    private Hex hexA;
    private Hex hexB;
    private Orientation orientation;
    private int tileId;

    // Orientation = the position of hexA relative to the volcano
    // The position of hexB relative to the volcano is always orientation + 1

    public Tile(Hex hexA, Hex hexB, int tileId, Orientation orientation)
    {
        this.tileId = tileId;
        volcano = new Hex(Terrain.VOLCANO, tileId);

        this.hexA = hexA;

        this.hexB = hexB;
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

    public void setOrientation(Orientation orientation) {this.orientation = orientation;}

    public int getOrientation()
    {
        return orientation.ordinal();
    }

    // NEVER MODIFY ORIENTATION OUTSIDE THE TILE CLASS!
    // If you need to modify a state by n places, ALWAYS use getOrientationPlus(n) or call rotLeft/rotRight in a loop
    // If you try to just use getOrientation() then modify that, it opens the door to a lot of errors
    public int getOrientationPlus(int number)
    {
        return orientation.getPlus(number).ordinal();
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
    //0 = N, 1 = NE, etc...
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

}