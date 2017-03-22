import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wylie on 3/14/2017.
 */
public class Board
{
    private GameManager manager;

    Player player1;
    Player player2;
    Player players[];

    private ArrayList<HexButton> hexButtons;
    private HashMap<Point, HexButton> buttonMap;

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    private static final int xOffset = 10;
    private static final int yOffset = 5;

    private Deck deck;

    private boolean tilePlaced;
    private boolean playerTracker;

    private static final Point[] neighborPts =
            {
                new Point(0, -40),
                new Point(30, -20),
                new Point(30, 20),
                new Point(0, 40),
                new Point(-30, 20),
                new Point(-30, -20)
            };

    // Constructor:
    public Board(GameManager manager)
    {
        this.manager = manager;

        hexButtons = new ArrayList<HexButton>();

        deck = new Deck();

        players = new Player[2];
        players[0] = new Player(Color.WHITE);
        players[1] = new Player(Color.BLACK);

        int hexagonX[] = {10, 30, 40, 30, 10, 0};
        int hexagonY[] = {0, 0, 20, 40, 40, 20};
        Polygon hexagon = new Polygon(hexagonX, hexagonY, 6);


        BufferedImage hexTemplate = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        Graphics2D templateG2 = hexTemplate.createGraphics();
        templateG2.setColor(Color.BLACK);
        templateG2.draw(hexagon);

        //g2d.drawImage(hexTemplate, 250, 200, null);

        //resetButtonMap();

        resetMapWithCenterHex();
    }

    // ====================================
    // Resetting and Initialization methods:



    public void resetButtonMap()
    {
        buttonMap = new HashMap<Point, HexButton>();

        int startX = 256;
        int startY = 128;
        int hexBoxSize = 40;
        int hexVertOffset = 20;
        int hexHoriOffset = 30;

        for(int i = startX + xOffset; i <= startX + WIDTH - hexBoxSize; i += hexHoriOffset * 2)
        {
            for(int j = startY + yOffset; j <= startY + HEIGHT - hexBoxSize; j += hexVertOffset * 2)
            {
                Point point = new Point(i, j);
                Hex hex = new Hex(Terrain.EMPTY);
                HexButton hexButton = new HexButton(point, hex, manager);
                buttonMap.put(point, hexButton);
                //System.out.println(point.toString());
                //g2d.drawImage(hexTemplate, i, j, null);
                //g2d.drawImage(hexTemplate, i + 30, j + 20, null);
            }
        }

        for(int i = startX + hexHoriOffset + xOffset; i <= startX + WIDTH - hexBoxSize; i += hexHoriOffset * 2)
        {
            for (int j = startY + hexVertOffset + yOffset; j <= startY + HEIGHT - hexBoxSize; j += hexVertOffset * 2)
            {
                Point point = new Point(i, j);
                Hex hex = new Hex(Terrain.EMPTY);
                HexButton hexButton = new HexButton(point, hex, manager);
                buttonMap.put(point, hexButton);
                //System.out.println(point.toString());
                //g2d.drawImage(hexTemplate, i, j, null);
            }
        }

        resetDeck();
    }

    public void clearHexes()
    {
        resetDeck();
        for(HexButton button : buttonMap.values())
        {
            button.resetButton();
        }
    }

    private void resetMapWithCenterHex()
    {
        buttonMap = new HashMap<Point, HexButton>();

        Point point = new Point(256 + 236, 128 + 236);
        buttonMap.put(point, new HexButton(point, new Hex(Terrain.EMPTY), manager));
    }

    public void resetWithOneHex()
    {
        resetMapWithCenterHex();
        resetDeck();
    }

    public void resetDeck()
    {
        deck.resetTileCount();
    }

    // ====================================
    // Game state management methods:

    public void processTurn(Point origin)
    {
        int playerIndex = playerTracker ? 1 : 0;
        placeTile(origin);
    }

    // ====================================
    // Tile Placement methods:

    public void placeTile(Point origin)
    {
        Tile tile = deck.getTopTile();
        HexButton centerButton = buttonMap.get(origin);

        if(tilePlacementIsLegal(tile, centerButton))
        {
            centerButton.changeHex(tile.getVolcano());
            placePerimeterHexes(origin);

            placeHex(origin, tile.getA(), tile.getOrientation());
            placeHex(origin, tile.getB(), tile.getOrientationPlus(1));

            deck.nextTile();
        }
    }

    private void placeHex(Point center, Hex hex, int orientation)
    {
        Point delta = neighborPts[orientation];
        Point origin = new Point(center.x + delta.x, center.y + delta.y);
        if(buttonMap.containsKey(origin))
        {
            HexButton button = buttonMap.get(origin);
            button.changeHex(hex);
        }
        else
        {
            HexButton button = new HexButton(origin, hex, manager);
            buttonMap.put(origin, button);
        }
        placePerimeterHexes(origin);
    }

    private void placePerimeterHexes(Point center)
    {
        for(int i = 0; i < 6; i++)
        {
            Point neighborPoint = neighborPts[i];
            Point buttonPoint = new Point(center.x + neighborPoint.x, center.y + neighborPoint.y);
            placeIfUnmapped(buttonPoint);
            place2ndLayerPerimeter(buttonPoint);
        }
    }

