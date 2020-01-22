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
    private int numSeekers = 5;

    private GameGUI gui;
    private TileType[][] tiles;
    private ArrayList<Point> spawns;
    private Human player;
    private Fuel fuel;
    private boolean fuelCollected = false;
    private Seeker[] seekers;
    private Chaser[] chasers;
    private Health health;
    private boolean healthCollected = false;

    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    int carX;
    int carY;
    int roadX;
    int roadY;

    private void randomSpawns(int cX, int Cy, int rX, int rY) {
        carX = rng.nextInt(GRID_WIDTH);
        carY = rng.nextInt(GRID_HEIGHT);
        roadX = rng.nextInt(GRID_WIDTH);
        roadY = rng.nextInt(GRID_HEIGHT);
    }

    private TileType[][] generateLevel() {
        tiles = new TileType[GRID_WIDTH][GRID_HEIGHT];
        randomSpawns(carX, carY, roadX, roadY);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int num = rng.nextInt(100);
                if (num >= 0 && num < 100) {
                    tiles[i][j] = TileType.GRASS;
                }
                if (num >= 0 && num < 50) {
                    tiles[i][j] = TileType.DIRT;
                }
                if (num >= 0 && num < 10) {
                    tiles[i][j] = TileType.NEST;
                }
                if (num >= 0 && num < 5) {
                    tiles[i][j] = TileType.WALL;
                }
            }
        }
        // 0 represents width, 1 represents height
        int randomNum = rng.nextInt(2);
        if (randomNum == 0) {
            for (int i = 0; i < GRID_WIDTH; i++) {
                tiles[i][roadY] = TileType.ROAD;
                tiles[carX][roadY] = TileType.CAR;
            }
        } else {
            for (int i = 0; i < GRID_HEIGHT; i++) {
                tiles[roadX][i] = TileType.ROAD;
                tiles[roadX][carY] = TileType.CAR;
            }
        }
        return tiles;
    }

    private ArrayList<Point> getSpawns() {
        ArrayList<Point> spawns = new ArrayList<Point>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == TileType.GRASS || tiles[i][j] == TileType.DIRT || tiles[i][j] == TileType.ROAD || tiles[i][j] == TileType.NEST) {
                    Point point = new Point(i, j);
                    spawns.add(point);
                }
            }
        }
        return spawns;
    }

    private Seeker[] spawnSeekers() {
        seekers = new Seeker[numSeekers];
        for (int i = 0; i < seekers.length; i++) {
            Point point = spawns.get(rng.nextInt(spawns.size()));
            seekers[i] = new Seeker(point.x, point.y);
            spawns.remove(point.x);
            spawns.remove(point.y);
        }
        return seekers;
    }

    private Human spawnPlayer() {
        if (tiles[roadX][carY] == TileType.CAR) {
            Human player = new Human(100, roadX, carY - 1);
            return player;
        } else {
            Human player = new Human(100, carX + 1, roadY);
            return player;
        }
    }

    private Fuel spawnFuel() {
        Point point = spawns.get(rng.nextInt(spawns.size()));
        Fuel fuel = new Fuel(point.x, point.y);
        spawns.remove(point.x);
        spawns.remove(point.y);
        return fuel;
    }

    private Health spawnHealth() {
        Point point = spawns.get(rng.nextInt(spawns.size()));
        Health health = new Health(point.x, point.y);
        spawns.remove(point.x);
        spawns.remove(point.y);
        return health;
    }

    public void movePlayerLeft() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (playerX == 0 || tiles[playerX - 1][playerY] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX - 1, playerY);
        }
    }

    public void movePlayerRight() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (playerX == 24 || tiles[playerX + 1][playerY] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX + 1, playerY);
        }
    }

    public void movePlayerUp() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (playerY == 0 || tiles[playerX][playerY - 1] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX, playerY - 1);
        }
    }

    public void movePlayerDown() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (playerY == 17 || tiles[playerX][playerY + 1] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX, playerY + 1);
        }
    }

    private void moveSeekers() {
        for (int i = 0; i < seekers.length; i++) {
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

        boolean canMove = true;
        for (int i = 0; i < seekers.length; i++) {
            Seeker seeker = seekers[i];
            if (differenceX < 0 && a.getX() - 1 == seeker.getX() && a.getY() == seeker.getY()) {
                canMove = false;
            }
            if (differenceX > 0 && a.getX() + 1 == seeker.getX() && a.getY() == seeker.getY()) {
                canMove = false;
            }
            if (differenceY > 0 && a.getX() == seeker.getX() && a.getY() + 1 == seeker.getY()) {
                canMove = false;
            }
            if (differenceY < 0 && a.getX() == seeker.getX() && a.getY() - 1 == seeker.getY()) {
                canMove = false;
            }
        }
        if (differenceX < 0 && canMove) {
            a.setPosition(a.getX() - 1, a.getY());
        }
        if (differenceX > 0 && canMove) {
            a.setPosition(a.getX() + 1, a.getY());
        }
        if (differenceY > 0 && canMove) {
            a.setPosition(a.getX(), a.getY() + 1);
        }
        if (differenceY < 0 && canMove) {
            a.setPosition(a.getX(), a.getY() - 1);
        }
        if (a.getX() == playerX && a.getY() == playerY) {
            player.changeHealth(-10);
        }
    }

    private void moveChasers() {
        for (int i = 0; i < chasers.length; i++) {
            if (chasers[i] != null) {
                moveChaser(chasers[i]);
            }
        }
    }

    private void moveChaser(Chaser c) {
        int playerX = player.getX();
        int playerY = player.getY();
        int chaserX = c.getX();
        int chaserY = c.getY();

        int differenceX = playerX - chaserX;
        int differenceY = playerY - chaserY;
        boolean canMove = true;
        for (int i = 0; i < chasers.length; i++) {
            if (chasers[i] == null) {
                break;
            } else {
                Chaser chaser = chasers[i];
                if (differenceX < 0 && c.getX() - 1 == chaser.getX() && c.getY() == chaser.getY()) {
                    canMove = false;
                }
                if (differenceX > 0 && c.getX() + 1 == chaser.getX() && c.getY() == chaser.getY()) {
                    canMove = false;
                }
                if (differenceY > 0 && c.getX() == chaser.getX() && c.getY() + 1 == chaser.getY()) {
                    canMove = false;
                }
                if (differenceY < 0 && c.getX() == chaser.getX() && c.getY() - 1 == chaser.getY()) {
                    canMove = false;
                }
            }
        }
        if (differenceX < 0 && canMove) {
            c.setPosition(c.getX() - 1, c.getY());
        }
        if (differenceX > 0 && canMove) {
            c.setPosition(c.getX() + 1, c.getY());
        }
        if (differenceY > 0 && canMove) {
            c.setPosition(c.getX(), c.getY() + 1);
        }
        if (differenceY < 0 && canMove) {
            c.setPosition(c.getX(), c.getY() - 1);
        }
        if (c.getX() == playerX && c.getY() == playerY) {
            player.changeHealth(-10);
        }
    }

    private void newLevel() {
        cleared++;
        numSeekers++;
        tiles = generateLevel();
        spawns = getSpawns();
        seekers = spawnSeekers();
        chasers = new Chaser[50];
        placePlayer();
        fuel = spawnFuel();
        health = spawnHealth();
        fuelCollected = false;
        healthCollected = false;
        gui.updateDisplay(tiles, player, seekers, chasers, fuel, health);
    }

    private void placePlayer() {
        if (tiles[roadX][carY] == TileType.CAR) {
            player.setPosition(roadX, carY - 1);
        } else {
            player.setPosition(carX + 1, roadY);
        }
    }

    public void doTurn() {
        if (turnNumber % 5 == 0) {
            moveSeekers();
        }

        if (turnNumber % 2 == 0) {
            moveChasers();
        }

        if (player.getHealth() < 1) {
            System.exit(0);
        }

        int playerX = player.getX();
        int playerY = player.getY();
        if (!fuelCollected) {
            int fuelX = fuel.getX();
            int fuelY = fuel.getY();
            if (playerX == fuelX && playerY == fuelY) {
                fuelCollected = true;
                fuel = null;
            }
        }
        if (tiles[roadX][carY] == TileType.CAR) {
            if (fuelCollected && (playerX == roadX && playerY == carY)) {
                newLevel();
            }
        }
        if (tiles[carX][roadY] == TileType.CAR) {
            if (fuelCollected && (playerX == carX && playerY == roadY)) {
                newLevel();
            }
        }

        if (tiles[playerX][playerY] == TileType.NEST) {
            for (int i = 0; i < chasers.length; i++) {
                if (chasers[i] == null) {
                    chasers[i] = new Chaser(playerX + 1, playerY + 1);
                    numChasers++;
                    break;
                }
            }
        }

        if (!healthCollected) {
            int healthX = health.getX();
            int healthY = health.getY();
            if (playerX == healthX && playerY == healthY) {
                healthCollected = true;
                health = null;
                player.changeHealth(10);
            }
        }

        gui.updateDisplay(tiles, player, seekers, chasers, fuel, health);
        turnNumber++;
    }

    public void startGame() {
        tiles = generateLevel();
        spawns = getSpawns();
        seekers = spawnSeekers();
        chasers = new Chaser[50];
        player = spawnPlayer();
        fuel = spawnFuel();
        health = spawnHealth();
        gui.updateDisplay(tiles, player, seekers, chasers, fuel, health);
    }
}
