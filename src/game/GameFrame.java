package game;

import game.ui.titlebar.TitleBarPanel;
import game.util.handlers.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class GameFrame extends JFrame {

    private static final String WINDOW_TITLE = "Bumbo Hell";

    public GameFrame() throws HeadlessException {
        super(WINDOW_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(Objects.requireNonNull(ImageHandler.getImageIcon("src/res/icon.png")).getImage());
        this.setUndecorated(true);
        double frameCornerArcWidth = 40.0;
        double frameCornerArcHeight = 40.0;
        this.setShape(new RoundRectangle2D.Double(
                0,
                0,
                1280,
                768,
                frameCornerArcWidth,
                frameCornerArcHeight
        ));

        setupGameWindow();
    }

    private void setupGameWindow() {
        this.setLayout(new BorderLayout());
        this.add(new TitleBarPanel(this), BorderLayout.NORTH);

        GamePanel gamePanel = createGamePanel();
        this.add(gamePanel, BorderLayout.CENTER);
        displayGameWindow();
        this.requestFocusInWindow();
        gamePanel.requestFocusInWindow();
    }

    private GamePanel createGamePanel() {
        GamePanel gamePanel = new GamePanel(this);
        gamePanel.startGameThread();
        return gamePanel;
    }

    private void displayGameWindow() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
