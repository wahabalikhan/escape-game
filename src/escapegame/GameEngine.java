package escapegame;

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
        int carX = rng.nextInt(GRID_WIDTH);
        int carY = rng.nextInt(GRID_HEIGHT);

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
        return null;
    }

    private Seeker[] spawnSeekers() {
        return null;
    }

    private Human spawnPlayer() {
        return null;
    }

    private Fuel spawnFuel() {
        return null;
    }

    public void movePlayerLeft() {
    }

    public void movePlayerRight() {
    }

    public void movePlayerUp() {
    }

    public void movePlayerDown() {
    }

    private void moveSeekers() {

    }

    private void moveSeeker(Seeker a) {

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
