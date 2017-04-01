import java.awt.*;

/**
 * Created by Wylie on 3/20/2017.
 */
public class Player
{
    private String name;
    private Board board;
    private Color color1;
    private Color color2;
    private int score;
    private int[] meeples;

    public Player(String name, Color color1, Color color2)
    {
        this.name = name;
        this.color1 = color1;
        this.color2 = color2;
        resetScore();
        resetResources();
    }

    public int getVillagers()
    {
        return meeples[Building.VILLAGER.ordinal()];
    }

    public int getTotoros()
    {
        return meeples[Building.TOTORO.ordinal()];
    }

    public int getTigers()
    {
        return meeples[Building.TIGER.ordinal()];
    }
    
    public String getName()
    {
        return name;
    }

    public void resetResources()
    {
        meeples = new int[3];
        meeples[Building.VILLAGER.ordinal()] = 20;
        meeples[Building.TIGER.ordinal()] = 2;
        meeples[Building.TOTORO.ordinal()] = 3;
    }

    public boolean instaWins()
    {
        boolean outOfVillagers = (meeples[Building.VILLAGER.ordinal()] == 0);
        boolean outOfTigers = (meeples[Building.TIGER.ordinal()] == 0);
        boolean outOfTotoros = (meeples[Building.TOTORO.ordinal()] == 0);

        return outOfVillagers ? (outOfTigers || outOfTotoros) : (outOfTigers && outOfTotoros);
    }

    public void decreaseTotoros()
    {
        meeples[Building.TOTORO.ordinal()]--;
    }

    public void decreaseVillagers(int decrement)
    {
        meeples[Building.VILLAGER.ordinal()] -= decrement;
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

    public void increaseScore(Building building)
    {
        increaseScore(building.getPlaceScore());
    }

    public void resetScore()
    {
        score = 0;
    }

    public boolean outOfResources()
    {
        return (meeples[Building.TOTORO.ordinal()] == 0 && meeples[Building.VILLAGER.ordinal()] == 0 && meeples[Building.TIGER.ordinal()] == 0);
    }

    public int getScore()
    {
        return score;
    }

    public int[] getMeeples()
    {
        return meeples;
    }

    public Color getColor1()
    {
        return color1;
    }

    public Color getColor2()
    {
        return color2;
    }

}
