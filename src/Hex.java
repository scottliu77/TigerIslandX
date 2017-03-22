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

    private int level;
    private int tileId;

    private final String typeName;
    private final Terrain terrain;

    // Hexagon shape definition:
    private static final int hexagonX[] = {10, 30, 40, 30, 10, 0};
    private static final int hexagonY[] = {0, 0, 20, 40, 40, 20};
    private static final Polygon hexagon = new Polygon(hexagonX, hexagonY, 6);

    public Hex(Terrain terrain)
    {
        image = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        hoverImage = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

        this.terrain = terrain;

        // Draw base and hover hex images for buttons:
        g2d.setColor(terrain.getColor());
        g2d.fill(hexagon);
        g2d.setColor(Color.BLACK);
        g2d.draw(hexagon);

        g2d = hoverImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setColor(Color.PINK);
        Stroke stroke = new BasicStroke(2);
        g2d.setStroke(stroke);
        g2d.draw(hexagon);

        // Alternate hover graphic, roughly equivalent to mouse detection box:
        //g2d.setColor(Color.GREEN);
        //g2d.fillOval(5, 5, 32, 32);

        // Set default values:
        tileId = 0;
        level = 0;
        typeName = terrain.toString();
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

    public Terrain getTerrain()
    {
        return terrain;
    }


    public void setLevel(int newLevel)
    {
        this.level = newLevel;
    }

    public void setTileId(int tileId)
    {
        this.tileId = tileId;
    }
}

enum Terrain
{
    ROCKY(Color.GRAY), LAKE(Color.CYAN), JUNGLE(Color.GREEN), GRASS(Color.YELLOW), VOLCANO(Color.RED), EMPTY(Color.WHITE);

    private Color color;

    Terrain(Color color)
    {
        this.color = color;
    }

    public Color getColor(){return color;}
}
