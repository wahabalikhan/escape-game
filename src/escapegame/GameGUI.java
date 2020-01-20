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

public class GameGUI extends JFrame {
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int HEALTH_BAR_HEIGHT = 3;

    Canvas canvas;

    public GameGUI() {
        initGUI();
    }

    public void registerKeyHandler(InputHandler i) {
        addKeyListener(i);
    }

    private void initGUI() {
        add(canvas = new Canvas());
        setTitle("Escape");
        setSize(816, 615);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void updateDisplay(TileType[][] tiles, Human player, Seeker[] seekers, Chaser[] chasers, Fuel fuel) {
        canvas.update(tiles, player, seekers, chasers, fuel);
    }
}

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

    TileType[][] currentTiles;
    Human currentPlayer;
    Seeker[] currentSeekers;
    Chaser[] currentChasers;
    Fuel currentFuel;

    public Canvas() {
        loadTileImages();
    }

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
        } catch (IOException e) {
            System.out.println("Exception loading image: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public void update(TileType[][] t, Human p, Seeker[] s, Chaser[] c, Fuel f) {
        currentTiles = t;
        currentPlayer = p;
        currentSeekers = s;
        currentChasers = c;
        currentFuel = f;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
    }

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
    }

    private void drawHealthBar(Graphics2D g2, Human h) {
        double remainingHealth = (double) h.getHealth() / (double) h.getMaxHealth();
        g2.setColor(Color.RED);
        g2.fill(new Rectangle2D.Double(h.getX() * GameGUI.TILE_WIDTH, h.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH, GameGUI.HEALTH_BAR_HEIGHT));
        g2.setColor(Color.GREEN);
        g2.fill(new Rectangle2D.Double(h.getX() * GameGUI.TILE_WIDTH, h.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH * remainingHealth, GameGUI.HEALTH_BAR_HEIGHT));
    }
}
