package escapegame;

public abstract class Entity {
    private int xPos;
    private int yPos;

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public final void setPosition(int x, int y) {
        xPos = x;
        yPos = y;
    }
}
