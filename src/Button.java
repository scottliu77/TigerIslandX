import javafx.geometry.Point3D;

import java.awt.*;
import java.awt.image.*;
import java.util.Timer;

import static java.lang.Math.sqrt;

// Buttons are components of Menu with which the user can directly interact using the mouse.
public class Button
{
    // Origin and terminal points determine the hitbox boundaries
    private Point origin;
    private Point terminal;

    // The Button can display one of two images: baseImage or hoverImage
    // Which images is displayed is based on the value of the hover boolean
    private BufferedImage baseImage;
    private BufferedImage hoverImage;
    private boolean hover;


    public Button(Point origin, BufferedImage baseImage, BufferedImage hoverImage)
    {
        this.origin = origin;
        this.baseImage = baseImage;
        this.hoverImage = hoverImage;
        // Might need to tweak calculation of terminal point by a few pixels:
        terminal = new Point(origin.x + baseImage.getWidth() - 1, origin.y + baseImage.getHeight() - 1);
        hover = false;
    }

    public void press()
    {
        System.out.println("Attempted to press Button with undefined behavior");
        // Contents of press() should be defined by subclasses
    }

    public void drawButton(Graphics2D g2d)
    {
        BufferedImage img;
        if (hover)
            img = hoverImage;
        else
            img = baseImage;
        g2d.drawImage(img, origin.x, origin.y, null);
    }

    public Point getOrigin()
    {
        return origin;
    }

    public boolean hoverCheck(Point point)
    {
        hover = pointIsOn(point);
        return hover;
    }

    public boolean pointIsOn(Point point)
    {
        return (point.x >= origin.x && point.x <= terminal.x &&
                point.y >= origin.y && point.y <= terminal.y);
    }

    public void setBaseImage(BufferedImage img)
    {
        baseImage = img;
    }

    public void setHoverImage(BufferedImage img)
    {
        hoverImage = img;
    }

    public void setHover(boolean state)
    {
        hover = state;
    }

    public boolean getHover()
    {
        return hover;
    }

    public BufferedImage getBaseImage() { return baseImage; }

    public BufferedImage getHoverImage() { return hoverImage; }
}

class HexButton extends Button
{
    private GameManager manager;
    private Hex hex;

    private Point hitboxOrigin;
    private Point hitboxTerminal;

    private Point3D abcPt;
    private Point qrPt;

    public HexButton(Point origin, Hex hex, GameManager manager)
    {
        super(origin, hex.getImage(), hex.getHoverImage());
        this.hex = hex;
        this.manager = manager;
        hitboxOrigin = new Point(getOrigin().x + 8, getOrigin().y + 4);
        hitboxTerminal = new Point(getOrigin().x + 32, getOrigin().y + 36);
        abcPt = toHexPt(origin);
    }

    static public Point toPixelPt(Point3D hexPt)
    {
        Point offsetPt = GameManager.hexOffsetPoint;

        double q = hexPt.getX();
        double r = hexPt.getZ();
        int x = (int) Math.round(20.0 * sqrt(3) * (q +  (r/2)));
        //double dx = sqrt(3) * (q + q + r) * 10.0;
        //System.out.println("dx = " + (dx + offsetPt.x));
        //double tensqrt3 = 10 * Math.sqrt(3);
        //int x = (int) Math.round(tensqrt3 * (q + q + r));
        int y = (int) Math.round(20.0 * 1.5 * r);

        x += (offsetPt.x);
        y += (offsetPt.y);

        return new Point(x, y);
    }

    public Point3D toHexPt(Point pixelPt)
    {
        double x = pixelPt.x;
        double y = pixelPt.y;
        Point offsetPt = GameManager.hexOffsetPoint;

        //System.out.println("x, y : " + x + ", " + y);

        x -= offsetPt.x;
        y -= offsetPt.y;

        //System.out.println("adj x, y: " + x + ", " + y);

        double q = ((x * (sqrt(3.0) / 3.0) - (y / 3.0)) / 20.0);
        double r = (y * (2.0 / 3.0) / 20.0);

        //System.out.println("q, r : " + q + ", " + r);

        int intQ = (int) Math.round(q);
        int intR = (int) Math.round(r);

        //System.out.println("(int) q, r: " + intQ + ", " + intR);

        qrPt = new Point(intQ, intR);

        int a = intQ;
        int c = intR;
        int b = -a - c;

        return new Point3D(a, b, c);
    }

