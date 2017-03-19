import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Display
{
    private BufferedImage img;
    private Point origin;

    public Display(BufferedImage img, Point origin)
    {
        this.img = img;
        this.origin = origin;
    }

    public void update()
    {
        // This should be defined by a subclass like press() in Button
        // Displays are static by default, so the default update() function just does nothing
    }

    public void setImg(BufferedImage newImg)
    {
        img = newImg;
    }

    public BufferedImage getImg()
    {
        return img;
    }

    public Point getOrigin()
    {
        return origin;
    }

    public void drawImg(Graphics2D g2d)
    {
        g2d.drawImage(img, origin.x, origin.y, null);
    }

    public Graphics2D createGraphics()
    {
        return (Graphics2D) img.createGraphics();
    }
}

class BackgroundDisplay extends Display
{
    BackgroundDisplay()
    {
        super(new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB), new Point(0,0));
        Graphics2D g2d = super.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 1024, 768);

        Color customOrange = new Color(0xFF, 0x99, 0x00);
        for(int i = 0; i <= 1024 - 64; i += 64)
        {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, i, 1024, 32);

            g2d.setColor(customOrange);
            g2d.fillRect(0, i + 32, 1024, 32);
        }
    }

}

class BoardDisplay extends Display
{
    private Board board;

    public BoardDisplay(Board board)
    {
        super(new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB), new Point(256, 128));
        Graphics2D g2d = super.createGraphics();
        //g2d.setColor(Color.WHITE);
        g2d.setColor(new Color(0xFF, 0xFF, 0xFF, 0xA0));
        g2d.fillRect(0, 0, 512, 512);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0,0, 511, 511);

        this.board = board;
    }
}

class DeckDisplay extends Display
{

    private Deck deck;
    private Graphics2D g2d;

    private Point[] posPoints;
    private Point center;

    public DeckDisplay(Deck deck)
    {
        super(new BufferedImage(129, 129, BufferedImage.TYPE_INT_ARGB), new Point(832, 128));
        g2d = super.createGraphics();

        center = new Point(44, 44);

        posPoints = new Point[6];
        posPoints[0] = new Point(center.x, center.y - 40);
        posPoints[1] = new Point(center.x + 30, center.y - 20);
        posPoints[2] = new Point(center.x + 30, center.y + 20);
        posPoints[3] = new Point(center.x, center.y + 40);
        posPoints[4] = new Point(center.x - 30, center.y + 20);
        posPoints[5] = new Point(center.x - 30, center.y - 20);

        drawBase();

        this.deck = deck;
        nextTile();
        update();
    }

    public void nextTile()
    {
        deck.nextTile();
    }

    private void drawBase()
    {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 128, 128);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 128, 128);
        g2d.drawString("Next:", 4, 16);
    }

    private void drawHex()
    {
        //g2d.drawImage(deck.getHex().getImage(), 44, 44, null);
        //g2d.drawString(deck.getHex().getTypeName(), 4, 124);
    }

    private void drawTile()
    {
        Tile tile = deck.getTopTile();
        g2d.drawImage(tile.getVolcano().getImage(), center.x, center.y, null);

        int orientation = tile.getOrientation();
        g2d.drawImage(tile.getA().getImage(), posPoints[orientation].x, posPoints[orientation].y, null);

        orientation = tile.getOrientationPlus(1);
        g2d.drawImage(tile.getB().getImage(), posPoints[orientation].x, posPoints[orientation].y, null);

        g2d.setColor(Color.BLACK);
        g2d.drawString("#" + tile.getTileId(), 4, 124);

    }

    public void update()
    {
        //System.out.println("Updating deck display");
        drawBase();
        //drawHex();
        drawTile();
    }
}

class HexDetailDisplay extends Display
{
    private Hex hex;
    private HexButton hexButton;
    private Board board;
    private Graphics2D g2d;

    public HexDetailDisplay(Board board)
    {
        super(new BufferedImage(129, 129, BufferedImage.TYPE_INT_ARGB), new Point(832, 384));
        this.board = board;
        g2d = (Graphics2D) super.getImg().createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

        drawBackground();
    }

    public void drawBackground()
    {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 128, 128);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 128, 128);
    }

    public void update()
    {
        hexButton = board.getHoverHexButton();

        drawBackground();
        if(hexButton != null)
        {
            hex = hexButton.getHex();
            g2d.drawImage(hex.getImage(), 4, 4, null);
            g2d.setColor(Color.BLACK);
            g2d.drawString("Level: " + hex.getLevel(), 4, 64);
            g2d.drawString("Origin: (" + hexButton.getOrigin().x + ", " + hexButton.getOrigin().y + ")", 4, 84);
            g2d.drawString("TileId: " + hex.getTileId(), 4, 104);
            g2d.drawString("Terrain: " + hex.getTypeName(), 4, 124);
        }
    }
}