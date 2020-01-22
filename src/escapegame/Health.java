package escapegame;

/**
 * The Health class extends the Entity class, and is used to track and use the
 * position of the Health in the game.
 */
public class Health extends Entity {

    /**
     * Creates a Health object with specified position on the game board.
     * @param x Starting X position for the health.
     * @param y Starting Y position for the health.
     */
    public Health(int x, int y) {
        setPosition(x, y);
    }
}
