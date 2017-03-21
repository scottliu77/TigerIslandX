import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wylie on 3/14/2017.
 */
public class Board
{
    Player player1;
    Player player2;

    private ArrayList<HexButton> hexButtons;
    private HashMap<Point, HexButton> buttonMap;

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    private static final int xOffset = 10;
    private static final int yOffset = 5;

    private Deck deck;

    private Hex hoverHex;
    private HexButton hoverHexButton;

    private static final Point[] neighborPts = {
            new Point(0, -40),
            new Point(30, -20),
            new Point(30, 20),
            new Point(0, 40),
            new Point(-30, 20),
            new Point(-30, -20)};

    // Constructor:
    public Board()
    {
        hexButtons = new ArrayList<HexButton>();

        deck = new Deck();

        int hexagonX[] = {10, 30, 40, 30, 10, 0};
        int hexagonY[] = {0, 0, 20, 40, 40, 20};
        Polygon hexagon = new Polygon(hexagonX, hexagonY, 6);


        BufferedImage hexTemplate = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        Graphics2D templateG2 = (Graphics2D) hexTemplate.createGraphics();
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

        for(int i = 256 + xOffset; i <= 256 + WIDTH - 40; i += 60)
        {
            for(int j = 128 + yOffset; j <= 128 + HEIGHT - 40; j += 40)
            {
                Point point = new Point(i, j);
                Hex hex = new EmptyHex();
                HexButton hexButton = new HexButton(point, hex, this);
                buttonMap.put(point, hexButton);
                //System.out.println(point.toString());
                //g2d.drawImage(hexTemplate, i, j, null);
                //g2d.drawImage(hexTemplate, i + 30, j + 20, null);
            }
        }

        for(int i = 256 + 30 + xOffset; i <= 256 + WIDTH - 40; i += 60)
        {
            for (int j = 128 + 20 + yOffset; j <= 128 + HEIGHT - 40; j += 40)
            {
                Point point = new Point(i, j);
                Hex hex = new EmptyHex();
                HexButton hexButton = new HexButton(point, hex, this);
                buttonMap.put(point, hexButton);
                //System.out.println(point.toString());
                //g2d.drawImage(hexTemplate, i, j, null);
            }
        }

        resetDeck();
    }

    public void resetMapWithCenterHex()
    {
        buttonMap = new HashMap<Point, HexButton>();

        Point point = new Point(256 + 236, 128 + 236);
        buttonMap.put(point, new HexButton(point, new EmptyHex(), this));
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

    public void placeHex(Point center, Hex hex, int orientation)
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
            HexButton button = new HexButton(origin, hex, this);
            buttonMap.put(origin, button);
        }
        placePerimeterHexes(origin);
    }

    public void placePerimeterHexes(Point center)
    {
        for(int i = 0; i < 6; i++)
        {
            Point neighborPoint = neighborPts[i];
            Point buttonPoint = new Point(center.x + neighborPoint.x, center.y + neighborPoint.y);
            if(!buttonMap.containsKey(buttonPoint))
            {
                HexButton button = new HexButton(buttonPoint, new EmptyHex(), this);
                buttonMap.put(buttonPoint, button);
            }
            place2ndLayerPerimeter(buttonPoint);
        }
    }

    public void place2ndLayerPerimeter(Point center)
    {
        for(int i = 0; i < 6; i++)
        {
            Point neighborPoint = neighborPts[i];
            Point buttonPoint = new Point(center.x + neighborPoint.x, center.y + neighborPoint.y);
            if(!buttonMap.containsKey(buttonPoint))
            {
                HexButton button = new HexButton(buttonPoint, new EmptyHex(), this);
                buttonMap.put(buttonPoint, button);
            }
        }
    }

    // ====================================
    // Accessors for member data:

    public HashMap<Point, HexButton> getButtonMap()
    {
        return buttonMap;
    }

    public HexButton getNeighborButton(HexButton base, int index)
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
                HexButton newNeighbor = new HexButton(neighborPt, new EmptyHex(), this);
                buttonMap.put(neighborPt, newNeighbor);
                return newNeighbor;
            }
            else
            {
                return buttonMap.get(neighborPt);
            }
        }
    }

    public HexButton getHoverHexButton()
    {
        return hoverHexButton;
    }

    public Deck getDeck()
    {
        return deck;
    }


    public void setHoverHexButton(HexButton button)
    {
        hoverHexButton = button;
    }



    // ==============================================
    // Tile Placement legality checking functions:

    private boolean tilePlacementIsLegal(Tile tile, HexButton targetButton)
    {
        if(!targetIsEmptyOrVolcano(targetButton))
        {
            System.out.println("Illegal move: target neither volcano nor empty");
            return false;
        }
        if (targetButton.getHex().getTypeName().equals("Volcano"))
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
        else if (targetButton.getHex().getTypeName().equals("Empty") && tile.getTileId() != 1)
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

    // adjacentToNonEmptyHex returns true if
    public boolean adjacentToNonEmptyHex(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation();
        int positionB = tile.getOrientationPlus(1);

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        if(hasNonEmptyNeighbor(targetButton))
        {
            return true;
        }

        if(hasNonEmptyNeighbor(buttonA))
        {
            return true;
        }

        if(hasNonEmptyNeighbor(buttonB))
        {
            return true;
        }

        return false;
    }

    // hasNonEmptyNeighbor returns true if any of a hex's neighbors are not Empty
    public boolean hasNonEmptyNeighbor(HexButton targetButton)
    {
        for(int i = 0; i < 6; i++)
        {
            HexButton neighborButton = getNeighborButton(targetButton, i);
            String neighborType = neighborButton.getHex().getTypeName();
            if (!neighborType.equals("Empty")) return true;
        }
        return false;
    }

    // allHexesEmpty returns true if you are attempting to place a Tile on three Empty hexes
    public boolean allHexesEmpty(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation();
        int positionB = tile.getOrientationPlus(1);

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        String tileV = targetButton.getHex().getTypeName();
        String tileA = buttonA.getHex().getTypeName();
        String tileB = buttonB.getHex().getTypeName();

        return(tileV.equals("Empty") && tileA.equals("Empty") && tileB.equals("Empty"));
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

        return (levelV == levelA && levelA == levelB);
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

        return (tileV == tileA && tileV == tileB && tileV != 0);
    }

    // targetIsEmptyOrVolcano returns true if the target hex is Empty or a Volcano
    public boolean targetIsEmptyOrVolcano(HexButton targetButton)
    {
        Hex hex = targetButton.getHex();
        String hexType = hex.getTypeName();
        return (hexType.equals("Empty") || hexType.equals("Volcano"));
    }
}
