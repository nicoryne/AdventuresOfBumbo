package classes.entities;

import classes.ui.components.GamePanel;

import java.awt.*;

public interface RenderableEntity {
    void update();

    void render(Graphics2D g);
}
