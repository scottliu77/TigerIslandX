import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Wylie on 3/14/2017.
 */

// A Hex represents a single space on the board.
public class Hex
{
    private BufferedImage image;
    private BufferedImage hoverImage;

    private boolean occupied;

    private int level;
    private int tileId;
    private int settlementId;

    private final String typeName;
    private final Terrain terrain;

    private Player owner;
    private Building building;

    // Hexagon shape definition:
    /*
    private static final int hexagonX[] = {10, 30, 40, 30, 10, 0};
    private static final int hexagonY[] = {0, 0, 20, 40, 40, 20};
    */

    private static final int hexagonX[] = {0, 20, 40, 40, 20, 0};
    private static final int hexagonY[] = {10, 0, 10, 30, 40, 30};
    private static final Polygon hexagon = new Polygon(hexagonX, hexagonY, 6);


    public Hex(Terrain terrain)
    {
        this(terrain, -1);
    }

    public Hex(Terrain terrain, int tileId)
    {

        image = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        hoverImage = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
//
//        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON );



        // Draw base and hover hex images for buttons:

        g2d.setColor(terrain.getColor());
        g2d.fill(hexagon);
        g2d.setColor(Color.BLACK);
        g2d.draw(hexagon);

        g2d = hoverImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setColor(Color.PINK);
        Stroke stroke = new BasicStroke(3);
        g2d.setStroke(stroke);
        g2d.draw(hexagon);

        // Alternate hover graphic, roughly equivalent to mouse detection box:
        //g2d.setColor(Color.GREEN);
        //g2d.fillOval(5, 5, 32, 32);

        // Set default values:
        this.tileId = tileId;
        level = 0;
        this.terrain = terrain;
        typeName = terrain.toString();
        building = Building.NONE;
        owner = null;
        settlementId = -1;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public BufferedImage getHoverImage()
    {
        return hoverImage;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public int getLevel()
    {
        return level;
    }

    public int getTileId()
    {
        return tileId;
    }

    public int getSettlementId() { return settlementId; }

    public Terrain getTerrain()
    {
        return terrain;
    }

    public boolean isOccupied() {return building.occupiesHex();}

    public void setLevel(int newLevel)
    {
        this.level = newLevel;
    }

    public void setSettlementId(int settlementId) { this.settlementId = settlementId; }

    public Building getBuilding() {return building;}

    public void placeBuilding(Building building, Player player)
    {
        if(this.building.occupiesHex())
        {
            System.out.println("Error, build target has existing building");
        }
        this.building = building;
        owner = player;
    }

    public Player getOwner()
    {
        return owner;
    }
}

enum Terrain
{
    ROCKY(Color.GRAY, true), LAKE(Color.CYAN, true), JUNGLE(Color.GREEN, true), GRASS(new Color(0xDF, 0xFF, 0x00), true), VOLCANO(Color.RED, false), EMPTY(Color.WHITE, false);

    private final Color color;
    private final boolean buildable;

    Terrain(Color color, boolean buildable)
    {
        this.color = color;
        this.buildable = buildable;
    }

    public Color getColor(){return color;}

    public boolean isBuildable(){return buildable;}
}

enum Building
{
    VILLAGER(true, false, 1), TIGER(true, true, 75), TOTORO(true, true, 200), NONE(false, false, 0);

    private final boolean occupiesHex;
    private final boolean permanent;
    private final int placeScore;

    Building(boolean occupiesHex, boolean permanent, int placeScore)
    {
        this.occupiesHex = occupiesHex;
        this.permanent = permanent;
        this.placeScore = placeScore;
    }

    public boolean occupiesHex()
    {
        return occupiesHex;
    }

    public boolean isPermanent() {return permanent;}

    public int getPlaceScore() {
        return placeScore;
    }
}
