import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by Wylie on 3/23/2017.
 */
public class MyStepdefs {

    Tile tile;
    Hex target;

    Hex neighbor0;

    Hex neighbor1;

    HexButton targetButton;
    HexButton neighbor0Button;
    HexButton neighbor1Button;

    GameManager manager;
    Board board;

    private static final int BOX_SIZE = 40;
    private static final int X_OFFSET = BOX_SIZE * 3 / 4;
    private static final int Y_OFFSET = BOX_SIZE / 2;

    private static final Point center = new Point(0, 0);

    private static final Point[] posPoints =
            {
                    new Point(center.x, center.y - BOX_SIZE),
                    new Point(center.x + X_OFFSET, center.y - Y_OFFSET),
                    new Point(center.x + X_OFFSET, center.y + Y_OFFSET),
                    new Point(center.x, center.y + BOX_SIZE),
                    new Point(center.x - X_OFFSET, center.y + Y_OFFSET),
                    new Point(center.x - X_OFFSET, center.y - Y_OFFSET)
            };

    @Given("^Hex and neighbors initialized$")
    public void hexAndNeighborsInitialized() throws Throwable
    {
        manager = new GameManager();
        board = manager.getBoard();
        tile = board.getDeck().getTopTile();

        HashMap<Point, HexButton> buttonMap = board.getButtonMap();
        ArrayList<Point> pointList = new ArrayList<Point>(buttonMap.keySet());
        targetButton = buttonMap.get(pointList.get(0));
    }

    @When("^Player selects a target Hex$")
    public void playerSelectsATargetHex() throws Throwable
    {
        board.placeTile(targetButton.getOrigin());
    }

    @Then("^Volcano placed at target Hex$")
    public void volcanoPlacedAtTargetHex() throws Throwable
    {
        assertEquals(targetButton.getHex(), tile.getVolcano());
    }

    @And("^Tile's Hex A placed at Volcano's neighbor (\\d+)$")
    public void tileSHexAPlacedAtVolcanoSNeighbor(int arg0) throws Throwable
    {
        Point targetPoint = targetButton.getOrigin();
        Point pointA = new Point(targetPoint.x + posPoints[0].x, targetPoint.y + posPoints[0].y);
        neighbor0Button = board.getHexButton(pointA);
        assertEquals(tile.getA(), neighbor0Button.getHex());
    }

    @And("^Tile's Hex B placed at Volcano's neighbor (\\d+)$")
    public void tileSHexBPlacedAtVolcanoSNeighbor(int arg0) throws Throwable
    {
        Point targetPoint = targetButton.getOrigin();
        Point pointB = new Point(targetPoint.x + posPoints[1].x, targetPoint.y + posPoints[1].y);
        neighbor1Button = board.getHexButton(pointB);
        assertEquals(tile.getB(), neighbor1Button.getHex());
    }
}