    public boolean pointIsOn(Point point)
    {
        return (point.x >= hitboxOrigin.x && point.x <= hitboxTerminal.x &&
            point.y >= hitboxOrigin.y && point.y <= hitboxTerminal.y);
        /*
        int radius = 16;
        int centerX = super.getOrigin().x + 5 + radius;
        int centerY = super.getOrigin().y + 5 + radius;
        float dX = centerX - point.x;
        float dY = centerY - point.y;
        return (sqrt((dX*dX) + (dY*dY)) < radius);
        */

    }

    public void changeHex(Hex newHex)
    {
        int previousHexLevel = this.hex.getLevel();

        this.hex = newHex;
        this.hex.setLevel(previousHexLevel+1);

        Graphics2D g2d = hex.getImage().createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        g2d.setColor(Color.BLACK);
        g2d.drawString("" + hex.getLevel(), 27, 17);

        g2d = hex.getHoverImage().createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        g2d.setColor(Color.BLACK);
        g2d.drawString("" + hex.getLevel(), 27, 17);

        super.setBaseImage(hex.getImage());
        super.setHoverImage(hex.getHoverImage());
    }

    public Hex getHex()
    {
        return hex;
    }

    public Point3D getABCPoint()
    {
        return abcPt;
    }

    public void placeBuilding(Building building, Player activePlayer)
    {
        hex.placeBuilding(building, activePlayer);
        Graphics2D g2d = hex.getImage().createGraphics();

        //g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
        //        RenderingHints.VALUE_ANTIALIAS_ON );

        Color baseColor = activePlayer.getColor1();
        Color textColor = activePlayer.getColor2();

        g2d.setColor(baseColor);
        g2d.fillOval(7, 13, 20, 20);
        g2d.setColor(Color.GRAY);
        g2d.drawOval(7, 13, 20, 20);
        g2d.setColor(textColor);
        g2d.drawString(building.toString().substring(0, 2), 11, 28);

        g2d = hex.getHoverImage().createGraphics();

        //g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
        //        RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setColor(baseColor);
        g2d.fillOval(7, 13, 20, 20);
        g2d.setColor(Color.GRAY);
        g2d.drawOval(7, 13, 20, 20);
        g2d.setColor(textColor);
        g2d.drawString(building.toString().substring(0, 2), 11, 28);
    }

    public void resetButton()
    {
        hex = new Hex(Terrain.EMPTY);
        super.setBaseImage(hex.getImage());
        super.setHoverImage(hex.getHoverImage());
    }

    public void press()
    {
        //manager.processTurn(super.getOrigin());
        if(manager.moveIsLegal(super.getOrigin()))
        {
            manager.processTurn(manager.generateMove(super.getOrigin()));
        }
        else
        {
            System.out.println("Move illegal, failed to generate");
        }
    }

    public Point getQRPoint()
    {
        return qrPt;
    }
}


class RotateRightButton extends Button
{
    private Deck deck;

    public RotateRightButton(Point origin, Deck deck)
    {
        super(origin, new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        buildBase();
        buildHover();
        this.deck = deck;
    }

    private void buildBase()
    {
        BufferedImage baseImg = super.getBaseImage();
        Graphics2D g2d = baseImg.createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        drawBaseImage(g2d);
    }

    private void buildHover()
    {
        BufferedImage hoverImg = super.getHoverImage();
        Graphics2D g2d = hoverImg.createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        drawBaseImage(g2d);

        g2d.setColor(Color.BLUE);
        g2d.fillOval(16, 16, 32, 32);
    }

    private void drawBaseImage(Graphics2D g2d)
    {
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 64, 64);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 63, 63);
        g2d.drawString("Rotate R", 4, 16);
    }

    public void press()
    {
        deck.rotRight();
    }
}

class RotateLeftButton extends Button
{
    private Deck deck;

