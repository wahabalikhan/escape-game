package escapegame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The GameEngine class is responsible for managing information about the game,
 * creating levels, the player, monsters, as well as updating information when a
 * key is pressed while the game is running.
 */
public class GameEngine {

    /**
     * An enumeration type to represent different types of tiles that make up
     * the level. Each type has a corresponding image file that is used to draw
     * the right tile to the screen for each tile in a level. All types except
     * for Walls are open for player movement. Walls will block player (but not
     * monster) movement. Nests look like dirt but trigger the creation of a
     * chaser when the player is next to them, then turn to dirt tiles. The Car
     * tile is the entry and exit point for the level.
     */
    public enum TileType {
        CAR, DIRT, NEST, GRASS, ROAD, WALL
    }

    /**
     * The width of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int GRID_WIDTH = 25;

    /**
     * The height of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int GRID_HEIGHT = 18;

    /**
     * A random number generator that can be used to include randomised choices
     * in the creation of levels, in choosing places to spawn the player,
     * monsters and fuel, and to randomise movement or other factors. It has a
     * seed of 911 set to support debugging - a seeded generator will create the
     * same values each time the program is run to help recreate bugs. Remove
     * the seed for a random set of values every time.
     */
    private Random rng = new Random();

    /**
     * The number of levels cleared by the player in this game. Can be used to
     * generate harder games as the player clears levels.
     */
    private static int cleared = 0;

    /**
     * Tracks the current turn number. Used to control monster movement.
     */
    private int turnNumber = 1;

    /**
     * The number of chasers added to the level - one is added every time the
     * player moves into a Nest tile. This is used to track the position in the
     * array of Chaser objects that new ones should be added. Whenever a Chaser
     * is created this value should in incremented by one to ensure Chasers are
     * not overwritten.
     */
    private int numChasers = 0;

    /**
     * The number of seekers added to the level - another seeker is added when
     * the player progresses to another level.
     */
    private int numSeekers = 5;

    /**
     * The GUI associated with a GameEngine object. THis link allows the engine
     * to pass level (tiles) and entity information to the GUI to be drawn.
     */
    private GameGUI gui;

    /**
     * The 2 dimensional array of tiles the represent the current level. The
     * size of this array should use the GRID_HEIGHT and GRID_WIDTH attributes
     * when it is created.
     */
    private TileType[][] tiles;

    /**
     * An ArrayList of Point objects used to create and track possible locations
     * to spawn the player, monsters and items.
     */
    private ArrayList<Point> spawns;

    /**
     * A Human object that is the current player. This object stores the state
     * information for the player, including the current position (which is a
     * pair of co-ordinates that corresponds to a tile in the current level)
     */
    private static Human player;

    /**
     * A Fuel object that must be collected by the player in order to move to
     * the next level. Once the player moves into the same tile as the Fuel
     * object it should be removed from the game (set to null) and the boolean
     * fuelCollected variable should be set to true.
     */
    private Fuel fuel;

    /**
     * A boolean variable to track if the player has collected the Fuel on this
     * level yet. It is used to check if a new level should be generated when
     * the player stands on the Car tile.
     */
    private boolean fuelCollected = false;

    /**
     * An array of Seeker objects that represents the seekers in the current
     * level. Elements in this array should be of the type Seeker, meaning that
     * a monster is alive and needs to be drawn or moved, or should be null
     * which means nothing is drawn or processed for movement. Null values in
     * this array are skipped during drawing and movement processing.
     */
    private Seeker[] seekers;

    /**
     * An array of Chaser objects that represents the chasers in the current
     * level. Elements in this array should be of the type Chaser, meaning that
     * a monster is alive and needs to be drawn or moved, or should be null
     * which means nothing is drawn or processed for movement. Null values in
     * this array are skipped during drawing and movement processing.
     */
    private Chaser[] chasers;

    /**
     * A Health object that can be collected by the player in order to increase
     * health by +10. Once the player moves into the same tile as the Health
     * object it should be removed from the game (set to null) and the boolean
     * healthCollected variable should be set to true.
     */
    private Health health;

    /**
     * A boolean variable to track if the player has collected the Health on this
     * level yet.
     */
    private boolean healthCollected = false;

    /**
     * Constructor that creates a GameEngine object and connects it with a
     * GameGUI object.
     *
     * @param gui The GameGUI object that this engine will pass information to
     * in order to draw levels and entities to the screen.
     */
    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    /**
     * variables to store random numbers from GRID_WIDTH and
     * GRID_HEIGHT to be used for positioning the car on the road and randomly generate the
     * road in a column/row.
     */
    int carX;
    int carY;
    int roadX;
    int roadY;

    /**
     * this method allows for the variables with the random integers assigned to
     * them to be passed in a parameter for other methods to access them.
     *
     * @param cX
     * @param Cy
     * @param rX
     * @param rY
     */
    private void randomSpawns(int cX, int Cy, int rX, int rY) {
        carX = rng.nextInt(GRID_WIDTH);
        carY = rng.nextInt(GRID_HEIGHT);
        roadX = rng.nextInt(GRID_WIDTH);
        roadY = rng.nextInt(GRID_HEIGHT);
    }

