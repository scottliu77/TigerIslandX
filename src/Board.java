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

    public Board(GameManager manager)
    {
        this.manager = manager;

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

        resetWithOneHex();
    }

    public HashMap<Point, HexButton> getButtonMap()
    {
        return buttonMap;
    }

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
    }

    public void resetWithOneHex()
    {
        buttonMap = new HashMap<Point, HexButton>();

        Point point = new Point(256 + 236, 128 + 236);
        buttonMap.put(point, new HexButton(point, new EmptyHex(), this));
    }

    public void placeTile(Point origin)
    {
        Tile tile = deck.getTopTile();
        deck.nextTile();

        HexButton centerButton = buttonMap.get(origin);
        centerButton.changeHex(tile.getVolcano());
        placePerimeterHexes(origin);

        placeHex(origin, tile.getA(), tile.getOrientation());
        placeHex(origin, tile.getB(), tile.getOrientationPlus(1));
        manager.updateMenu();
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
        manager.updateHexButtons();
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

    public Deck getDeck()
    {
        return deck;
    }

    public void rotDeckTileRight()
    {
        deck.getTopTile().rotRight();
        manager.updateMenu();
    }

    public void rotDeckTileLeft()
    {
        deck.getTopTile().rotLeft();
        manager.updateMenu();
    }

    public Hex getHoverHex()
    {
        return hoverHex;
    }

    public void setHoverHexButton(HexButton button)
    {
        hoverHexButton = button;
        manager.updateMenu();
    }

    public HexButton getHoverHexButton()
    {
        return hoverHexButton;
    }
}