    public RotateLeftButton(Point origin, Deck deck)
    {
        super(origin, new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        buildBase();
        buildHover();
        this.deck = deck;
    }

    private void buildBase()
    {
        BufferedImage baseImg = super.getBaseImage();
        Graphics2D g2d = baseImg.createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        drawBaseImage(g2d);
    }

    private void buildHover()
    {
        BufferedImage hoverImg = super.getHoverImage();
        Graphics2D g2d = hoverImg.createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        drawBaseImage(g2d);

        g2d.setColor(Color.BLUE);
        g2d.fillOval(16, 16, 32, 32);
    }

    private void drawBaseImage(Graphics2D g2d)
    {
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 63, 63);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 63, 63);
        g2d.drawString("Rotate L", 4, 16);
    }


    public void press()
    {
        deck.rotLeft();
    }
}

class RadialButton extends Button
{
    BufferedImage pressImage;

    boolean depressed;

    public RadialButton(Point origin, BufferedImage base, BufferedImage hover, BufferedImage press)
    {
        super(origin, base, hover);
        this.pressImage = press;
        depressed = false;
    }

    public void drawButton(Graphics2D g2d)
    {
        BufferedImage img;
        if (depressed)
            img = pressImage;
        else if (super.getHover())
            img = super.getHoverImage();
        else
            img = super.getBaseImage();
        g2d.drawImage(img, super.getOrigin().x, super.getOrigin().y, null);
    }

    public void setDepressed(boolean nextState)
    {
        this.depressed = nextState;
    }

    public void setPressImage(BufferedImage pressImage)
    {
        this.pressImage = pressImage;
    }

    public BufferedImage getPressImage()
    {
        return pressImage;
    }
}

class ExpansionSelectButton extends RadialButton
{
    private GameManager manager;

    public ExpansionSelectButton(Point origin, GameManager manager)
    {
        super(origin, new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB));
        this.manager = manager;
        buildBase();
        buildHover();
        buildPressed();
    }

    public void buildBase()
    {
        Graphics2D g2d = super.getBaseImage().createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 64, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, 63, 31);
        g2d.setColor(Color.BLACK);
        g2d.drawString("EXPAND", 4, 16);
    }

    public void buildHover()
    {
        Graphics2D g2d = super.getHoverImage().createGraphics();

        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, 64, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, 63, 31);
        g2d.setColor(Color.BLACK);
        g2d.drawString("EXPAND", 4, 16);
    }

    public void buildPressed()
    {
        Graphics2D g2d = super.getPressImage().createGraphics();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 32, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, 31, 31);
        g2d.setColor(Color.WHITE);
        g2d.drawString("EXPAND", 4, 16);
    }

    public void press()
    {
        manager.setExpandNext(true);
    }
}

class MeepleSelectButton extends RadialButton
{
    private GameManager manager;
    private Building building;

