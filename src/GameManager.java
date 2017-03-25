import java.util.*;
import java.awt.*;

// GameManager is an intermediary class between the TigerPanel and the game Menu.
public class GameManager
{
    private Board board;
    private Building activeBuilding;

    GameManager()
    {
        board = new Board(this);
        activeBuilding = Building.VILLAGER;
    }

    public PlayerMove generateMove(Point targetPoint)
    {
        if(!board.getTilePlaced())
        {
            return new TilePlacementMove(board.getActivePlayer(), targetPoint);
        }
        else
        {
            return new BuildingPlacementMove(board.getActivePlayer(), targetPoint, activeBuilding);
        }
    }

    public boolean moveIsLegal(Point targetPoint)
    {
        HexButton hexButton = board.getHexButton(targetPoint);
        if(!board.getTilePlaced())
        {
            Tile tile = board.getDeck().getTopTile();
            return board.tilePlacementIsLegal(tile, hexButton);
        }
        else
        {
            return board.buildingPlacementIsLegal(activeBuilding, hexButton);
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

    public Building getActiveBuilding()
    {
        return activeBuilding;
    }

    public void setActiveBuilding(Building building)
    {
        activeBuilding = building;
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
