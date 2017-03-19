import java.util.Random;

/**
 * Created by Wylie on 3/14/2017.
 */
public class Tile
{
    private VolcanoHex volcano;
    private Hex hexA;
    private Hex hexB;
    private int orientation;

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

