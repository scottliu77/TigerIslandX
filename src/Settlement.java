import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wylie on 3/24/2017.
 */
public class Settlement
{
    private ArrayList<HexButton> hexes;
    private Player owner;

    private boolean hasTiger;
    private boolean hasTotoro;

    private int settlementId;

    public Settlement(HexButton firstHex, int settlementId)
    {
        hexes = new ArrayList<HexButton>();
        hexes.add(firstHex);
        owner = firstHex.getHex().getOwner();
        this.settlementId = settlementId;
        firstHex.getHex().setSettlementId(settlementId);
        hasTiger = false;
        hasTotoro = false;
    }

    public void checkNeighbors(HexButton startHex, Board board)
    {
        for(int i = 0; i < 6; i++)
        {
            HexButton neighbor = board.getNeighborButton(startHex, i);
            if ( neighbor.getHex().getOwner() == owner && neighbor.getHex().getSettlementId() != settlementId && !hexes.contains(neighbor))
            {
                neighbor.getHex().setSettlementId(settlementId);
                hexes.add(neighbor);
                checkNeighbors(neighbor, board);
            }
        }
    }

    public void checkForTotoro()
    {
        hasTotoro = hasBuilding(Building.TOTORO);
    }

    public void checkForTiger()
    {
        hasTiger = hasBuilding(Building.TIGER);
    }

    public int getSize()
    {
        return hexes.size();
    }

    public int getSettlementId()
    {
        return settlementId;
    }

    public ArrayList<HexButton> getHexes()
    {
        return hexes;
    }

    private boolean hasBuilding(Building building)
    {
        for(HexButton hexButton : hexes)
        {
            if(hexButton.getHex().getBuilding() == building)
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasTotoro()
    {
        checkForTotoro();
        return hasTotoro;
    }

    public boolean hasTiger()
    {
        checkForTiger();
        return hasTiger;
    }

    public Player getOwner()
    {
        return owner;
    }


}
