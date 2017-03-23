import java.awt.*;

/**
 * Created by Wylie on 3/22/2017.
 */
interface PlayerMove
{
    public void execute(Board board);
}

class TilePlacementMove implements PlayerMove
{
    Player player;
    Point targetPoint;

    public TilePlacementMove(Player player, Point targetPoint)
    {
        this.player = player;
        this.targetPoint = targetPoint;
    }

    public void execute(Board board)
    {
        board.placeTile(targetPoint);
    }
}

class BuildingPlacementMove implements PlayerMove
{
    Player player;
    Point targetPoint;
    Building building;

    public BuildingPlacementMove(Player player, Point targetPoint, Building building)
    {
        this.player = player;
        this.targetPoint = targetPoint;
        this.building = building;
    }

    public void execute(Board board)
    {
        board.placeBuilding(targetPoint, building);
        player.consumeMeeples(building, 1);
    }
}
