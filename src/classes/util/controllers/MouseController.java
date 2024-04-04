package classes.util.controllers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseMotionListener, MouseListener {

    private int mousePositionX;
    private int mousePositionY;
    private boolean mouseClicked;

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClicked = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseClicked = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        setMousePositionX(e.getX());
        setMousePositionY(e.getY());
    }

    public int getMousePositionX() {
        return mousePositionX;
    }

    public void setMousePositionX(int mousePositionX) {
        this.mousePositionX = mousePositionX;
    }

    public int getMousePositionY() {
        return mousePositionY;
    }

    public void setMousePositionY(int mousePositionY) {
        this.mousePositionY = mousePositionY;
    }

    public boolean isMouseClicked() {
        return this.mouseClicked;
    }
}
