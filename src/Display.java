import javafx.geometry.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

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

class PlayerStatusDisplay extends Display
{
    private static final int WIDTH = 224;
    private static final int HEIGHT = 64;

    private Player player;
    private Graphics2D g2d;

    PlayerStatusDisplay(Player player, Point origin)
    {
        super(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB), origin);
        this.player = player;
        g2d = super.createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

        drawBase();
        drawStats();
    }

    private void drawBase()
    {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
    }

    private void drawStats()
    {
        g2d.setColor(Color.BLACK);
        g2d.drawString(player.getName(), 4, 16);

        int[] meeples = player.getMeeples();
        Color color1 = player.getColor1();
        Color color2 = player.getColor2();

        g2d.setColor(color1);
        g2d.fillOval(64, 8, 32, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawOval(64, 8, 32 - 1, 32 - 1);
        g2d.setColor(color2);
        g2d.drawString(Building.VILLAGER.toString().substring(0, 2), 64 + 8, 8 + 23);

        g2d.setColor(color1);
        g2d.fillOval(112, 8, 32, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawOval(112, 8, 32 - 1, 32 - 1);
        g2d.setColor(color2);
        g2d.drawString(Building.TIGER.toString().substring(0, 2), 112 + 8, 8 + 23);

        g2d.setColor(color1);
        g2d.fillOval(160, 8, 32, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawOval(160, 8, 32 - 1, 32 - 1);
        g2d.setColor(color2);
        g2d.drawString(Building.TOTORO.toString().substring(0, 2), 160 + 8, 8 + 23);

        g2d.setColor(Color.BLACK);
        g2d.drawString("" + meeples[Building.VILLAGER.ordinal()], 64 + 8, 54);
        g2d.drawString("" + meeples[Building.TIGER.ordinal()], 112 + 8, 54);
        g2d.drawString("" + meeples[Building.TOTORO.ordinal()], 160 + 8, 54);
        g2d.drawString("Score: " + player.getScore(), 4, 60);
    }

    public void update()
    {
        drawBase();
        drawStats();
    }
}


class TurnStatusDisplay extends Display
{
    private static final int WIDTH = 128;
    private static final int HEIGHT = 96;

    private Board board;
    private Graphics2D g2d;

    TurnStatusDisplay(Board board)
    {
        super(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB), new Point(832, 16));
        this.board = board;
        g2d = super.createGraphics();
        drawBase();
        drawStrings();
    }

    private void drawBase()
    {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
    }

    private void drawStrings()
    {
        g2d.setColor(Color.BLACK);
        if(board.getGameResult() == null)
        {
            g2d.drawString("Placement: " + (board.getTilePlaced() ? "Build" : "Tile"), 4, 16);
            g2d.drawString("ActvPlayer: " + (board.getActivePlayer().getName()), 4, 32);
            g2d.drawString("ActvBldng: " + board.getActiveBuilding().toString(), 4, 48);
            g2d.drawString("ActvTerrn: " + board.getActiveTerrain().toString(), 4, 64);
            g2d.drawString("ExpandNext: " + board.getExpandNext(), 4, 80);
        }
        else
        {
            g2d.drawString("Game Over!", 4, 16);
            g2d.drawString("Winner: " + board.getWinner().getName(), 4, 32);
            g2d.drawString("End Condn: " + board.getGameResult(), 4, 48);
        }
    }

    public void update()
    {
        drawBase();
        drawStrings();
    }
}


// DeckDisplay shows the Tile that is currently on top of the Deck
class DeckDisplay extends Display
{
    private Deck deck;
    private Graphics2D g2d;

    /*
    private static final int BOX_SIZE = 40;
    private static final int X_OFFSET = BOX_SIZE * 3 / 4;
    private static final int Y_OFFSET = BOX_SIZE / 2;

    private static final Point center = new Point(44, 44);

    private static final Point[] posPoints =
            {
                new Point(center.x, center.y - BOX_SIZE),
                new Point(center.x + X_OFFSET, center.y - Y_OFFSET),
                new Point(center.x + X_OFFSET, center.y + Y_OFFSET),
                new Point(center.x, center.y + BOX_SIZE),
                new Point(center.x - X_OFFSET, center.y + Y_OFFSET),
                new Point(center.x - X_OFFSET, center.y - Y_OFFSET)
            };

     */

    private static final Point center = new Point(44, 44);

    private static final Point[] posPoints =
            {
                    new Point(center.x + -20, center.y + -30),
                    new Point(center.x + 20, center.y + -30),
                    new Point(center.x + 40, center.y + 0),
                    new Point(center.x + 20, center.y + 30),
                    new Point(center.x + -20, center.y + 30),
                    new Point(center.x + -40, center.y + 0)
            };


    public DeckDisplay(Deck deck)
    {
        super(new BufferedImage(129, 129, BufferedImage.TYPE_INT_ARGB), new Point(832, 128));
        g2d = super.createGraphics();

        this.deck = deck;


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

        int orientation = tile.getOrientation().ordinal();
        g2d.drawImage(tile.getA().getImage(), posPoints[orientation].x, posPoints[orientation].y, null);

        orientation = tile.getOrientationPlus(1).ordinal();
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
        super(new BufferedImage(200, 144, BufferedImage.TYPE_INT_ARGB), new Point(816, 336));
        g2d = super.createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

        drawBackground();
    }

    public void drawBackground()
    {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 200, 144);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 199, 143);
    }

    public void update(HexButton hoverButton)
    {
        drawBackground();
        if(hoverButton != null)
        {
            hexButton = hoverButton;
        }
        if(hexButton != null)
        {
            Hex hex = hexButton.getHex();
            Point point = hexButton.getOrigin();
            Point3D abcPt = hexButton.getABCPoint();
            //Point qrPt = hexButton.getQRPoint();
            g2d.drawImage(hex.getImage(), 100, 4, null);
            g2d.setColor(Color.BLACK);
            g2d.drawString("TileId: " + hex.getTileId(), 4, 19);
            g2d.drawString("Level: " + hex.getLevel(), 4, 39);
            g2d.drawString("Building: " + hex.getBuilding().toString(), 4, 59);
            g2d.drawString("Pt: (" + point.x + ", " + point.y + ") == (" + (int) abcPt.getX() + ", " + (int) abcPt.getY() + ", " + (int) abcPt.getZ() + ")", 4, 79);
            //g2d.drawString("("+ HexButton.toPixelPt(abcPt).x + ", " + HexButton.toPixelPt(abcPt).y + ")", 110, 100);
            //g2d.drawString("(" + qrPt.x + ", " + qrPt.y + ")", 110, 100);
            // Outputs for checking QR and ABC coordinate conversions:
            //g2d.drawString("QR: " + qrPt.x + ", " + qrPt.y, 4, 39);
            //g2d.drawString("Origin: (" + point.x + ", " + point.y + ")", 4, 59);
            //g2d.drawString("ABC: " + (int) abcPt.getX() + ", " + (int) abcPt.getY() + ", " + (int) abcPt.getZ(), 4, 79);
            g2d.drawString("Terrain: " + hex.getTypeName(), 4, 99);
            String playerName;
            if(hex.getOwner() == null)
            {
                playerName = "None";
            }
            else
            {
                playerName = hex.getOwner().getName();
            }
            g2d.drawString("Owner: " + playerName, 4, 119);
            g2d.drawString("SettlementId: " + hex.getSettlementId(), 4, 139);
            //System.out.println("Pt: (" + point.x + ", " + point.y + ") == (" + (int) abcPt.getX() + ", " + (int) abcPt.getY() + ", " + (int) abcPt.getZ() + ")");
        }
    }
}

