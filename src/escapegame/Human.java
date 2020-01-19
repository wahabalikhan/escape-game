package escapegame;

public class Human extends Entity {
    private int maHealth;
    private int health;

    public Human(int h, int x, int y) {
        maHealth = h;
        health = h;
        setPosition(x, y);
    }

    public int getMaHealth() {
        return maHealth;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int amount) {
        health += amount;
        if (health > maHealth) {
            health = maHealth;
        }

        if (health < 0) {
            health = 0;
        }
    }
}