    private void place2ndLayerPerimeter(Point center)
    {
        for(int i = 0; i < 6; i++)
        {
            Point neighborPoint = neighborPts[i];
            Point buttonPoint = new Point(center.x + neighborPoint.x, center.y + neighborPoint.y);
            placeIfUnmapped(buttonPoint);
        }
    }

    private void placeIfUnmapped(Point buttonPoint)
    {
        if(!buttonMap.containsKey(buttonPoint))
        {
            HexButton button = new HexButton(buttonPoint, new Hex(Terrain.EMPTY), manager);
            buttonMap.put(buttonPoint, button);
        }
    }

    // ====================================
    // Accessors for member data:

    public HashMap<Point, HexButton> getButtonMap()
    {
        return buttonMap;
    }

    private HexButton getNeighborButton(HexButton base, int index)
    {
        if(index < 0 || index > 5)
        {
            System.out.println("Error, invalid neighbor index = " + index);
            return null;
        }
        else
        {
            Point delta = neighborPts[index];
            Point neighborPt = new Point(base.getOrigin().x + delta.x, base.getOrigin().y + delta.y);
            if(!buttonMap.containsKey(neighborPt))
            {
                return new HexButton(neighborPt, new Hex(Terrain.EMPTY), manager);
            }
            else
            {
                return buttonMap.get(neighborPt);
            }
        }
    }

    public Deck getDeck()
    {
        return deck;
    }

    public HexButton getHexButton(Point point){return buttonMap.get(point);}

    public boolean getTilePlaced() { return tilePlaced; }

    public boolean getPlayerTracker() { return playerTracker; }


    // ==============================================
    // Tile Placement legality checking functions:

    private boolean tilePlacementIsLegal(Tile tile, HexButton targetButton)
    {
        if(!targetIsEmptyOrVolcano(targetButton))
        {
            System.out.println("Illegal move: target neither volcano nor empty");
            return false;
        }
        if (targetButton.getHex().getTerrain() == Terrain.VOLCANO)
        {
            if (hexesShareTile(tile, targetButton))
            {
                System.out.println("Illegal move: hexes share tile");
                return false;
            }
            if (!hexesShareLevel(tile, targetButton))
            {
                System.out.println("Illegal move: hexes not same height");
                return false;
            }
        }
        else if (targetButton.getHex().getTerrain() == Terrain.EMPTY && tile.getTileId() != 1)
        {
            if (!allHexesEmpty(tile, targetButton))
            {
                System.out.println("Illegal move: empty/nonempty tile placement");
                return false;
            }
            if (!adjacentToNonEmptyHex(tile, targetButton))
            {
                System.out.println("Illegal move: attempted to make new island");
                return false;
            }
        }

        return true;
    }

    // adjacentToNonEmptyHex returns true if any Fex of the Tile will be adjacent to a non-Empty Hex
    public boolean adjacentToNonEmptyHex(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation();
        int positionB = tile.getOrientationPlus(1);

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        return hasNonEmptyNeighbor(targetButton) || hasNonEmptyNeighbor(buttonA) || hasNonEmptyNeighbor(buttonB);

    }

    // hasNonEmptyNeighbor returns true if any of a Hex's neighbors are not Empty
    public boolean hasNonEmptyNeighbor(HexButton targetButton)
    {
        for(int i = 0; i < 6; i++)
        {
            HexButton neighborButton = getNeighborButton(targetButton, i);
            // If neighborButton's hex is not Empty, return true
            Terrain terrain = neighborButton.getHex().getTerrain();
            if (terrain != Terrain.EMPTY)
            {
                return true;
            }
        }
        // When all neighborButtons have been checked and none were Empty, return false
        return false;
    }

    // allHexesEmpty returns true if you are attempting to place a Tile on three Empty hexes
    public boolean allHexesEmpty(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation();
        int positionB = tile.getOrientationPlus(1);

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        Hex hexV = targetButton.getHex();
        Hex hexA = buttonA.getHex();
        Hex hexB = buttonB.getHex();

        return hexV.getTerrain() == Terrain.EMPTY && hexA.getTerrain() == Terrain.EMPTY && hexB.getTerrain() == Terrain.EMPTY;
    }

    // hexesShareLevel returns true if you are attempting to place a Tile on three hexes of the same level
    public boolean hexesShareLevel(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation();
        int positionB = tile.getOrientationPlus(1);

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        int levelV = targetButton.getHex().getLevel();
        int levelA = buttonA.getHex().getLevel();
        int levelB = buttonB.getHex().getLevel();

        return levelV == levelA && levelA == levelB;
    }

    // hexesShareTile returns true if you are attempting to place a Tile directly over another
    public boolean hexesShareTile(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation();
        int positionB = tile.getOrientationPlus(1);

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        int tileV = targetButton.getHex().getTileId();
        int tileA = buttonA.getHex().getTileId();
        int tileB = buttonB.getHex().getTileId();

        return tileV == tileA && tileV == tileB && tileV != 0;
    }

    // targetIsEmptyOrVolcano returns true if the target hex is Empty or a Volcano
    public boolean targetIsEmptyOrVolcano(HexButton targetButton)
    {
        Hex hex = targetButton.getHex();

        return hex.getTerrain() == Terrain.EMPTY || hex.getTerrain() == Terrain.VOLCANO;
    }
}
