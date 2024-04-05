import classes.exceptions.GameInitializationException;
import classes.ui.GamePanel;

import javax.swing.JFrame;

/**
 * The Main class serves as the entry point for the "Adventures of Bumbo" game application.
 * It sets up the game window and initializes the game panel.
 */
public class Main {

    private static final String WINDOW_TITLE = "Bumbo Hell"; // Title of the game window

    public static void main(String[] args) throws GameInitializationException {
        Main main = new Main();
        main.setupGameWindow();
    }

    /**
     * Sets up the game window by creating a JFrame and adding the game panel to it.
     */
    private void setupGameWindow() throws GameInitializationException {
        JFrame window = createGameWindow();
        window.add(createGamePanel());
        displayGameWindow(window);
    }

    /**
     * Creates and configures a JFrame for the game window.
     *
     * @return The configured JFrame for the game window.
     */
    private JFrame createGameWindow() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application when window is closed
        window.setResizable(false); // Disable window resizing
        window.setTitle(WINDOW_TITLE); // Set the title of the window
        return window;
    }

    /**
     * Creates an instance of the game panel.
     *
     * @return The created game panel.
     */
    private GamePanel createGamePanel() throws GameInitializationException {
        GamePanel gamePanel = new GamePanel();
        gamePanel.setupGame();
        gamePanel.startGameThread();
        return gamePanel;
    }

    /**
     * Displays the game window.
     *
     * @param window The JFrame representing the game window to be displayed.
     */
    private void displayGameWindow(JFrame window) {
        window.pack(); // Pack the components inside the window
        window.setLocationRelativeTo(null); // Center the window on the screen
        window.setVisible(true); // Make the window visible
    }
}
