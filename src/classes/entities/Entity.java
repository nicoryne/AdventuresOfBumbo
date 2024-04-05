package classes.entities;

import java.awt.*;

public interface Entity {

    void update();

    void render(Graphics2D g2);

    void kill();

    void spawn(double x, double y);


}
