package game.ui.components.titlebar;

import game.exceptions.FontHandlerException;
import game.util.handlers.FontHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class TitleBarButton extends JButton {
    private static final String FONT_NAME = "font-1.ttf";
    private static final float FONT_SIZE = 16f;
    private static final Dimension BUTTON_SIZE = new Dimension(50, 50);
    private static final Color TEXT_FOREGROUND_COLOR = Color.WHITE;
    private static final Color BUTTON_BACKGROUND_DEFAULT_COLOR = Color.BLACK;
    private static final Color BUTTON_BACKGROUND_SELECTED_COLOR = Color.RED;

    public TitleBarButton(String text, ActionListener actionListener) {
        Font font;
        try {
            font = FontHandler.getFont(FONT_NAME, FONT_SIZE);
        } catch (FontHandlerException e) {
            throw new RuntimeException(e);
        }

        this.setPreferredSize(BUTTON_SIZE);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setFont(font);
        this.setForeground(TEXT_FOREGROUND_COLOR);
        this.setBackground(BUTTON_BACKGROUND_DEFAULT_COLOR);
        this.setText(text);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                actionListener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                TitleBarButton.super.setBackground(BUTTON_BACKGROUND_SELECTED_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                TitleBarButton.super.setBackground(BUTTON_BACKGROUND_DEFAULT_COLOR);
            }
        });
    }
}
