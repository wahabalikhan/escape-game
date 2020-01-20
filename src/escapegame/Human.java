package escapegame;

public class Human extends Entity {
    private int maxHealth;
    private int health;

    public Human(int h, int x, int y) {
        maxHealth = h;
        health = h;
        setPosition(x, y);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }

        if (health < 0) {
            health = 0;
        }
    }
}
