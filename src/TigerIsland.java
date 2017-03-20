import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by Wylie on 3/19/2017.
 */

// Main class contains JMenus (i.e., dropdown menus) and emplace a TigerPanel
public class TigerIsland
{
    static private final int WIDTH = 1024;
    static private final int HEIGHT = 768;

    GameManager manager;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
        return;
    }

    public static void createAndShowGUI()
    {
        JFrame frame = new JFrame();

        frame.setTitle("Group O - TigerIsland v1 Development Build");
        frame.setSize(WIDTH + 16, HEIGHT + 62);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TigerPanel panel = new TigerPanel(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB));

        JMenu fileMenu = new JMenu("File");

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        });

        fileMenu.add(exit);


        JMenuItem clear = new JMenuItem("Empty all Hexes");
        clear.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                panel.emptyHexes();
            }
        });

        fileMenu.add(clear);

        JMenuItem reset = new JMenuItem("Reset Board");
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event) { panel.resetHexes(); }
        });
        fileMenu.add(reset);

        JMenuItem resetWithOneHex = new JMenuItem("Reset with One Hex");
        resetWithOneHex.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event) { panel.resetWithOneHex(); }
        });
        fileMenu.add(resetWithOneHex);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.validate();
        frame.setVisible(true);
    }

}

// TigerPanel displays the output image and detects mouse inputs
class TigerPanel extends JPanel {
    private BufferedImage image;
    private Graphics2D g2d;

    private Point buttonPoint;

    private GameManager manager;

    public TigerPanel(BufferedImage image) {
        this.image = image;
        g2d = (Graphics2D) image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        manager = new GameManager(this);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                manager.checkForPress(event.getPoint());
                repaint();
            }

            public void mouseClicked(MouseEvent event) {

            }

            public void mouseReleased(MouseEvent event) {

            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent event) {
                manager.checkForHover(event.getPoint());
                repaint();
            }

            public void mouseDragged(MouseEvent event) {
                manager.checkForHover(event.getPoint());
                repaint();
            }
        });


    }

    public void initializeMenus() {

    }

    public void emptyHexes() {
        manager.emptyHexes();
        repaint();
    }

    public void resetHexes() {
        manager.resetHexes();
        repaint();
    }

    public void resetWithOneHex() {
        manager.resetWithOneHex();
        repaint();
    }

    public Graphics2D getG2D() {
        return g2d;
    }

    public void drawImg(BufferedImage img) {
        g2d.drawImage(img, 0, 0, null);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

}
