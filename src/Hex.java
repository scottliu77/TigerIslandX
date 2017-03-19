import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Wylie on 3/14/2017.
 */
public class Hex
{
    private BufferedImage image;
    private BufferedImage hoverImage;
    private Color color;

    private Point origin;
    private int level;
    private int tileId;

    private Hex[] neighbors;


    private String typeName;

    private Point[] posPoints;

    public Hex(Color color)
    {
        this.color = color;

        image = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        hoverImage = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

        // Define the shape of our hexagon:
        int hexagonX[] = {10, 30, 40, 30, 10, 0};
        int hexagonY[] = {0, 0, 20, 40, 40, 20};
        Polygon hexagon = new Polygon(hexagonX, hexagonY, 6);

        // Draw base and hover hex images for buttons:
        g2d.setColor(color);
        g2d.fill(hexagon);
        g2d.setColor(Color.BLACK);
        g2d.draw(hexagon);

        g2d = (Graphics2D) hoverImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setColor(Color.PINK);
        Stroke stroke = new BasicStroke(2);
        g2d.setStroke(stroke);
        g2d.draw(hexagon);

        // Alternate hover graphic, roughly equivalent to mouse detection box:
        //g2d.setColor(Color.GREEN);
        //g2d.fillOval(5, 5, 32, 32);

        // Set default values:
        typeName = "Default Hex";
        tileId = 0;
    }

    public Color getColor()
    {
        return color;
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

    public void setLevel(int newLevel)
    {
        this.level = newLevel;
    }


    public void drawHex(Graphics2D g2d)
    {
        g2d.drawImage(image, origin.x, origin.y, null);
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public void setTileId(int tileId)
    {
        this.tileId = tileId;
    }

    public int getTileId()
    {
        return tileId;
    }

}

class EmptyHex extends Hex
{
    public EmptyHex()
    {
        super(Color.WHITE);
        super.setTypeName("Empty Space");
        this.setLevel(0);
    }
}

class VolcanoHex extends Hex
{
    public VolcanoHex()
    {
        super(Color.RED);
        super.setTypeName("Volcano");
    }
}

class RockyHex extends Hex
{
    public RockyHex()
    {
        //super(Color.GRAY);
        super(new Color(0x66, 0x33, 0x00));
        super.setTypeName("Rocky");
    }
}

class LakeHex extends Hex
{
    public LakeHex()
    {
        super(Color.BLUE);
        super.setTypeName("Lake");
    }
}

class JungleHex extends Hex
{
    public JungleHex()
    {
        super(Color.GREEN);
        super.setTypeName("Jungle");
    }
}

class GrassHex extends Hex
{
    public GrassHex()
    {
        super(Color.YELLOW);
        super.setTypeName("Grass");
    }
}