    public MeepleSelectButton(Point origin, GameManager manager, Building building)
    {
        super(origin, new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB) );
        this.manager = manager;
        this.building = building;
        buildBase();
        buildHover();
        buildPressed();
    }

    public void buildBase()
    {
        Graphics2D g2d = super.getBaseImage().createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 64, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, 63, 31);
        g2d.setColor(Color.BLACK);
        g2d.drawString(building.toString(), 4, 16);
    }

    public void buildHover()
    {
        Graphics2D g2d = super.getHoverImage().createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, 64, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, 63, 31);
        g2d.setColor(Color.BLACK);
        g2d.drawString(building.toString(), 4, 16);
    }

    public void buildPressed()
    {
        Graphics2D g2d = super.getPressImage().createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 64, 32);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, 63, 31);
        g2d.setColor(Color.WHITE);
        g2d.drawString(building.toString(), 4, 16);
    }

    public void press()
    {
        manager.setActiveBuilding(building);
        manager.setExpandNext(false);
    }

    /*
    public void drawButton(Graphics2D g2d)
    {
        BufferedImage img;
        //if (pressed)
        //    img = pressedImage;
        if (super.getHover())
            img = super.getHoverImage();
        else
            img = super.getBaseImage();
        g2d.drawImage(img, super.getOrigin().x, super.getOrigin().y, null);
    }
    */

 }

 class TerrainSelectButton extends RadialButton
 {
     private Terrain terrain;
     private GameManager manager;

     public TerrainSelectButton(Point origin, GameManager manager, Terrain terrain)
     {
         super(origin, new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB));
         this.manager = manager;
         this.terrain = terrain;
         buildBase();
         buildHover();
         buildPressed();
     }

     public void buildBase()
     {
         Graphics2D g2d = super.getBaseImage().createGraphics();

         g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON );

         g2d.setColor(Color.WHITE);
         g2d.fillRect(0, 0, 64, 32);
         g2d.setColor(Color.GRAY);
         g2d.drawRect(0, 0, 63, 31);
         g2d.setColor(Color.BLACK);
         g2d.drawString(terrain.toString(), 4, 16);
     }

     public void buildHover()
     {
         Graphics2D g2d = super.getHoverImage().createGraphics();

         g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON );

         g2d.setColor(terrain.getColor());
         g2d.fillRect(0, 0, 64, 32);
         g2d.setColor(Color.GRAY);
         g2d.drawRect(0, 0, 63, 31);
         g2d.setColor(Color.BLACK);
         g2d.drawString(terrain.toString(), 4, 16);
     }

     public void buildPressed()
     {
         Graphics2D g2d = super.getPressImage().createGraphics();

         g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON );

         g2d.setColor(terrain.getColor());
         g2d.fillRect(0, 0, 64, 32);
         g2d.setColor(Color.BLACK);
         g2d.drawRect(0, 0, 63, 31);
         g2d.setColor(Color.BLACK);
         g2d.drawString(terrain.toString(), 4, 16);
     }

     public void press()
     {
         manager.setExpandNext(true);
         manager.setActiveTerrain(terrain);
     }

 }

 class PlayAnalyzerMoveButton extends Button
 {
     private Board board;

     public PlayAnalyzerMoveButton(Board board)
     {
         super(new Point(32, 640), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB) );
         this.board = board;
         drawBase();
         drawHover();
     }

     public void drawBase()
     {
         Graphics2D g2d = super.getBaseImage().createGraphics();

         g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON );

         g2d.setColor(Color.WHITE);
         g2d.fillRect(0, 0, 64, 32);
         g2d.setColor(Color.GRAY);
         g2d.drawRect(0, 0, 63, 31);
         g2d.setColor(Color.BLACK);
         g2d.drawString("AI MOVE", 4, 26);
     }

     public void drawHover()
     {

         Graphics2D g2d = super.getHoverImage().createGraphics();

         g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON );

         g2d.setColor(Color.GRAY);
         g2d.fillRect(0, 0, 64, 32);
         g2d.setColor(Color.BLACK);
         g2d.drawRect(0, 0, 63, 31);
         g2d.setColor(Color.WHITE);
         g2d.drawString("AI MOVE", 4, 26);
     }

     public void press()
     {
         board.getActiveAnalyzer().selectAndPlayMove();
     }

 }

 class AutoResolveButton extends Button
 {
     private Board board;

     public AutoResolveButton(Board board)
     {
         super(new Point(96, 640), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB), new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB));
         this.board = board;
         drawBase();
         drawHover();
     }

     public void drawBase()
     {
         Graphics2D g2d = super.getBaseImage().createGraphics();

         g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON );

         g2d.setColor(Color.WHITE);
         g2d.fillRect(0, 0, 64, 32);
         g2d.setColor(Color.GRAY);
         g2d.drawRect(0, 0, 63, 31);
         g2d.setColor(Color.BLACK);
         g2d.drawString("RESOLVE", 4, 26);
     }

     public void drawHover()
     {
         Graphics2D g2d = super.getHoverImage().createGraphics();

         g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON );

         g2d.setColor(Color.GRAY);
         g2d.fillRect(0, 0, 64, 32);
         g2d.setColor(Color.BLACK);
         g2d.drawRect(0, 0, 63, 31);
         g2d.setColor(Color.WHITE);
         g2d.drawString("RESOLVE", 4, 26);
     }

     public void press()
     {
         while(board.getGameResult() == null)
         {
             long startTime = System.currentTimeMillis();
             board.getActiveAnalyzer().selectAndPlayMove();
             board.getActiveAnalyzer().selectAndPlayMove();
             long endTime = System.currentTimeMillis();
             System.out.println("Time elapsed during turn: " + (endTime - startTime) + " ms");
         }
     }
 }

