package game.ui.titlebar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DraggableTitleBarListener implements MouseListener, MouseMotionListener {

    private Point startDrag;

    private Point startLocation;

    private final JPanel titleBarPanel;

    private final JFrame targetFrame;

    public DraggableTitleBarListener(JPanel titleBarPanel, JFrame targetFrame) {
       this.startDrag = new Point();
       this.startLocation = new Point();
       this.titleBarPanel = titleBarPanel;
       this.targetFrame = targetFrame;
    }

    private Point getScreenLocation(MouseEvent e) {
        Point CURSOR_LOCATION = e.getPoint();
        Point TARGET_LOCATION = titleBarPanel.getLocationOnScreen();

        return new Point(
                (int) (TARGET_LOCATION.getX() + CURSOR_LOCATION.getX()),
                (int) (TARGET_LOCATION.getY() + CURSOR_LOCATION.getY()));
    }
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        startDrag = getScreenLocation(e);
        startLocation = targetFrame.getLocation();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int offsetX = e.getXOnScreen() - startDrag.x;
        int offsetY = e.getYOnScreen() - startDrag.y;

        int newLocationX = startLocation.x + offsetX;
        int newLocationY = startLocation.y + offsetY;

        targetFrame.setLocation(newLocationX, newLocationY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
