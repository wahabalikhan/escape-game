package escapegame;

import javax.imageio.ImageIO;
import javax.swing.*;

import escapegame.GameEngine.TileType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * The GameGUI class is responsible for rendering graphics to the screen to display
 * the game grid, player, monsters and the fuel. The GameGUI class passes keyboard
 * events to a registered InputHandler to be handled.
 */
public class GameGUI extends JFrame {

    /**
     * The three final int attributes below set the size of some graphical elements,
     * specifically the display height and width of tiles in the level and the height
     * of health bars for Ship objects in the game. Tile sizes should match the size
     * of the image files used in the game.
     */
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int HEALTH_BAR_HEIGHT = 3;

    /**
     * The canvas is the area that graphics are drawn to. It is an internal class
     * of the GameGUI class.
     */
    Canvas canvas;

    /**
     * Constructor for the GameGUI class. It calls the initGUI method to generate the
     * required objects for display.
     */
    public GameGUI() {
        initGUI();
    }

    /**
     * Registers an object to be passed keyboard events captured by the GUI.
     * @param i the InputHandler object that will process keyboard events to
     * make the game respond to input
     */
    public void registerKeyHandler(InputHandler i) {
        addKeyListener(i);
    }

    /**
     * Method to create and initialise components for displaying elements of the
     * game on the screen.
     */
    private void initGUI() {
        add(canvas = new Canvas());
        setTitle("Escape");
        setSize(816, 615);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Method to update the graphical elements on the screen, usually after entities
     * have moved when a keyboard event was handled. The method
     * requires four arguments and displays corresponding information on the screen.
     * @param tiles A 2-dimensional array of TileTypes. This is the tiles of the
     * current level that should be drawn to the screen.
     * @param player A Human object. This object is used to draw the player in
    the right tile and display its health. null can be passed for this argument,
    in which case no player will be drawn.
     * @param seekers An array of Seeker objects that is processed to draw
     * seekers in tiles. null can be passed for this argument in which case no
     * seekers will be drawn. Elements in the seekers array can also be null,
     * in which case nothing will be drawn for that element of the array.
     * @param chasers An array of Chaser objects that is processed to draw
     * chasers in tiles. null can be passed for this argument in which case no
     * chasers will be drawn. Elements in the chasers array can also be null,
     * in which case nothing will be drawn for that element of the array.
     * @param fuel A Fuel object. This is used to draw the fuel on the map.
     * @param health A Health object. This is used to draw the health on the map.
     */
    public void updateDisplay(TileType[][] tiles, Human player, Seeker[] seekers, Chaser[] chasers, Fuel fuel, Health health) {
        canvas.update(tiles, player, seekers, chasers, fuel, health);
    }
}

/**
 * Internal class used to draw elements within a JPanel. The Canvas class loads
 * images from an asset folder inside the main project folder.
 */
class Canvas extends JPanel {
    private BufferedImage car;
    private BufferedImage chaser;
    private BufferedImage dirt;
    private BufferedImage fuel;
    private BufferedImage grass;
    private BufferedImage nest;
    private BufferedImage road;
    private BufferedImage seeker;
    private BufferedImage wall;
    private BufferedImage player;
    private BufferedImage health;

    TileType[][] currentTiles;
    Human currentPlayer;
    Seeker[] currentSeekers;
    Chaser[] currentChasers;
    Fuel currentFuel;
    Health currentHealth;

    /**
     * Constructor that loads tile images for use in this class
     */
    public Canvas() {
        loadTileImages();
    }

