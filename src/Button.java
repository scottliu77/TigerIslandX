import java.awt.*;
import java.awt.image.*;

import static java.lang.Math.sqrt;

public class Button
{
    private Point origin;
    private Point terminal;
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
        System.out.println("Attempted to press TestPackage.Button with undefined behavior");
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

    public BufferedImage getBaseImage()
    {
        return baseImage;
    }

    public BufferedImage getHoverImage()
    {
        return hoverImage;
    }

	/*
	public void drawBase(Graphics2D g2d)
	{
		g2d.drawImage(baseImage, origin.x, origin.y, null);
	}

	public void drawHover(Graphics2D g2d)
	{
		g2d.drawImage(hoverImage, origin.x, origin.y, null);
	}
	*/

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

        // int previousHexLevel = this.hex.getLevel();
        // this.hex = newHex;
        // this.hex.setLevel(previousHexLevel + 1);
        //Graphics2D g2d = hex.getImage().createGraphics();
        //g2d.setColor(Color.BLACK);
        // g2d.drawString("" + hex.getLevel(), 20, 20);

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

    public void press()
    {
        board.placeTile(super.getOrigin());
    }

}


class RotateRightButton extends Button
{
    Board board;

    public RotateRightButton(Point origin, Board board, BufferedImage baseImg, BufferedImage hoverImg)
    {
        super(origin, baseImg, hoverImg);
        this.board = board;
    }

    public void press()
    {
        board.rotDeckTileRight();
    }
}

class RotateLeftButton extends Button
{
    Board board;

    public RotateLeftButton(Point origin, Board board, BufferedImage baseImg, BufferedImage hoverImg)
    {
        super(origin, baseImg, hoverImg);
        this.board = board;
    }

    public void press()
    {
        board.rotDeckTileLeft();
    }
}
