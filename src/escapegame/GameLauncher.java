package escapegame;

import java.awt.*;

public class GameLauncher {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameGUI gui = new GameGUI();
                gui.setVisible(true);
                GameEngine eng = new GameEngine(gui);
                InputHandler i = new InputHandler(eng);
                gui.registerKeyHandler(i);
                eng.startGame();
            }
        });
    }
}
