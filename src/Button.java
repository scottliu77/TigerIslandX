import java.awt.*;
import java.awt.image.*;

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
    private Board board;
    private Hex hex;

    public HexButton(Point origin, Hex hex, Board board)
    {
        super(origin, hex.getImage(), hex.getHoverImage());
        this.hex = hex;
        this.board = board;
    }

    public boolean pointIsOn(Point point)
    {
        int radius = 16;
        int centerX = super.getOrigin().x + 5 + radius;
        int centerY = super.getOrigin().y + 5 + radius;
        float dX = centerX - point.x;
        float dY = centerY - point.y;
        return (sqrt((dX*dX) + (dY*dY)) < radius);
    }

    public void changeHex(Hex newHex)
    {
        int previousHexLevel = this.hex.getLevel();

        this.hex = newHex;
        this.hex.setLevel(previousHexLevel+1);

        Graphics2D g2d = hex.getImage().createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.drawString("" + hex.getLevel(), 20, 20);

        super.setBaseImage(hex.getImage());
        super.setHoverImage(hex.getHoverImage());
    }

    public Hex getHex()
    {
        return hex;
    }

    public boolean hoverCheck(Point point)
    {
        super.setHover(pointIsOn(point));
        if(super.getHover())
        {
            board.setHoverHexButton(this);
        }

        return super.getHover();
    }

    public void resetButton()
    {
        hex = new Hex(Terrain.EMPTY);
        super.setBaseImage(hex.getImage());
        super.setHoverImage(hex.getHoverImage());
    }

    public void press()
    {
        board.placeTile(super.getOrigin());
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
        drawBaseImage(g2d);
    }

    private void buildHover()
    {
        BufferedImage hoverImg = super.getHoverImage();
        Graphics2D g2d = hoverImg.createGraphics();
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
        drawBaseImage(g2d);
    }

    private void buildHover()
    {
        BufferedImage hoverImg = super.getHoverImage();
        Graphics2D g2d = hoverImg.createGraphics();
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
