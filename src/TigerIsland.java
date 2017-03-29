import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Wylie on 3/19/2017.
 */

// Main class contains JMenus (i.e., dropdown menus) and emplace a TigerPanel
public class TigerIsland
{
    static private final int WIDTH = 1024;
    static private final int HEIGHT = 768;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    // createAndShowGUI assembles and configures the JFrame
    public static void createAndShowGUI()
    {
        JFrame frame = new JFrame();

        frame.setTitle("Group O - TigerIsland v2 Development Build");
        frame.setSize(WIDTH + 16, HEIGHT + 62);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

        JMenuItem reset = new JMenuItem("Reset to Large Board");
        reset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                panel.resetHexes();
            }
        });

        fileMenu.add(reset);

        JMenuItem resetWithOneHex = new JMenuItem("Reset with One Hex");
        resetWithOneHex.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                panel.resetWithOneHex();
            }
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
// Used for human user input/output
class TigerPanel extends JPanel
{
    private BufferedImage image;
    private Graphics2D g2d;

    //private GameManager manager;

    private ArrayList<Menu> menus;
    private BaseMenu baseMenu;
    private Menu activeMenu;

    public TigerPanel(BufferedImage image) {
        this.image = image;
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //manager = new GameManager(g2d);

        initializeMenus();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                activeMenu.checkForPress(event.getPoint());
                activeMenu.updateDisplays();
                activeMenu.drawMenu(g2d);
                repaint();
            }

            public void mouseClicked(MouseEvent event) {

            }

            public void mouseReleased(MouseEvent event) {

            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent event) {
                activeMenu.checkForHover(event.getPoint());
                activeMenu.updateDisplays();
                activeMenu.drawMenu(g2d);
                repaint();
            }

            public void mouseDragged(MouseEvent event) {
                activeMenu.checkForHover(event.getPoint());
                activeMenu.updateDisplays();
                activeMenu.drawMenu(g2d);
                repaint();
            }
        });


    }

    private void initializeMenus()
    {
        menus = new ArrayList<Menu>();
        baseMenu = new BaseMenu();
        menus.add(baseMenu);
        activeMenu = menus.get(0);
        activeMenu.drawMenu(g2d);
    }

    public void emptyHexes() {
        baseMenu.clearHexes();
        baseMenu.drawMenu(g2d);
        repaint();
    }

    public void resetHexes() {
        baseMenu.resetHexes();
        baseMenu.drawMenu(g2d);
        repaint();
    }

    public void resetWithOneHex() {
        baseMenu.resetWithOneHex();
        baseMenu.drawMenu(g2d);
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
