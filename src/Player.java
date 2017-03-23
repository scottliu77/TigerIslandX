import java.awt.*;

/**
 * Created by Wylie on 3/20/2017.
 */
public class Player
{
    private Board board;
    private Color color;
    private int score;
    private int[] meeples;

    public Player(Color color)
    {
        this.color = color;
        resetScore();
        resetResources();
    }

    public int getVillagers()
    {
        return meeples[Building.VILLAGE.ordinal()];
    }

    public int getTotoros()
    {
        return meeples[Building.TOTORO.ordinal()];
    }

    public void resetResources()
    {
        meeples = new int[3];
        meeples[Building.VILLAGE.ordinal()] = 20;
        meeples[Building.TIGER.ordinal()] = 2;
        meeples[Building.TOTORO.ordinal()] = 3;
    }

    public void decreaseTotoros()
    {
        meeples[Building.TOTORO.ordinal()]--;
    }

    public void decreaseVillagers(int decrement)
    {
        meeples[Building.VILLAGE.ordinal()] -= decrement;
    }

    public void decreaseTigers() {meeples[Building.TIGER.ordinal()]--;}

    public void consumeMeeples(Building building, int number)
    {
        meeples[building.ordinal()] -= number;
    }

    public void increaseScore(int points)
    {
        score += points;
    }

    public void resetScore()
    {
        score = 0;
    }

    public boolean outOfResources()
    {
        return (meeples[Building.TOTORO.ordinal()] == 0 && meeples[Building.VILLAGE.ordinal()] == 0 && meeples[Building.TIGER.ordinal()] == 0);
    }

    public int getScore()
    {
        return score;
    }

    public void placeTile(Point point)
    {

    }

}
