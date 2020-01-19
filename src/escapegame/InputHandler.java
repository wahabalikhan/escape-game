package escapegame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    GameEngine engine;

    public InputHandler(GameEngine eng) {
        engine = eng;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                engine.movePlayerLeft();
                break;
            case KeyEvent.VK_RIGHT:
                engine.movePlayerRight();
                break;
            case KeyEvent.VK_UP:
                engine.movePlayerUp();
                break;
            case KeyEvent.VK_DOWN:
                engine.movePlayerDown();
                break;
        }
        engine.doTurn();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
