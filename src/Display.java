import java.awt.*;
import java.awt.image.BufferedImage;

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
        return img.createGraphics();
    }
}

// BackgroundDisplay constructs and displays the static tigerstripe background
class BackgroundDisplay extends Display
{
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int W_BLACK = 32;
    private static final int W_ORANGE = 32;
    private static final int W_TOTAL = W_BLACK + W_ORANGE;

    BackgroundDisplay()
    {
        super(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB), new Point(0,0));
        Graphics2D g2d = super.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        Color customOrange = new Color(0xFF, 0x99, 0x00);
        for(int i = 0; i <= WIDTH - W_TOTAL; i += W_TOTAL)
        {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, i, WIDTH, W_BLACK);

            g2d.setColor(customOrange);
            g2d.fillRect(0, i + W_BLACK, WIDTH, W_ORANGE);
        }
    }

}

// BoardDisplay is basically just another static background image right now, but might be used later
class BoardDisplay extends Display
{
    //private Board board;
    private static final int SIZE = 512;

    public BoardDisplay(Board board)
    {
        super(new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB), new Point(256, 128));
        Graphics2D g2d = super.createGraphics();
        //g2d.setColor(Color.WHITE);
        g2d.setColor(new Color(0xFF, 0xFF, 0xFF, 0xA0));
        g2d.fillRect(0, 0, SIZE, SIZE);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0,0, SIZE - 1, SIZE - 1);

        //this.board = board;
    }
}

// DeckDisplay shows the Tile that is currently on top of the Deck
class DeckDisplay extends Display
{
    private Deck deck;
    private Graphics2D g2d;

    private Point[] posPoints;
    private Point center;

    private static final int BOX_SIZE = 40;
    private static final int X_OFFSET = BOX_SIZE * 3 / 4;
    private static final int Y_OFFSET = BOX_SIZE / 2;

    public DeckDisplay(Deck deck)
    {
        super(new BufferedImage(129, 129, BufferedImage.TYPE_INT_ARGB), new Point(832, 128));
        g2d = super.createGraphics();

        this.deck = deck;

        center = new Point(44, 44);

        posPoints = new Point[6];
        posPoints[0] = new Point(center.x, center.y - BOX_SIZE);
        posPoints[1] = new Point(center.x + X_OFFSET, center.y - Y_OFFSET);
        posPoints[2] = new Point(center.x + X_OFFSET, center.y + Y_OFFSET);
        posPoints[3] = new Point(center.x, center.y + BOX_SIZE);
        posPoints[4] = new Point(center.x - X_OFFSET, center.y + Y_OFFSET);
        posPoints[5] = new Point(center.x - X_OFFSET, center.y - Y_OFFSET);

        update();
    }

    private void drawBase()
    {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 128, 128);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 128, 128);
        g2d.drawString("Next:", 4, 16);
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
        drawTile();
    }
}

// HexDetailDisplay shows detailed info about the Hex being hovered over
class HexDetailDisplay extends Display
{
    private Graphics2D g2d;
    private HexButton hexButton;

    public HexDetailDisplay()
    {
        super(new BufferedImage(129, 129, BufferedImage.TYPE_INT_ARGB), new Point(832, 384));
        g2d = super.createGraphics();
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

    public void update(HexButton hoverButton)
    {
        //HexButton hoverButton = menu.getHoverHexButton();

        drawBackground();
        if(hoverButton != null)
        {
            hexButton = hoverButton;
        }
        if(hexButton != null)
        {
            Hex hex = hexButton.getHex();
            g2d.drawImage(hex.getImage(), 4, 4, null);
            g2d.setColor(Color.BLACK);
            g2d.drawString("Level: " + hex.getLevel(), 4, 64);
            g2d.drawString("Origin: (" + hexButton.getOrigin().x + ", " + hexButton.getOrigin().y + ")", 4, 84);
            g2d.drawString("TileId: " + hex.getTileId(), 4, 104);
            g2d.drawString("Terrain: " + hex.getTypeName(), 4, 124);
        }
    }
}