package classes;

import classes.entities.EntityObject;
import classes.entities.Player;
import classes.entities.projectile.PlayerProjectile;
import classes.entities.projectile.ProjectilePrototype;
import classes.exceptions.GameInitializationException;
import classes.util.controllers.ControllerComponents;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.GameManagerComponents;
import classes.util.managers.TileManager;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;


public class Game {

    private Properties gameProperties;

    private static Game game;

    private ControllerComponents controllerComponents;

    private GameManagerComponents gameManagerComponents;

    private Player player;

    private ArrayList<EntityObject> entities;

    private Game() {}

    public static Game getInstance() {
        if (game == null) {
            game = new Game();
        }

        return game;
    }

    public void setupGame() throws GameInitializationException {
        this.gameProperties = new Properties();

        try {
            loadPropertiesFile();
        } catch (IOException e) {
            throw new GameInitializationException("Error loading properties file: ", e);
        }

        this.entities = new ArrayList<>();
        this.controllerComponents = new ControllerComponents(new KeyboardController(), new MouseController());
        this.player = new Player(controllerComponents.getKeyboardController(), controllerComponents.getMouseController());
        this.gameManagerComponents = new GameManagerComponents(new TileManager());
    }

    public void setProperty(String key, String value) {
        gameProperties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return gameProperties.getProperty(key);
    }

    private void loadPropertiesFile() throws IOException {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String gameConfigPath = rootPath + "game.properties";

        try (FileInputStream inputStream = new FileInputStream(gameConfigPath)) {
            gameProperties.load(inputStream);
        }
    }

    public ControllerComponents getControllerComponents() {
        return controllerComponents;
    }

    public GameManagerComponents getGameManagerComponents() {
        return gameManagerComponents;
    }

    public Player getPlayer() {
        return player;
    }

    public void addEntity(EntityObject entityObject) {
        entities.add(entityObject);
    }

    public void renderEntities(Graphics2D g2){
        getGameManagerComponents().getTileManager().render(g2);
        ArrayList<ProjectilePrototype> playerProjectiles = player.getProjectiles();

        for(ProjectilePrototype projectile : playerProjectiles) {
            if(projectile != null && projectile.getRenderComponent().isAlive()) {
                projectile.render(g2);
            }
        }
        player.render(g2);

//        for(EntityObject entity : entities) {
//            if(entity != null) {
//                entity.render(g2);
//            }
//        }
    }

    public void updateEntities() {
        player.update();

//        for(EntityObject entity : entities) {
//            if(entity != null && entity.getRenderComponent().isAlive()) {
//                entity.update();
//            } else {
//                assert entity != null;
//                entity.kill();
//            }
//
//        }
    }

    public double getPlayerMaxViewableX() {
        return player.getPositionComponent().getWorldPositionX().doubleValue() +
                player.getPositionComponent().getScreenPositionX().doubleValue();
    }

    public double getPlayerMinViewableX() {
        return player.getPositionComponent().getWorldPositionX().doubleValue() -
                player.getPositionComponent().getScreenPositionX().doubleValue();
    }

    public double getPlayerMaxViewableY() {
        return player.getPositionComponent().getWorldPositionY().doubleValue() +
                player.getPositionComponent().getScreenPositionY().doubleValue();
    }

    public double getPlayerMinViewableY() {
        return player.getPositionComponent().getWorldPositionY().doubleValue() -
                player.getPositionComponent().getScreenPositionY().doubleValue();
    }

    public int getScreenWidth() {
        int tileSize = Integer.parseInt(game.getProperty("TILE_SIZE"));
        int maxScreenColumns = Integer.parseInt(game.getProperty("MAX_SCREEN_COL"));

        return tileSize * maxScreenColumns; // Returns screen width based on tile size and number of columns
    }

    public int getScreenHeight() {
        int tileSize = Integer.parseInt(game.getProperty("TILE_SIZE"));
        int maxScreenRows = Integer.parseInt(game.getProperty("MAX_SCREEN_ROW"));

        return tileSize * maxScreenRows; // Returns screen height based on tile size and number of rows
    }


}
