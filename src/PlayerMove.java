import java.awt.*;

/**
 * Created by Wylie on 3/22/2017.
 */
interface PlayerMove
{
    void execute(Board board);
}

class TilePlacementMove implements PlayerMove
{
    private Player player;
    private Point targetPoint;
    private Orientation orientation;

    public TilePlacementMove(Player player, Point targetPoint, Orientation orientation)
    {
        this.player = player;
        this.targetPoint = targetPoint;
        this.orientation = orientation;
    }

    public void execute(Board board)
    {
        board.placeTile(targetPoint, orientation);
    }
}

class BuildingPlacementMove implements PlayerMove
{
    private Point targetPoint;
    private Building building;

    public BuildingPlacementMove(Player player, Point targetPoint, Building building)
    {
        this.targetPoint = targetPoint;
        this.building = building;
    }

    public void execute(Board board)
    {
        board.placeBuilding(targetPoint, building);
    }
}

class SettlementExpansionMove implements PlayerMove
{
    //private Point targetPoint;
    private Settlement settlement;
    private Terrain terrain;

    public SettlementExpansionMove(Player player, Settlement settlement, Terrain terrain)
    {
        this.settlement = settlement;
        this.terrain = terrain;
    }

    public Settlement getSettlement()
    {
        return settlement;
    }

    public void execute(Board board) { board.expandSettlement(settlement, terrain);}
}
