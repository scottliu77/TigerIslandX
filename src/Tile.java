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
    private int orientation;

    // Orientation = the position of hexA relative to the volcano
    // The position of hexB relative to the volano is always orientation + 1

    public Tile(Hex hexA, Hex hexB)
    {
        volcano = new VolcanoHex();
        this.hexA = hexA;
        this.hexB = hexB;
        orientation = 0;
    }

    public void rotRight()
    {
        orientation++;
        if (orientation > 5) orientation = 0;
    }

    public void rotLeft()
    {
        orientation--;
        if (orientation < 0) orientation = 5;
    }

    public int getOrientation()
    {
        return orientation;
    }

    // NEVER MODIFY ORIENTATION OUTSIDE THE TILE CLASS!
    // If you need to modify a state by n places, ALWAYS use getOrientationPlus(n) or call rotLeft/rotRight in a loop
    // If you try to just use getOrientation() then modify that, it opens the door to a lot of errors
    public int getOrientationPlus(int number)
    {
        return (orientation + number) % 6;
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

    // I began working on this Orientation class to prevent errors from doing math on the current state and forgetting to mod
    // Currently unused, so make sure you use getOrientationPlus() whenever you want to get a modified state
    private class Orientation
    {
        int state;

        private static final int MIN = 0;
        private static final int MAX = 5;

        public Orientation()
        {
            state = 0;
        }

        public int getState()
        {
            return state;
        }

        public void nextState()
        {
            state++;
            if(state > MAX) state = MIN;
        }

        public void prevState()
        {
            state--;
            if(state < MIN) state = MAX;
        }

        public void modifyState(int modifier)
        {
            state += modifier;
            state %= MAX - 1;
        }

    }
}

