package escapegame;

import com.sun.source.tree.ArrayAccessTree;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameEngine {

    public enum TileType {
        CAR, DIRT, NEST, GRASS, ROAD, WALL
    }

    public static final int GRID_WIDTH = 25;
    public static final int GRID_HEIGHT = 18;

    private Random rng = new Random();

    private int cleared = 0;
    private int turnNumber = 1;
    private int numChasers = 0;

    private GameGUI gui;
    private TileType[][] tiles;
    private ArrayList<Point> spawns;
    private Human player;
    private Fuel fuel;
    private boolean fuelCollected = false;
    private Seeker[] seekers;
    private Chaser[] chasers;

    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    int carX;
    int carY;

    private void randomSpawns(int x, int y) {
        carX = rng.nextInt(GRID_WIDTH);
        carY = rng.nextInt(GRID_HEIGHT);
    }

    private TileType[][] generateLevel() {
        /*
        You must complete the code for the generateLevel() method. The method must return an array of
        TileType values (TileType is an enumeration in the GameEngine class). Your code should return
        the array which is used to draw tiles to the screen by the GameGUI class. For example, a 2D
        array of only TileType.GRASS values will draw an empty level of grass tiles. You should code
        the method to produce an interesting and fun level for the player to move around. It is suggested
        that you design an algorithm and then implement the algorithm in code. The task requires levels
        be generated dynamically and you should avoid hard coding a completely fixed level layout. The
        level should contain exactly one tile with the type TileType.CAR.
         */
        tiles = new TileType[GRID_WIDTH][GRID_HEIGHT];
        randomSpawns(carX, carY);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int num = rng.nextInt(100);
                if (num >= 0 && num < 100) {
                    tiles[i][j] = TileType.GRASS;
                }
                if (num >= 0 && num < 50) {
                    tiles[i][j] = TileType.DIRT;
                }
                if (num >= 0 && num < 15) {
                    tiles[i][j] = TileType.ROAD;
                }
                if (num >= 0 && num < 10) {
                    tiles[i][j] = TileType.NEST;
                }
                if (num >= 0 && num < 5) {
                    tiles[i][j] = TileType.WALL;
                }
                tiles[carX][carY] = TileType.CAR;
            }
        }
        return tiles;
    }

    private ArrayList<Point> getSpawns() {
        ArrayList<Point> spawns = new ArrayList<Point>();
        for (int i=0; i<tiles.length; i++) {
            for (int j=0; j<tiles[i].length; j++) {
                if (tiles[i][j] == TileType.GRASS || tiles[i][j] == TileType.DIRT || tiles[i][j] == TileType.ROAD || tiles[i][j] == TileType.NEST) {
                    Point point = new Point(i,j);
                    spawns.add(point);
                }
            }
        }
        return spawns;
    }

    private Seeker[] spawnSeekers() {
        seekers = new Seeker[5];
        for (int i=0; i< seekers.length; i++) {
            Point point = spawns.get(rng.nextInt(spawns.size()));
            seekers[i] = new Seeker(point.x, point.y);
            spawns.remove(point.x);
            spawns.remove(point.y);
        }
        return seekers;
    }

    private Human spawnPlayer() {
        Human player = new Human(100, carX+1, carY);
        return player;
    }

    private Fuel spawnFuel() {
        return null;
    }

    public void movePlayerLeft() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (tiles[playerX-1][playerY] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX-1, playerY);
        }
    }

    public void movePlayerRight() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (tiles[playerX+1][playerY] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX+1, playerY);
        }
    }

    public void movePlayerUp() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (tiles[playerX][playerY-1] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX, playerY-1);
        }
    }

    public void movePlayerDown() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (tiles[playerX][playerY+1] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX, playerY+1);
        }
    }

    private void moveSeekers() {
        for (int i=0; i<seekers.length; i++) {
            if (seekers[i] != null) {
                moveSeeker(seekers[i]);
            }
        }
    }

    private void moveSeeker(Seeker a) {
        int seekerX = a.getX();
        int seekerY = a.getY();
        int playerX = player.getX();
        int playerY = player.getY();

        int differenceX = playerX - seekerX;
        int differenceY = playerY - seekerY;

        if (differenceX > 0) {
            a.setPosition(a.getX()+1, a.getY());
        }
        if (differenceX < 0) {
            a.setPosition(a.getX()-1, a.getY());
        }
        if (differenceY > 0) {
            a.setPosition(a.getX(), a.getY()+1);
        }
        if (differenceX < 0) {
            a.setPosition(a.getX(), a.getY()-1);
        }
        if ((a.getX() == playerX && a.getY() == playerY) || (playerX == a.getX() && playerY == a.getY())) {
            player.changeHealth(-10);
        }
    }

    private void moveChasers() {

    }

    private void moveChaser(Chaser c) {

    }

    private void newLevel() {

    }

    private void placePlayer() {

    }

    public void doTurn() {
        if (turnNumber % 5 == 0) {
            moveSeekers();
        }

        if (player.getHealth() < 1) {
            System.exit(0);
        }
        gui.updateDisplay(tiles, player, seekers, chasers, fuel);
        turnNumber++;
    }

    public void startGame() {
        tiles = generateLevel();
        spawns = getSpawns();
        seekers = spawnSeekers();
        chasers = new Chaser[50];
        player = spawnPlayer();
        fuel = spawnFuel();
        gui.updateDisplay(tiles, player, seekers, chasers, fuel);
    }
}
