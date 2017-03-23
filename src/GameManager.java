import java.util.*;
import java.awt.*;

// GameManager is an intermediary class between the TigerPanel and the game Menu.
public class GameManager
{
    private Board board;

    GameManager()
    {
        board = new Board(this);
    }

    public PlayerMove generateMove(Point targetPoint)
    {
        if(!board.getTilePlaced())
        {
            return new TilePlacementMove(board.getActivePlayer(), targetPoint);
        }
        else
        {
            return new BuildingPlacementMove(board.getActivePlayer(), targetPoint, Building.VILLAGE);
        }
    }

    public void processTurn(PlayerMove playerMove)
    {
        board.processTurn(playerMove);
    }

    public void emptyHexes()
    {
        board.clearHexes();
    }

    public void resetHexes()
    {
        board.resetButtonMap();
    }

    public void resetWithOneHex()
    {
        board.resetWithOneHex();
    }

    public Board getBoard()
    {
        return board;
    }

    public HashMap<Point, HexButton> getButtonMap()
    {
        return board.getButtonMap();
    }

    public void resetDeck()
    {
        board.resetDeck();
    }
}
