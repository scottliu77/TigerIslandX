import java.util.ArrayList;

/**
 * Created by Wylie on 3/26/2017.
 */
public class MoveAnalyzer
{
    private Board board;
    ArrayList<HexButton> overallMoveset;
    ArrayList<HexButton> legalMoves;
    ArrayList<HexButton> legalTilePlacements;
    ArrayList<HexButton> legalTotoroPlacements;
    ArrayList<HexButton> legalVillagerPlacements;
    ArrayList<HexButton> legalTigerPlacements;
    ArrayList<HexButton> legalSettlementExpansions;

    public MoveAnalyzer(Board board)
    {
        this.board = board;
    }

    public void updateMoveset()
    {
        overallMoveset = new ArrayList<HexButton>(board.getButtonMap().values());
    }

    public void updateLegalMoves()
    {
        legalMoves = new ArrayList<HexButton>();
    }


}
