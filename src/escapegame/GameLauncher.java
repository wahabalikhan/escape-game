package escapegame;

import java.awt.*;

/**
 * This class is the entry point for the project, containing the main method that
 * starts a game. It creates instances of the different classes of this project
 * and connects them appropriately.
 */
public class GameLauncher {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            /**
             * The run method starts the game in a separate thread. It creates
             * the GUI, the engine and the input handler classes and connects
             * those that call other objects.
             */
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