class SettlementsDisplay extends Display
{
    private SettlementManager settlementManager;
    private Graphics2D g2d;

    public SettlementsDisplay(SettlementManager settlementManager)
    {
        super(new BufferedImage(128, 512, BufferedImage.TYPE_INT_ARGB), new Point(64, 16));
        this.settlementManager = settlementManager;
        g2d = super.createGraphics();
        drawBase();
        drawSettlements();
    }

    public void drawBase()
    {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 128, 512);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, 127, 511);
    }

    public void drawSettlements()
    {
        g2d.setColor(Color.BLACK);
        g2d.drawString("Settlements:", 28, 16);
        int y = 32;
        for(Settlement settlement : settlementManager.getSettlements())
        {
            Color color1 = settlement.getOwner().getColor1();
            Color color2 = settlement.getOwner().getColor2();
            g2d.setColor(color1);
            g2d.fillRect(1, y-12, 126, 16);
            g2d.setColor(color2);
            String string = "Settlement " +  settlement.getSettlementId() + " Size: " + settlement.getSize();
            g2d.drawString(string, 4, y);
            g2d.setColor(Color.GRAY);
            g2d.drawRect(0, y-12, 127, 16);
            y += 16;
        }
    }

    public void update()
    {
        drawBase();
        drawSettlements();
    }

}

class MoveAnalyzerDisplay extends Display
{
    private Board board;
    private MoveAnalyzer moveAnalyzer;
    private Graphics2D g2d;

    public MoveAnalyzerDisplay(Board board)
    {
        super(new BufferedImage(192, 128, BufferedImage.TYPE_INT_ARGB), new Point(32, 544));
        g2d = super.getImg().createGraphics();
        this.board = board;
        this.moveAnalyzer = board.getActiveAnalyzer();
        update();
    }

    public void drawBase()
    {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 192, 128);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, 191, 127);
    }

    public void drawStrings()
    {
        g2d.setColor(Color.BLACK);
        g2d.drawString("Possible tile placements: " + moveAnalyzer.getTilePlacements().size(), 4, 16);
        g2d.drawString("Possible villager placements: " + moveAnalyzer.getLegalVillagerPlacements().size(), 4, 32);
        g2d.drawString("Possible tiger placements: " + moveAnalyzer.getLegalTigerPlacements().size(), 4, 48);
        g2d.drawString("Possible totoro placements: " + moveAnalyzer.getLegalTotoroPlacements().size(), 4, 64);
        g2d.drawString("Possible settlement expands: " + moveAnalyzer.getLegalSettlementExpansions().size(), 4, 80);
        g2d.drawString("Active: " + moveAnalyzer.getClass().toString(), 4, 94);
    }

    public void update()
    {
        moveAnalyzer = board.getActiveAnalyzer();
        drawBase();
        drawStrings();
    }
}