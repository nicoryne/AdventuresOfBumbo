package classes.entities.util;

import classes.entities.projectile.ProjectilePrototype;

import java.util.ArrayList;
import java.util.Iterator;

public interface RangedEntity {

    void setProjectiles(ArrayList<ProjectilePrototype> projectiles);

    ArrayList<ProjectilePrototype> getProjectiles();

    default void updateProjectiles() {
        Iterator<ProjectilePrototype> iterator = getProjectiles().iterator();
        while (iterator.hasNext()) {
            ProjectilePrototype projectile = iterator.next();
            if (projectile.getRenderComponent().isAlive()) {
                projectile.update();
            } else {
                iterator.remove();
            }
        }
    }
}