    /**
     * Generates a new level. The method builds a 2D array of TileTypes that
     * will be used to draw tiles to the screen and to add a variety of elements
     * into each level. Tiles can be car, grass, dirt, nests, road or wall. This
     * method should contain the implementation of an algorithm to create an
     * interesting and varied level each time it is called.
     *
     * Second algorithm is used to generate the road tiles in a row/column
     * and position the car on a random road tile.
     *
     * @return A 2D array of TileTypes representing the tiles in the current
     * level of the map. The size of this array should use the width and height
     * attributes of the level specified by GRID_WIDTH and GRID_HEIGHT.
     */
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

    /**
     * Generates spawn points for entities. The method processes the tiles array
     * and finds tiles that are suitable for spawning, i.e. grass, dirt, road
     * and nest tiles. Suitable tiles should be added to the ArrayList that will
     * be returned as Point objects - Points are a simple kind of object that
     * contain an X and a Y co-ordinate stored using the int primitive type.
     *
     * @return An ArrayList containing Point objects representing suitable X and
     * Y co-ordinates in the current level that entities can be spawned in.
     */
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

    /**
     * Spawns seekers in suitable locations in the current level. The method
     * uses the spawns ArrayList to pick suitable positions to add seekers,
     * removing these positions from the spawns ArrayList as they are used
     * (using the remove() method) to avoid multiple entities spawning in the
     * same location. The method creates seekers by instantiating the Seeker
     * class, setting health and the X and Y position for the seeker using the
     * Point object removed from the spawns ArrayList.
     *
     * @return An array of Seeker objects representing the aliens for the
     * current level
     */
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

    /**
     * Spawns a Human entity in the game. The method instantiates the Human
     * class and assigns values for the health and position of the player. The
     * players position should be set to the tile representing the Car in the
     * game, although you may set the player's position to any location while
     * testing and debugging the spawning behaviour of this method.
     *
     * @return A Human object representing the player in the game
     */
    private Human spawnPlayer() {
        if (tiles[roadX][carY] == TileType.CAR) {
            Human player = new Human(100, roadX, carY - 1);
            return player;
        } else {
            Human player = new Human(100, carX + 1, roadY);
            return player;
        }
    }

    /**
     * Spawns a Fuel object in the game. The method uses the spawns ArrayList to
     * pick a suitable position for the Fuel in the game, removing the chosen
     * position from the ArrayList to avoid multiple entities being created in
     * the same position. The method works by instantiating the Fuel class and
     * returning the Fuel object.
     *
     * @return A Fuel object representing the fuel in the game
     */
    private Fuel spawnFuel() {
        Point point = spawns.get(rng.nextInt(spawns.size()));
        Fuel fuel = new Fuel(point.x, point.y);
        spawns.remove(point.x);
        spawns.remove(point.y);
        return fuel;
    }

    /**
     * Spawns a Health object in the game. The method uses the spawns ArrayList to
     * pick a suitable position for the Health in the game, removing the chosen
     * position from the ArrayList to avoid multiple entities being created in
     * the same position. The method works by instantiating the Health class and
     * returning the Health object.
     *
     * @return A Health object representing the health in the game
     */
    private Health spawnHealth() {
        Point point = spawns.get(rng.nextInt(spawns.size()));
        Health health = new Health(point.x, point.y);
        spawns.remove(point.x);
        spawns.remove(point.y);
        return health;
    }

