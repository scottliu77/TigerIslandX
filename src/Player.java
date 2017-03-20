import java.awt.*;

/**
 * Created by Wylie on 3/20/2017.
 */
public class Player
{
    private Color color;
    private int score;
    private int villagers;
    private int totoros;

    public Player(Color color)
    {
        this.color = color;
        resetScore();
        resetResources();
    }

    public int getVillagers()
    {
        return villagers;
    }

    public int getTotoros()
    {
        return totoros;
    }

    public void resetResources()
    {
        villagers = 20;
        totoros = 3;
    }

    public void decreaseTotoros()
    {
        totoros--;
    }

    public void decreaseVillagers(int decrement)
    {
        villagers -= decrement;
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
        return (totoros == 0 && villagers == 0);
    }

}
