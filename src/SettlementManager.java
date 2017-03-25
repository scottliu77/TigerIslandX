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
}
