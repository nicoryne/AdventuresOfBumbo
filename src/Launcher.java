import game.exceptions.GameInitializationException;
import game.ui.GamePanel;
import game.ui.titlebar.TitleBarPanel;
import game.util.handlers.ImageHandler;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

/**
 * The Main class serves as the entry point for the "Adventures of Bumbo" game application.
 * It sets up the game window and initializes the game panel.
 */
public class Launcher {

    private static final String WINDOW_TITLE = "Bumbo Hell"; // Title of the game window

    private JFrame window;

    public static void main(String[] args) throws GameInitializationException {
        Launcher launcher = new Launcher();
        launcher.setupGameWindow();
    }

    /**
     * Sets up the game window by creating a JFrame and adding the game panel to it.
     */
    private void setupGameWindow() throws GameInitializationException {
        window = createGameWindow();
        window.setLayout(new BorderLayout());
        window.add(new TitleBarPanel(window), BorderLayout.NORTH);

        GamePanel gamePanel = createGamePanel();
        window.add(gamePanel, BorderLayout.CENTER);
        displayGameWindow(window);
        window.requestFocusInWindow();
        gamePanel.requestFocusInWindow();
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
        window.setIconImage(Objects.requireNonNull(ImageHandler.getImageIcon("src/res/icon.png")).getImage());
        window.setUndecorated(true);
        double frameCornerArcWidth = 40.0;
        double frameCornerArcHeight = 40.0;
        window.setShape(new RoundRectangle2D.Double(
                0,
                0,
                768,
                576,
                frameCornerArcWidth,
                frameCornerArcHeight
        ));
        return window;
    }

    /**
     * Creates an instance of the game panel.
     *
     * @return The created game panel.
     */
    private GamePanel createGamePanel() throws GameInitializationException {
        GamePanel gamePanel = new GamePanel(window);
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
