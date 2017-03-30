import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Wylie on 3/24/2017.
 */
public class SettlementManager
{
    private Board board;
    private CopyOnWriteArrayList<Settlement> settlements;
    private int settlementCount;

    private ArrayList<HexButton> expansionHexes;

    public SettlementManager(Board board)
    {
        this.board = board;
        updateSettlements();
    }

    public void updateSettlements()
    {
        settlements = new CopyOnWriteArrayList<Settlement>();
        settlementCount = 0;

        ArrayList<HexButton> hexButtons = new ArrayList<HexButton>(board.getButtonMap().values());

        for (HexButton hexButton : hexButtons)
        {
            hexButton.getHex().setSettlementId(-1);
        }

        for (HexButton hexButton : hexButtons)
        {
            Hex hex = hexButton.getHex();
            if(hex.isOccupied() && hex.getSettlementId() == -1)
            {
                Player owner = hex.getOwner();
                Settlement settlement = new Settlement(hexButton, settlementCount);
                settlementCount++;
                settlements.add(settlement);
                searchAndAddHexes(settlement, hexButton);
            }
        }
    }

    public void searchAndAddHexes(Settlement settlement, HexButton hexButton)
    {
        settlement.checkNeighbors(hexButton, board);
    }

    public CopyOnWriteArrayList<Settlement> getSettlements()
    {
        return settlements;
    }

    public Settlement getSettlement(HexButton hexButton)
    {
        for (Settlement settlement : settlements)
        {
            if(settlement.getHexes().contains(hexButton))
            {
                return settlement;
            }
        }
        return null;
    }

    public Settlement getSettlement(Point origin)
    {
        return getSettlement(board.getHexButton(origin));
    }

    public boolean isInSettlement(HexButton hexButton)
    {
        for (Settlement settlement : settlements)
        {
            if(settlement.getHexes().contains(hexButton))
            {
                return true;
            }
        }
        return false;
    }

    public boolean nukeWillDestroySettlement(HexButton buttonA, HexButton buttonB)
    {
        Settlement settlementA = null;
        Settlement settlementB = null;

        if(isInSettlement(buttonA))
        {
            settlementA = getSettlement(buttonA);
            if(settlementA.getSize() == 1) return true;
        }
        if(isInSettlement(buttonB))
        {
            settlementB = getSettlement(buttonB);
            if(settlementB.getSize() == 1) return true;
        }
        if(settlementA != null && settlementB != null)
        {
            if(settlementA == settlementB && settlementA.getSize() == 2) return true;
        }

        return false;

       // return(settlementA.getSize() == 1 || settlementB.getSize() == 1 || (settlementA == settlementB && settlementA.getSize() == 2));
    }

    public Expansion getExpansion(Settlement settlement, Terrain terrain)
    {
        expansionHexes = new ArrayList<HexButton>();
        ArrayList<HexButton> settlementHexes = settlement.getHexes();
        for (HexButton settlementHex : settlementHexes)
        {
            addEligibleNeighbors(settlementHex, terrain);
        }
        return new Expansion(expansionHexes);
    }

    public void addEligibleNeighbors(HexButton targetButton, Terrain terrain)
    {
        for(int i = 0; i < 6; i++)
        {
            HexButton neighbor = board.getNeighborButton(targetButton, i);
            Hex neighborHex = neighbor.getHex();
            if(!neighborHex.isOccupied() && neighborHex.getTerrain() == terrain && !expansionHexes.contains(neighbor))
            {
                expansionHexes.add(neighbor);
                addEligibleNeighbors(neighbor, terrain);
            }
        }
    }
}

class Expansion
{
    private ArrayList<HexButton> hexes;
    private int cost;
    private int score;

    public Expansion(ArrayList<HexButton> hexButtons)
    {
        this.hexes = hexButtons;
        cost = 0;
        score = 0;
        for(HexButton hex : hexes)
        {
            int level = hex.getHex().getLevel();
            cost += level;
            score += (level * level);
        }
    }

    public ArrayList<HexButton> getHexes()
    {
        return hexes;
    }

    public int getCost()
    {
        return cost;
    }

    public int getScore()
    {
        return score;
    }

}