    /**
     * Handles the movement of the player when attempting to move left in the
     * game. This method is called by the InputHandler class when the user has
     * pressed the left arrow key on the keyboard. The method checks whether the
     * tile to the left of the player is empty for movement and if it is updates
     * the player object's X and Y locations with the new position. If the tile
     * to the left of the player is not empty the method will not update the
     * player position, but could make other changes to the game.
     */
    public void movePlayerLeft() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (playerX == 0 || tiles[playerX - 1][playerY] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX - 1, playerY);
        }
    }

    /**
     * Handles the movement of the player when attempting to move right in the
     * game. This method is called by the InputHandler class when the user has
     * pressed the right arrow key on the keyboard. The method checks whether
     * the tile to the right of the player is empty for movement and if it is
     * updates the player object's X and Y locations with the new position. If
     * the tile to the right of the player is not empty the method will not
     * update the player position, but could make other changes to the game.
     */
    public void movePlayerRight() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (playerX == 24 || tiles[playerX + 1][playerY] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX + 1, playerY);
        }
    }

    /**
     * Handles the movement of the player when attempting to move up in the
     * game. This method is called by the InputHandler class when the user has
     * pressed the up arrow key on the keyboard. The method checks whether the
     * tile above the player is empty for movement and if it is updates the
     * player object's X and Y locations with the new position. If the tile
     * above the player is not empty the method will not update the player
     * position, but could make other changes to the game.
     */
    public void movePlayerUp() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (playerY == 0 || tiles[playerX][playerY - 1] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX, playerY - 1);
        }
    }

    /**
     * Handles the movement of the player when attempting to move right in the
     * game. This method is called by the InputHandler class when the user has
     * pressed the down arrow key on the keyboard. The method checks whether the
     * tile below the player is empty for movement and if it is updates the
     * player object's X and Y locations with the new position. If the tile
     * below the player is not empty the method will not update the player
     * position, but could make other changes to the game.
     */
    public void movePlayerDown() {
        int playerX = player.getX();
        int playerY = player.getY();
        if (playerY == 17 || tiles[playerX][playerY + 1] == TileType.WALL) {
            player.setPosition(playerX, playerY);
        } else {
            player.setPosition(playerX, playerY + 1);
        }
    }

    /**
     * Moves all seekers on the current level. The method checks for non-null
     * elements in the seekers array and calls the moveSeeker method for each
     * one that is not null.
     */
    private void moveSeekers() {
        for (int i = 0; i < seekers.length; i++) {
            if (seekers[i] != null) {
                moveSeeker(seekers[i]);
            }
        }
    }

    /**
     * Moves a specific seeker in the game closer to the player. The method
     * updates the X and Y attributes of the seeker to reflect its new position.
     * The new position should be closer to the player, by finding the
     * difference between the X or Y co-ordinate of this seeker and the player,
     * determining if it is positive or negative and then moving up/down or
     * left/right to change the value towards zero. If a seeker attempts to move
     * into a tile containing the player, the player should have their health
     * reduced by some amount.
     *
     * @param a The Seeker that needs to be moved
     */
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

    /**
     * Moves all chasers on the current level. The method checks for non-null
     * elements in the chasers array and calls the moveChaser method for each
     * one that is not null.
     */
    private void moveChasers() {
        for (int i = 0; i < chasers.length; i++) {
            if (chasers[i] != null) {
                moveChaser(chasers[i]);
            }
        }
    }

    /**
     * Moves a specific chaser in the game closer to the player. The method
     * updates the X and Y attributes of the chaser to reflect its new position.
     * The new position should be closer to the player, by finding the
     * difference between the X or Y co-ordinate of this chaser and the player,
     * determining if it is positive or negative and then moving up/down or
     * left/right to decrease the value towards a distance of zero. If a chaser
     * attempts to move into a tile with the player, the players health should
     * be reduced by some amount instead.
     *
     * @param c The Chaser that needs to be moved
     */
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

    /**
     * Called in response to the player collecting the Fuel and returning to the
     * Car tile. The method increases the valued of cleared by one, resets the
     * numChasers variable to zero, sets fuelCollected to false, generates a new
     * level by calling thegenerateLevel method, fills the spawns ArrayList with
     * suitable spawn locations, then spawns Seekers, clears the chasers array
     * and spawns the Fuel. Finally it places the player in the new level by
     * calling the placePlayer() method. Note that a new player object should
     * not be created here as this will reset the player's health to maximum.
     */
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

    /**
     * Places the player in a level by setting the player objects X and Y
     * position values to the tile that contains the Car.
     */
    private void placePlayer() {
        if (tiles[roadX][carY] == TileType.CAR) {
            player.setPosition(roadX, carY - 1);
        } else {
            player.setPosition(carX + 1, roadY);
        }
    }

    /**
     * Performs a single turn of the game when the user presses a key on the
     * keyboard. This method moves any monsters then checks if the player is
     * dead, exiting the game or resetting it. It checks if the player has
     * collected the fuel and returned to the Car to win the level and calls the
     * newLevel() method if it does. It also checks if the player has landed on
     * a nest tile. If it has, then a chaser is spawned. The method checks if
     * the health has been collected as well. Finally it requests the GUI to redraw the
     * game level by passing it the tiles, player, monsters and fuel for the
     * current level.
     */
    public void doTurn() {
        if (turnNumber % 5 == 0) {
            moveSeekers();
        }

        if (turnNumber % 2 == 0) {
            moveChasers();
        }

//        if (player.getHealth() < 1) {
//            System.exit(0);
//        }

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

    /**
     * Starts a game. This method generates a level, finds spawn positions in
     * the level, spawns monsters, fuel and the player and then requests the GUI
     * to update the level on screen using the information on tiles, player,
     * monsters and fuel.
     */
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

    public static void youWin(Graphics g) {
        try {
            Graphics2D g2 = (Graphics2D) g;
            Image youWin = ImageIO.read(new File("assets/you-win.png"));
            if (cleared == 5) {
                g2.drawImage(youWin, 0, 0, 816,615,null);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void gameOver(Graphics g) {
        try {
            Graphics2D g2 = (Graphics2D) g;
            Image gameOver = ImageIO.read(new File("assets/game-over.png"));
            if (player.getHealth() < 1) {
                g2.drawImage(gameOver, 0, 0, 816,615,null);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
