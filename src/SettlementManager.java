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
}
