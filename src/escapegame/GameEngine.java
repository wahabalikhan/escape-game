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

    private Random rng = new Random(911);

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
        return null;
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