    /**
     * Loads tiles images from a fixed folder location within the project directory
     */
    private void loadTileImages() {
        try {
            car = ImageIO.read(new File("assets/car.png"));
            assert car.getHeight() == GameGUI.TILE_HEIGHT &&
                    car.getWidth() == GameGUI.TILE_WIDTH;
            chaser = ImageIO.read(new File("assets/chaser.png"));
            assert chaser.getHeight() == GameGUI.TILE_HEIGHT &&
                    chaser.getWidth() == GameGUI.TILE_WIDTH;
            dirt = ImageIO.read(new File("assets/dirt.png"));
            assert dirt.getHeight() == GameGUI.TILE_HEIGHT &&
                    dirt.getWidth() == GameGUI.TILE_WIDTH;
            fuel = ImageIO.read(new File("assets/fuel.png"));
            assert fuel.getHeight() == GameGUI.TILE_HEIGHT &&
                    fuel.getWidth() == GameGUI.TILE_WIDTH;
            grass = ImageIO.read(new File("assets/grass.png"));
            assert grass.getHeight() == GameGUI.TILE_HEIGHT &&
                    grass.getWidth() == GameGUI.TILE_WIDTH;
            nest = ImageIO.read(new File("assets/nest.png"));
            assert nest.getHeight() == GameGUI.TILE_HEIGHT &&
                    nest.getWidth() == GameGUI.TILE_WIDTH;
            road = ImageIO.read(new File("assets/road.png"));
            assert road.getHeight() == GameGUI.TILE_HEIGHT &&
                    road.getWidth() == GameGUI.TILE_WIDTH;
            seeker = ImageIO.read(new File("assets/seeker.png"));
            assert seeker.getHeight() == GameGUI.TILE_HEIGHT &&
                    seeker.getWidth() == GameGUI.TILE_WIDTH;
            player = ImageIO.read(new File("assets/runner.png"));
            assert player.getHeight() == GameGUI.TILE_HEIGHT &&
                    player.getWidth() == GameGUI.TILE_WIDTH;
            wall = ImageIO.read(new File("assets/wall.png"));
            assert wall.getHeight() == GameGUI.TILE_HEIGHT &&
                    wall.getWidth() == GameGUI.TILE_WIDTH;
            health = ImageIO.read(new File("assets/health.png"));
            assert health.getHeight() == GameGUI.TILE_HEIGHT &&
                    health.getWidth() == GameGUI.TILE_WIDTH;
        } catch (IOException e) {
            System.out.println("Exception loading image: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    /**
     * Updates the current graphics on the screen to display the tiles, fuel, player and monsters
     */
    public void update(TileType[][] t, Human p, Seeker[] s, Chaser[] c, Fuel f, Health h) {
        currentTiles = t;
        currentPlayer = p;
        currentSeekers = s;
        currentChasers = c;
        currentFuel = f;
        currentHealth = h;
        repaint();
    }

    /**
     * Override of method in super class, it draws the custom elements for this
     * game such as the tiles, player, aliens and asteroids.
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
        GameEngine.youWin(g);
        GameEngine.gameOver(g);
    }

    /**
     * Draws graphical elements to the screen to display the current level
     * tiles, the player, asteroids and the aliens. If the tiles, player or
     * alien objects are null they will not be drawn.
     * @param g Graphics object to use for drawing
     */
    private void drawMap(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Random r = new Random(555);
        if (currentTiles != null) {
            for (int i = 0; i < currentTiles.length; i++) {
                for (int j = 0; j < currentTiles[i].length; j++) {
                    switch (currentTiles[i][j]) {
                        case CAR:
                            g2.drawImage(car, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case DIRT:
                            g2.drawImage(dirt, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case NEST:
                            g2.drawImage(nest, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case GRASS:
                            g2.drawImage(grass, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case ROAD:
                            g2.drawImage(road, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case WALL:
                            g2.drawImage(wall, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                    }
                }
            }
        }

        if (currentSeekers != null) {
            for (Seeker s : currentSeekers) {
                if (s != null) {
                    g2.drawImage(seeker, s.getX() * GameGUI.TILE_WIDTH, s.getY() * GameGUI.TILE_HEIGHT, null);
                }
            }
        }

        if (currentChasers != null) {
            for (Chaser c : currentChasers) {
                if (c != null) {
                    g2.drawImage(chaser, c.getX() * GameGUI.TILE_WIDTH, c.getY() * GameGUI.TILE_HEIGHT, null);
                }
            }
        }

        if (currentPlayer != null) {
            g2.drawImage(player, currentPlayer.getX() * GameGUI.TILE_WIDTH, currentPlayer.getY() * GameGUI.TILE_HEIGHT, null);
            drawHealthBar(g2, currentPlayer);
        }

        if (currentFuel != null) {
            g2.drawImage(fuel, currentFuel.getX() * GameGUI.TILE_WIDTH, currentFuel.getY() * GameGUI.TILE_HEIGHT, null);
        }
        if (currentHealth != null) {
            g2.drawImage(health, currentHealth.getX() * GameGUI.TILE_WIDTH, currentHealth.getY() * GameGUI.TILE_HEIGHT, null);
        }
    }

    /**
     * Draws a health bar for the given entity at the bottom of the tile that
     * the entity is located in.
     * @param g2 The graphics object to use for drawing
     * @param h The entity that the health bar will be drawn for
     */
    private void drawHealthBar(Graphics2D g2, Human h) {
        double remainingHealth = (double) h.getHealth() / (double) h.getMaxHealth();
        g2.setColor(Color.RED);
        g2.fill(new Rectangle2D.Double(h.getX() * GameGUI.TILE_WIDTH, h.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH, GameGUI.HEALTH_BAR_HEIGHT));
        g2.setColor(Color.GREEN);
        g2.fill(new Rectangle2D.Double(h.getX() * GameGUI.TILE_WIDTH, h.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH * remainingHealth, GameGUI.HEALTH_BAR_HEIGHT));
    }
}
