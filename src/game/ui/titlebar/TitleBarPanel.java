package game.ui.titlebar;

import game.Game;
import game.util.handlers.ImageHandler;
import game.util.managers.FontManager;
import services.LoggerHelper;

import javax.swing.*;
import java.awt.*;

public class TitleBarPanel extends JPanel {

    private JLabel iconLabel;

    private JLabel titleLabel;

    private JPanel buttonContainer;

    private TitleBarButton exitButton;

    private TitleBarButton minimizeButton;

    private int iconWidth = 32;

    private int iconHeight = 32;

    private static final Dimension BUTTON_CONTAINER_SIZE = (new Dimension(100, 50));
    private static final Dimension TITLE_PANEL_SIZE = (new Dimension(768, 40));
    private static final Color PANEL_BACKGROUND_COLOR = Color.black;
    private static final Color TEXT_FOREGROUND_COLOR = Color.white;
    private static final String FONT_NAME = "Dofded";
    private static final float FONT_SIZE = 16f;

    public TitleBarPanel(JFrame windowFrame){
        this.iconLabel = new JLabel();
        this.titleLabel = new JLabel();
        this.buttonContainer = new JPanel();
        this.exitButton = new TitleBarButton("X", e -> handleExit());
        this.minimizeButton = new TitleBarButton("---", e -> handleIconified(windowFrame));

        DraggableTitleBarListener draggableTitleBarListener = new DraggableTitleBarListener(this, windowFrame);
        windowFrame.addMouseMotionListener(draggableTitleBarListener);
        windowFrame.addMouseListener(draggableTitleBarListener);

        decorateComponents();

        this.setLayout(new BorderLayout());
        this.setBackground(PANEL_BACKGROUND_COLOR);
        this.setPreferredSize(TITLE_PANEL_SIZE);
        this.add(iconLabel, BorderLayout.WEST);
        this.add(titleLabel, BorderLayout.CENTER);
        this.add(buttonContainer, BorderLayout.EAST);
    }

    private void handleExit() {
        LoggerHelper.logInfo("Exiting program");
        System.exit(0);
    }

    private void handleIconified(JFrame windowFrame) {
        LoggerHelper.logInfo("Minimized program");
        windowFrame.setExtendedState(Frame.ICONIFIED);
    }

    private void decorateComponents() {
        decorateIcon();
        decorateTitleLabel();
        decorateButtonContainer();
    }

    private void decorateIcon() {
        ImageIcon originalIcon = ImageHandler.getImageIcon("src/res/icon.png");
        assert originalIcon != null;
        ImageIcon resizedIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

        iconLabel.setIcon(resizedIcon);
        iconLabel.setLayout(new BorderLayout());
        iconLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 0),
                iconLabel.getBorder()));
    }

    private void decorateTitleLabel(){
        Font font = FontManager.getInstance().getFont(FONT_NAME, FONT_SIZE);

        titleLabel.setFont(font);

        titleLabel.setText("umbo Hell");
        titleLabel.setForeground(TEXT_FOREGROUND_COLOR);
    }

    private void decorateButtonContainer() {
        buttonContainer.setLayout(new BorderLayout());
        buttonContainer.setPreferredSize(BUTTON_CONTAINER_SIZE);
        buttonContainer.setOpaque(false);
        buttonContainer.add(exitButton, BorderLayout.EAST);
        buttonContainer.add(minimizeButton, BorderLayout.CENTER);
    }
}
